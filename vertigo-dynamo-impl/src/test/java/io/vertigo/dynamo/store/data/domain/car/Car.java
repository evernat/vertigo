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
package io.vertigo.dynamo.store.data.domain.car;

import io.vertigo.dynamo.domain.model.Entity;
import io.vertigo.dynamo.domain.model.EnumVAccessor;
import io.vertigo.dynamo.domain.model.ListVAccessor;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.dynamo.domain.model.VAccessor;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.dynamo.store.data.domain.famille.Famille;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class Car implements Entity {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String manufacturer;
	private String model;
	private String description;
	private Integer year;
	private Integer kilo;
	private Integer price;
	private java.math.BigDecimal consommation;

	@io.vertigo.dynamo.domain.stereotype.Association(
			name = "A_MTY_CAR",
			fkFieldName = "MTY_CD",
			primaryDtDefinitionName = "DT_MOTOR_TYPE",
			primaryIsNavigable = true,
			primaryRole = "MotorType",
			primaryLabel = "Motor type",
			primaryMultiplicity = "0..1",
			foreignDtDefinitionName = "DT_CAR",
			foreignIsNavigable = false,
			foreignRole = "Car",
			foreignLabel = "Car",
			foreignMultiplicity = "0..*")
	private final EnumVAccessor<MotorType, MotorTypeEnum> mtyCdAccessor = new EnumVAccessor<>(MotorType.class, "MotorType", MotorTypeEnum.class);

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
	private final VAccessor<Famille> famIdAccessor = new VAccessor<>(Famille.class, "Famille");

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
	private final ListVAccessor<Famille> familleAccessor = new ListVAccessor<>(this, "ANN_FAM_CAR_LOCATION", "Famille");

	/** {@inheritDoc} */
	@Override
	public UID<Car> getUID() {
		return UID.of(this);
	}

	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'identifiant de la voiture'.
	 * @return Long id <b>Obligatoire</b>
	 */
	@Field(domain = "DO_ID", type = "ID", required = true, label = "identifiant de la voiture")
	public Long getId() {
		return id;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'identifiant de la voiture'.
	 * @param id Long <b>Obligatoire</b>
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Constructeur'.
	 * @return String manufacturer <b>Obligatoire</b>
	 */
	@Field(domain = "DO_KEYWORD", required = true, label = "Constructeur")
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Constructeur'.
	 * @param manufacturer String <b>Obligatoire</b>
	 */
	public void setManufacturer(final String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'ModÃ¨le'.
	 * @return String model <b>Obligatoire</b>
	 */
	@Field(domain = "DO_FULL_TEXT", required = true, label = "ModÃ¨le")
	public String getModel() {
		return model;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'ModÃ¨le'.
	 * @param model String <b>Obligatoire</b>
	 */
	public void setModel(final String model) {
		this.model = model;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Descriptif'.
	 * @return String description <b>Obligatoire</b>
	 */
	@Field(domain = "DO_FULL_TEXT", required = true, label = "Descriptif")
	public String getDescription() {
		return description;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Descriptif'.
	 * @param description String <b>Obligatoire</b>
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'AnnÃ©e'.
	 * @return Integer year <b>Obligatoire</b>
	 */
	@Field(domain = "DO_INTEGER", required = true, label = "AnnÃ©e")
	public Integer getYear() {
		return year;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'AnnÃ©e'.
	 * @param year Integer <b>Obligatoire</b>
	 */
	public void setYear(final Integer year) {
		this.year = year;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'KilomÃ©trage'.
	 * @return Integer kilo <b>Obligatoire</b>
	 */
	@Field(domain = "DO_INTEGER", required = true, label = "KilomÃ©trage")
	public Integer getKilo() {
		return kilo;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'KilomÃ©trage'.
	 * @param kilo Integer <b>Obligatoire</b>
	 */
	public void setKilo(final Integer kilo) {
		this.kilo = kilo;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Prix'.
	 * @return Integer price <b>Obligatoire</b>
	 */
	@Field(domain = "DO_INTEGER", required = true, label = "Prix")
	public Integer getPrice() {
		return price;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Prix'.
	 * @param price Integer <b>Obligatoire</b>
	 */
	public void setPrice(final Integer price) {
		this.price = price;
	}

	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Consommation'.
	 * @return BigDecimal consommation <b>Obligatoire</b>
	 */
	@Field(domain = "DO_CONSO", required = true, label = "Consommation")
	public java.math.BigDecimal getConsommation() {
		return consommation;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Consommation'.
	 * @param consommation BigDecimal <b>Obligatoire</b>
	 */
	public void setConsommation(final java.math.BigDecimal consommation) {
		this.consommation = consommation;
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Motor type'.
	 * @return String mtyCd
	 */
	@Field(domain = "DO_STRING", type = "FOREIGN_KEY", label = "Motor type")
	public String getMtyCd() {
		return (String) mtyCdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Motor type'.
	 * @param mtyCd String
	 */
	public void setMtyCd(final String mtyCd) {
		mtyCdAccessor.setId(mtyCd);
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Famille'.
	 * @return Long famId <b>Obligatoire</b>
	 */
	@Field(domain = "DO_ID", type = "FOREIGN_KEY", required = true, label = "Famille")
	public Long getFamId() {
		return (Long) famIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Famille'.
	 * @param famId Long <b>Obligatoire</b>
	 */
	public void setFamId(final Long famId) {
		famIdAccessor.setId(famId);
	}

	/**
	 * Association : Famille.
	 * @return l'accesseur vers la propriété 'Famille'
	 */
	public VAccessor<Famille> famille() {
		return famIdAccessor;
	}

	/**
	 * Association : Motor type.
	 * @return l'accesseur vers la propriété 'Motor type'
	 */
	public EnumVAccessor<MotorType, MotorTypeEnum> motorType() {
		return mtyCdAccessor;
	}

	@Deprecated
	public MotorType getMotorType() {
		// we keep the lazyness
		if (!mtyCdAccessor.isLoaded()) {
			mtyCdAccessor.load();
		}
		return mtyCdAccessor.get();
	}

	/**
	 * Retourne l'UID: Motor type.
	 * @return UID de l'association
	 */
	@Deprecated
	public io.vertigo.dynamo.domain.model.UID<MotorType> getMotorTypeURI() {
		return mtyCdAccessor.getUID();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
