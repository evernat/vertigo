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
package io.vertigo.core.node.config.xml;

import io.vertigo.core.node.component.data.BioManager;
import io.vertigo.core.node.component.data.BioManagerImpl;
import io.vertigo.core.node.component.data.MathManager;
import io.vertigo.core.node.component.data.MathManagerImpl;
import io.vertigo.core.node.component.data.MathPlugin;
import io.vertigo.core.node.config.Features;
import io.vertigo.core.param.Param;

/**
 * A feature for the Bio Module.
 * @author mlaroche
 *
 */
public class BioFeatures extends Features {

	public BioFeatures() {
		super("bio");
	}

	@Override
	protected void buildFeatures() {
		getModuleConfigBuilder()
				.addComponent(BioManager.class, BioManagerImpl.class)
				.addComponent(MathManager.class, MathManagerImpl.class, Param.of("start", "100"))
				.addPlugin(MathPlugin.class, Param.of("factor", "20"));
	}

}