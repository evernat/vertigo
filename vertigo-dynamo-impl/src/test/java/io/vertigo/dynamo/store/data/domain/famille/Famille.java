/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.dynamo.store.data.domain.famille;

import io.vertigo.dynamo.domain.metamodel.DtFieldName;
import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.ListVAccessor;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.dynamo.store.data.domain.car.Car;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class Famille implements Entity {

	public enum CarFields implements DtFieldName<Car> {
		ID, MANUFACTURER, MODEL, DESCRIPTION, YEAR, KILO, PRICE, CONSOMMATION, MTY_CD, FAM_ID
	}

	private static final long serialVersionUID = 1L;

	private Long famId;
	private String libelle;

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_FAM_CAR_FAMILLE",
			fkFieldName = "FAM_ID",
			primaryDtDefinitionName = "DT_FAMILLE",
			primaryIsNavigable = false,
			primaryRole = "Famille",
			primaryLabel = "Famille",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DT_CAR",
			foreignIsNavigable = true,
			foreignRole = "VoituresFamille",
			foreignLabel = "Voitures de la famille",
			foreignMultiplicity = "0..*")
	private final ListVAccessor<Car> voituresFamilleAccessor = new ListVAccessor<>(this, "A_FAM_CAR_FAMILLE", "VoituresFamille");

	@io.vertigo.dynamo.domain.stereotype.AssociationNN(
			name = "ANN_FAM_CAR_LOCATION",
			tableName = "FAM_CAR_LOCATION",
			dtDefinitionA = "DT_FAMILLE",
			dtDefinitionB = "DT_CAR",
			navigabilityA = false,
			navigabilityB = true,
			roleA = "Famille",
			roleB = "VoituresLocation",
			labelA = "Famille",
			labelB = "Voitures de location")
	private final ListVAccessor<Car> voituresLocationAccessor = new ListVAccessor<>(this, "ANN_FAM_CAR_LOCATION", "VoituresLocation");

	/** {@inheritDoc} */
	@Override
	public UID<Famille> getUID() {
		return UID.of(this);
	}

	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'identifiant de la famille'.
	 * @return Long famId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_ID", type = "ID", required = true, label = "identifiant de la famille")
	public Long getFamId() {
		return famId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'identifiant de la famille'.
	 * @param famId Long <b>Obligatoire</b>
	 */
	public void setFamId(final Long famId) {
		this.famId = famId;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Libelle'.
	 * @return String libelle
	 */
	@Field(domain = "DO_STRING", label = "Libelle")
	public String getLibelle() {
		return libelle;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Libelle'.
	 * @param libelle String
	 */
	public void setLibelle(final String libelle) {
		this.libelle = libelle;
	}

	/**
	 * Champ : COMPUTED.
	 * Récupère la valeur de la propriété calculée 'Libelle'.
	 * @return String description
	 */
	@Field(domain = "DO_LIBELLE_LONG", type = "COMPUTED", persistent = false, label = "Libelle")
	public String getDescription() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getLibelle());
		builder.append('[');
		builder.append(getFamId());
		builder.append(']');
		return builder.toString();
	}

	/**
	 * Association : Voitures de la famille.
	 * @return l'accesseur vers la propriété 'Voitures de la famille'
	 */
	public ListVAccessor<Car> voituresFamille() {
		return voituresFamilleAccessor;
	}

	/**
	 * Association : Voitures de la famille.
	 * @return io.vertigo.dynamo.domain.model.DtList<Car>
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.model.DtList<Car> getVoituresFamilleList() {
		// we keep the lazyness
		if (!voituresFamilleAccessor.isLoaded()) {
			voituresFamilleAccessor.load();
		}
		return voituresFamilleAccessor.get();
	}

	/**
	 * Association URI: Voitures de la famille.
	 * @return URI de l'association
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.metamodel.association.DtListURIForSimpleAssociation getVoituresFamilleDtListURI() {
		return (io.vertigo.dynamo.domain.metamodel.association.DtListURIForSimpleAssociation) voituresFamilleAccessor.getDtListURI();
	}

	/**
	 * Association : Voitures de location.
	 * @return l'accesseur vers la propriété 'Voitures de location'
	 */
	public ListVAccessor<Car> voituresLocation() {
		return voituresLocationAccessor;
	}

	/**
	 * Association : Voitures de location.
	 * @return io.vertigo.dynamo.domain.model.DtList<Car>
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.model.DtList<Car> getVoituresLocationList() {
		// we keep the lazyness
		if (!voituresLocationAccessor.isLoaded()) {
			voituresLocationAccessor.load();
		}
		return voituresLocationAccessor.get();
	}

	/**
	 * Association URI: Voitures de location.
	 * @return URI de l'association
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.metamodel.association.DtListURIForNNAssociation getVoituresLocationDtListURI() {
		return (io.vertigo.dynamo.domain.metamodel.association.DtListURIForNNAssociation) voituresLocationAccessor.getDtListURI();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
