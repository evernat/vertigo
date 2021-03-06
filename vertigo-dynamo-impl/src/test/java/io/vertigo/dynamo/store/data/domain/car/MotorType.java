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
package io.vertigo.dynamo.store.data.domain.car;

import io.vertigo.dynamo.domain.model.DtStaticMasterData;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class MotorType implements DtStaticMasterData {
	private static final long serialVersionUID = 1L;

	private String mtyCd;
	private String label;

	/** {@inheritDoc} */
	@Override
	public UID<MotorType> getUID() {
		return UID.of(this);
	}

	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'id'.
	 * @return String mtyCd <b>Obligatoire</b>
	 */
	@Field(domain = "DoString", type = "ID", required = true, label = "id")
	public String getMtyCd() {
		return mtyCd;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'id'.
	 * @param mtyCd String <b>Obligatoire</b>
	 */
	public void setMtyCd(final String mtyCd) {
		this.mtyCd = mtyCd;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Label'.
	 * @return String label <b>Obligatoire</b>
	 */
	@Field(domain = "DoFullText", required = true, label = "Label")
	public String getLabel() {
		return label;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Label'.
	 * @param label String <b>Obligatoire</b>
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
