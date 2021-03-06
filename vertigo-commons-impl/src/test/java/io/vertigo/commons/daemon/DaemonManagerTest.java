/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, Vertigo.io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.commons.daemon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import io.vertigo.AbstractTestCaseJU5;
import io.vertigo.app.config.NodeConfig;
import io.vertigo.app.config.ModuleConfig;
import io.vertigo.commons.CommonsFeatures;
import io.vertigo.commons.analytics.AnalyticsManager;
import io.vertigo.commons.analytics.health.HealthCheck;
import io.vertigo.commons.analytics.health.HealthStatus;

/**
 * @author TINGARGIOLA
 */
public final class DaemonManagerTest extends AbstractTestCaseJU5 {

	@Inject
	private DaemonManager daemonManager;
	@Inject
	private AnalyticsManager analyticsManager;
	@Inject
	private FakeComponent fakeComponent;

	@Override
	protected NodeConfig buildNodeConfig() {
		return NodeConfig.builder()
				.beginBoot()
				.endBoot()
				.addModule(new CommonsFeatures()
						.build())
				.addModule(ModuleConfig.builder("myAspects")
						.addAspect(DaemonFakeAspect.class)
						.build())
				.addModule(ModuleConfig.builder("myApp")
						.addComponent(FakeComponent.class)
						.build())
				.build();
	}

	@Test
	public void testSimple() throws Exception {
		DaemonStat daemonStat = daemonManager.getStats().stream()
				.filter(stat -> FakeComponent.SIMPLE_DAEMON_NAME.equals(stat.getDaemonName()))
				.findFirst().get();
		assertEquals(0, daemonStat.getCount());
		assertEquals(0, daemonStat.getFailures());
		assertEquals(0, daemonStat.getSuccesses());
		assertEquals(DaemonStat.Status.pending, daemonStat.getStatus());

		assertEquals(0, fakeComponent.getExecutionCount());
		// -----
		Thread.sleep(5000); //soit deux execs

		daemonStat = daemonManager.getStats().stream()
				.filter(stat -> FakeComponent.SIMPLE_DAEMON_NAME.equals(stat.getDaemonName()))
				.findFirst().get();
		assertEquals(2, daemonStat.getCount());
		assertEquals(1, daemonStat.getFailures());
		assertEquals(1, daemonStat.getSuccesses());
		assertEquals(DaemonStat.Status.pending, daemonStat.getStatus());

		assertTrue(fakeComponent.getExecutionCount() > 0);

		final HealthCheck daemonsExecHealthCheck = analyticsManager.getHealthChecks()
				.stream()
				.filter(healtChk -> "daemons".equals(healtChk.getFeature()) && "lastExecs".equals(healtChk.getName()))
				.findFirst()
				.get();

		assertTrue(daemonsExecHealthCheck.getMeasure().getStatus() == HealthStatus.GREEN);

	}

}
