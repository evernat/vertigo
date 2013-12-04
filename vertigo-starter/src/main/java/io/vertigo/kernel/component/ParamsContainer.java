/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.kernel.component;

import io.vertigo.kernel.lang.Assertion;

import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 * Container contenant des param�tres.
 * 
 * @author pchretien
 */
public final class ParamsContainer implements Container {
	private final Map<String, String> params;

	public ParamsContainer(final Map<String, String> params) {
		Assertion.checkNotNull(params);
		//---------------------------------------------------------------------
		this.params = params;
	}

	/** {@inheritDoc} */
	public boolean contains(final String id) {
		Assertion.checkNotNull(id);
		//-----------------------------------------------------------------
		return params.containsKey(id);
	}

	/** {@inheritDoc} */
	public <O> O resolve(final String id, final Class<O> clazz) {
		Assertion.checkNotNull(id);
		Assertion.checkNotNull(params.containsKey(id), "Le param�tre '{0}' de type '{1}' n'a pas �t� d�fini.", id, clazz.getSimpleName());
		// ---------------------------------------------------------------------
		final Object value = getParam(params, id, clazz);
		final Class<O> type = box(clazz);
		Assertion.checkArgument(type.isAssignableFrom(value.getClass()), "Composant/param�tre '{0}' type '{1}' , type attendu '{2}'", id, value.getClass(), clazz);
		return type.cast(value);
	}

	/** {@inheritDoc} */
	public Set<String> keySet() {
		return Collections.unmodifiableSet(params.keySet());
	}

	/**
	 * R�cup�ration d'un param�tre typ� par son nom.
	 * @param paramName Nom du param�tre
	 * @param paramType Type du param�tre attendu
	 * @return Valeur sous forme texte du param�tre
	 */
	private static Object getParam(final Map<String, String> params, final String paramName, final Class<?> paramType) {
		Assertion.checkNotNull(params);
		Assertion.checkNotNull(paramName);
		//---------------------------------------------------------------------
		final String value = params.get(paramName);
		return cast(paramType, value);
	}

	private static Object cast(final Class<?> paramType, final String value) {
		if (String.class.equals(paramType)) {
			return value;
		} else if (Boolean.class.equals(paramType) || boolean.class.equals(paramType)) {
			return Boolean.valueOf(value);
		} else if (Integer.class.equals(paramType) || int.class.equals(paramType)) {
			return Integer.valueOf(value);
		} else if (Long.class.equals(paramType) || long.class.equals(paramType)) {
			return Long.valueOf(value);
		}
		return null;
	}

	private static Class box(final Class<?> clazz) {
		Assertion.checkNotNull(clazz);
		//---------------------------------------------------------------------
		if (clazz.isPrimitive()) {
			//Boolean n'est pas assignable � boolean
			//boolean n'est pas assignable � Boolean
			//Or dans notre cas value est un objet et clazz peut �tre un type primitif !
			if (boolean.class.equals(clazz)) {
				return Boolean.class;
			} else if (byte.class.equals(clazz)) {
				return Byte.class;
			} else if (char.class.equals(clazz)) {
				return Character.class;
			} else if (short.class.equals(clazz)) {
				return Short.class;
			} else if (int.class.equals(clazz)) {
				return Integer.class;
			} else if (long.class.equals(clazz)) {
				return Long.class;
			} else if (float.class.equals(clazz)) {
				return Float.class;
			} else if (double.class.equals(clazz)) {
				return Double.class;
			}
			throw new IllegalArgumentException(clazz + "est un type primitif non g�r�");
		}
		return clazz;
	}
}
