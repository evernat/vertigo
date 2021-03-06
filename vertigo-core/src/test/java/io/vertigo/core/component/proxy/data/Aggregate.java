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
package io.vertigo.core.component.proxy.data;

import io.vertigo.core.component.Component;

/**
 * Example of a component defined as a proxy.
 * each method is marked with a specific annotation that explains what to do.
 * @author pchretien
 *
 */
public interface Aggregate extends Component {

	@AggregatorAnnotation(operation = AggregatorOperation.count)
	long count(int a);

	@AggregatorAnnotation(operation = AggregatorOperation.count)
	long count(int a, int b);

	@AggregatorAnnotation(operation = AggregatorOperation.count)
	long count(int a, int b, int c);

	@AggregatorAnnotation(operation = AggregatorOperation.max)
	int max(int a);

	@AggregatorAnnotation(operation = AggregatorOperation.max)
	int max(int a, int b);

	@AggregatorAnnotation(operation = AggregatorOperation.max)
	int max(int a, int b, int c);

	@AggregatorAnnotation(operation = AggregatorOperation.min)
	int min(int a);

	@AggregatorAnnotation(operation = AggregatorOperation.min)
	int min(int a, int b);

	@AggregatorAnnotation(operation = AggregatorOperation.min)
	int min(int a, int b, int c);

}
