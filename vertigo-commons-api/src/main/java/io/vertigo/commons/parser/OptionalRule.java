/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2016, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.commons.parser;

import java.util.Optional;

import io.vertigo.lang.Assertion;

/**
 * An optional rule.
 * This rule is usefull each time there is an optional part inside a more global expression.
 *
 * @author pchretien
 * @param<R> Type of the product text parsing
 */
public final class OptionalRule<R> implements Rule<Optional<R>> {
	private final String expression;
	private final Rule<R> rule;

	/**
	 * Constructor.
	 * @param rule Optional rule
	 */
	public OptionalRule(final Rule<R> rule) {
		super();
		Assertion.checkNotNull(rule);
		//-----
		expression = rule.getExpression() + "?";
		this.rule = rule;
	}

	/** {@inheritDoc} */
	@Override
	public String getExpression() {
		return expression;
	}

	@Override
	public Parser<Optional<R>> createParser() {
		return new Parser<Optional<R>>() {
			private Optional<R> option;

			/** {@inheritDoc} */
			@Override
			public int parse(final String text, final int start) throws NotFoundException {
				int index = start;
				//-----
				option = Optional.empty();
				try {
					final Parser<R> parser = rule.createParser();
					index = parser.parse(text, index);
					option = Optional.ofNullable(parser.get());
				} catch (final NotFoundException e) {
					//As the rule is optional, if we found nothing then the index doesn't move and no exception is thrown.
				}
				return index;
			}

			@Override
			public Optional<R> get() {
				return option;
			}
		};
	}
}