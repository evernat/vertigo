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
package io.vertigo.commons.node;

import org.h2.Driver;

import io.vertigo.app.config.NodeConfig;
import io.vertigo.commons.CommonsFeatures;
import io.vertigo.commons.plugins.app.registry.db.DbAppNodeRegistryPlugin;
import io.vertigo.core.param.Param;

public class DbNodeRegistryPluginTest extends AbstractNodeManagerTest {

	@Override
	protected NodeConfig buildNodeConfig() {

		return buildRootNodeConfig()
				.addModule(new CommonsFeatures()
						.withNodeRegistryPlugin(DbAppNodeRegistryPlugin.class,
								Param.of("driverClassName", Driver.class.getName()),
								Param.of("jdbcUrl", "jdbc:h2:mem:database"))
						.build())
				.build();
	}

}
