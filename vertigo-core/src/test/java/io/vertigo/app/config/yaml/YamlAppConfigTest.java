/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2018, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.app.config.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import io.vertigo.app.AutoCloseableApp;
import io.vertigo.app.config.AppConfig;
import io.vertigo.core.spaces.component.data.BioManager;

public final class YamlAppConfigTest {

	@Test
	public void testBoot() {

		final AppConfig appConfig = new YamlAppConfigBuilder(new Properties())
				.withFiles(getClass(), "bio-boot.yaml")
				.build();

		testBioManager(appConfig);
	}

	@Test
	public void testNoBoot() {

		final AppConfig appConfig = new YamlAppConfigBuilder(new Properties())
				.withFiles(getClass(), "bio.yaml")
				.build();

		testBioManager(appConfig);
	}

	private void testBioManager(final AppConfig appConfig) {
		try (AutoCloseableApp app = new AutoCloseableApp(appConfig)) {
			assertEquals(app, app);
			assertTrue(app.getComponentSpace().contains("bioManager"));
			final BioManager bioManager = app.getComponentSpace().resolve(BioManager.class);
			final int res = bioManager.add(1, 2, 3);
			assertEquals(366, res);
			assertTrue(bioManager.isActive());
		}
	}
}
