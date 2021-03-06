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
package io.vertigo.app.config.yaml;

import io.vertigo.app.config.Feature;
import io.vertigo.app.config.Features;
import io.vertigo.core.param.Param;
import io.vertigo.core.spaces.component.data.BioManager;
import io.vertigo.core.spaces.component.data.BioManagerImpl;
import io.vertigo.core.spaces.component.data.MathManager;
import io.vertigo.core.spaces.component.data.MathManagerImpl;

/**
 * A feature for the Bio Module.
 * @author mlaroche
 *
 */
public class YamlBioFeatures extends Features {

	public YamlBioFeatures() {
		super("yaml-bio");
	}

	@Feature("bio")
	public YamlBioFeatures withBio() {
		getModuleConfigBuilder()
				.addComponent(BioManager.class, BioManagerImpl.class);
		return this;
	}

	@Feature("math")
	public YamlBioFeatures withMath(final Param... params) {
		getModuleConfigBuilder()
				.addComponent(MathManager.class, MathManagerImpl.class, params);
		return this;
	}

	@Override
	protected void buildFeatures() {
		//nothing by default
	}

}
