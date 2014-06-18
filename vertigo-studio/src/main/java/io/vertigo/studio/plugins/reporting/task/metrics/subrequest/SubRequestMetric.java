package io.vertigo.studio.plugins.reporting.task.metrics.subrequest;

import io.vertigo.studio.reporting.Metric;

/**
 * Composant d'affichage des résultats du plugin SsRequeteCountPlugin.
 * 
 * @author tchassagnette
 */
public final class SubRequestMetric implements Metric {
	private final int subRequestCount;

	/**
	 * Constructeur par défaut.
	 */
	public SubRequestMetric(final int subRequestCount) {
		this.subRequestCount = subRequestCount;
	}

	public String getTitle() {
		return "Nombre de ss-requêtes";
	}

	public Integer getValue() {
		return subRequestCount;
	}

	/** {@inheritDoc} */
	public String getValueInformation() {
		return null;
	}

	public String getUnit() {
		return "";
	}

	public Status getStatus() {
		return Status.Executed;
	}
}
