package io.vertigo.dynamo.plugins.work.rest;

import io.vertigo.commons.codec.CodecManager;
import io.vertigo.dynamo.impl.work.DistributedWorkerPlugin;
import io.vertigo.dynamo.work.WorkEngineProvider;
import io.vertigo.dynamo.work.WorkItem;
import io.vertigo.dynamo.work.WorkResultHandler;
import io.vertigo.kernel.exception.VRuntimeException;
import io.vertigo.kernel.lang.Activeable;
import io.vertigo.kernel.lang.Assertion;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Ex�cution synchrone et distante des Works avec un transfert par WS REST.
 * 
 * @author npiedeloup, pchretien
 * @version $Id: RestDistributedWorkerPlugin.java,v 1.13 2014/02/27 10:31:19 pchretien Exp $
 */
public final class RestDistributedWorkerPlugin implements DistributedWorkerPlugin, Activeable {
	private final long timeoutSeconds;
	private final Set<String> workTypes;
	private final MultipleWorkQueues multipleWorkQueues;
	private final WorkQueueRestServer workQueueRestServer;

	/**
	 * Constructeur.
	 * @param timeoutSeconds Timeout des travaux en attente de traitement
	 * @param workTypesAsString Liste des types de work distribu�s (s�parateur ;)
	 * @param codecManager Manager d'encodage/decodage
	 */
	@Inject
	public RestDistributedWorkerPlugin(@Named("timeoutSeconds") final long timeoutSeconds, @Named("workTypes") final String workTypesAsString, final CodecManager codecManager) {
		Assertion.checkArgument(timeoutSeconds < 10000, "Le timeout s'exprime en seconde.");
		Assertion.checkArgNotEmpty(workTypesAsString);
		//---------------------------------------------------------------------
		this.timeoutSeconds = timeoutSeconds;
		final String[] workTypesArray = workTypesAsString.split(";");//
		workTypes = new HashSet<>(Arrays.asList(workTypesArray));
		multipleWorkQueues = new MultipleWorkQueues();
		workQueueRestServer = new WorkQueueRestServer(multipleWorkQueues, 20 * 1000, codecManager);
	}

	/**
	 * @return Serveur REST de la WorkQueue. (appell�e par l'impl� Jersey)
	 */
	public WorkQueueRestServer getWorkQueueRestServer() {
		return workQueueRestServer;
	}

	/** {@inheritDoc} */
	public void start() {
		workQueueRestServer.start();
	}

	/** {@inheritDoc} */
	public void stop() {
		workQueueRestServer.stop();
	}

	/** {@inheritDoc} */
	public <WR, W> boolean canProcess(final WorkEngineProvider<WR, W> workEngineProvider) {
		return workTypes.contains(obtainWorkType(workEngineProvider));
	}

	/** {@inheritDoc} */
	public <WR, W> void process(final WorkItem<WR, W> workitem2) {
		Assertion.checkNotNull(workitem2);
		//---------------------------------------------------------------------
		final WorkResultHandlerSync<WR> workResultHandler = new WorkResultHandlerSync<>();
		final WorkItem<WR, W> workItem = new WorkItem<>(workitem2.getWork(), workitem2.getWorkEngineProvider(), workResultHandler);
		multipleWorkQueues.putWorkItem(obtainWorkType(workitem2.getWorkEngineProvider()), workItem);
		workResultHandler.waitResult(timeoutSeconds); //on attend le r�sultat
		//---
		workitem2.setResult(workResultHandler.getResultOrThrowError());//retourne le r�sultat ou lance l'erreur
	}

	/** {@inheritDoc} */
	public <WR, W> void schedule(final WorkItem<WR, W> workItem) {
		Assertion.checkNotNull(workItem);
		//---------------------------------------------------------------------
		multipleWorkQueues.putWorkItem(obtainWorkType(workItem.getWorkEngineProvider()), workItem);
	}

	private <WR, W> String obtainWorkType(final WorkEngineProvider<WR, W> workEngineProvider) {
		return workEngineProvider.getName();
	}

	private static final class WorkResultHandlerSync<WR> implements WorkResultHandler<WR> {
		private boolean started = false;
		private boolean finished = false;
		private WR result;
		private Throwable error;

		/** {@inheritDoc} */
		public void onStart() {
			started = true;
		}

		/**
		 * Attend la fin de ce Work.
		 * @param waitTimeoutSeconds temps maximum d'attente en seconde
		 */
		public synchronized void waitResult(final long waitTimeoutSeconds) {
			if (!finished) {
				try {
					wait(waitTimeoutSeconds * 1000); //attend un notify
				} catch (final InterruptedException e) {
					throw new VRuntimeException("Arret demand� : on stop le travail en cours");
				}
				if (!finished) {
					if (!started) {
						error = new VRuntimeException("Timeout : le traitement n'a pas �t� pris en charge en " + waitTimeoutSeconds + "s");
					} else {
						error = new VRuntimeException("Timeout : le traitement ne s'est pas termin� en " + waitTimeoutSeconds + "s");
					}
					//TODO : si timeout retirer de la file, ou d�sactiver le handler
				}
			}
		}

		/** {@inheritDoc} */
		public synchronized void onSuccess(final WR newResult) {
			this.result = newResult;
			finished = true;
			error = null; //si on a eut une erreur avant, ou un timeout : on reset l'erreur
			notifyAll(); //d�bloque le wait
		}

		/** {@inheritDoc} */
		public synchronized void onFailure(final Throwable newError) {
			this.error = newError;
			finished = true;
			notifyAll(); //d�bloque le wait
		}

		/**
		 * Retourne le r�sultat ou lance l'erreur re�u le cas echant.
		 * @return r�sultat
		 */
		public WR getResultOrThrowError() {
			if (error != null) {
				//si il ya une erreur 
				if (error instanceof Error) {
					throw Error.class.cast(error);
				}
				if (error instanceof RuntimeException) {
					throw RuntimeException.class.cast(error);
				}
				throw new VRuntimeException(error);
			}
			return result;
		}
	}

}
