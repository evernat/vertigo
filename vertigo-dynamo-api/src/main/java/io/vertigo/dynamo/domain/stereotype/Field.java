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
package io.vertigo.dynamo.domain.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pchretien
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Field {
	/**
	 * Type du champ.
	 */
	String type() default "DATA";

	/**
	 * Nom du domaine.
	 */
	String domain();

	/**
	 * Si le champ est non null.
	 */
	boolean required() default false;

	/**
	 * Libellé du champ.
	 */
	String label();

	/**
	 * Persistance du champ.
	 */
	boolean persistent() default true;
}
