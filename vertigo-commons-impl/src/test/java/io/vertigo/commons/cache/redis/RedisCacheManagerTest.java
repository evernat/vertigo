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
package io.vertigo.commons.cache.redis;

import io.vertigo.app.config.ModuleConfig;
import io.vertigo.app.config.NodeConfig;
import io.vertigo.commons.CommonsFeatures;
import io.vertigo.commons.cache.AbstractCacheManagerTest;
import io.vertigo.commons.cache.TestCacheDefinitionProvider;
import io.vertigo.core.param.Param;

/**
 * RedisCache Manager test class.
 * Uses RedisConnector from {@link io.vertigo.commons.impl.connectors.redis.RedisConnector}
 *
 * @author pchretien, dszniten
 */
public class RedisCacheManagerTest extends AbstractCacheManagerTest {
	// Unit tests use abstract class methods

	/**
	 * Max nbRows to 500.
	 */
	public RedisCacheManagerTest() {
		super(500);
	}

	@Override
	protected NodeConfig buildNodeConfig() {
		return NodeConfig.builder()
				.beginBoot()
				.endBoot()
				.addModule(new CommonsFeatures()
						.withCache()
						.withRedisConnector(
								Param.of("host", "redis-pic.part.klee.lan.net"),
								Param.of("port", "6379"),
								Param.of("database", "0"))
						.withRedisCache()
						.build())
				.addModule(ModuleConfig.builder("myApp")
						.addDefinitionProvider(TestCacheDefinitionProvider.class)
						.build())
				.build();
	}
}
