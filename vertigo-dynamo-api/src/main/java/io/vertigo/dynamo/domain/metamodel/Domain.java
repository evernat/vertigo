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
package io.vertigo.dynamo.domain.metamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.vertigo.app.Home;
import io.vertigo.core.spaces.definiton.Definition;
import io.vertigo.core.spaces.definiton.DefinitionPrefix;
import io.vertigo.core.spaces.definiton.DefinitionReference;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.VSystemException;

/**
 * A domain exists to enrich the primitive datatypes, giving them super powers.
 *
 * A domain has
 *  - a validator (executed by a list of constraints)
 *  - a formatter
 *
 * A domain is a shared object ; by nature it is immutable.
 *
 * A domain is a definition, its prefix is "DO_"
 *
 * Examples :
 *  A mail is not defined by a simple "String", but by a domain called 'Mail'.
 *  Weights, currencies, codes, labels...
 *
 *  An application is built with some dozens of domains.
 *
 * @author pchretien
 */
@DefinitionPrefix("DO")
public final class Domain implements Definition {
	private final String name;
	private final DataType dataType;

	/** Formatter. */
	private final DefinitionReference<FormatterDefinition> formatterDefinitionRef;

	/** Validator composed by a list of constraints. */
	private final List<DefinitionReference<ConstraintDefinition>> constraintDefinitionRefs;

	/** List of property-value tuples */
	private final Properties properties;

	/**
	 * Name of the DtDefinition (for the DtObject or DtList)
	 */
	private final String dtDefinitionName;

	/**
	 * Constructorr.
	 * @param name the name of the domain
	 * @param dataType the dataType lof the domain
	 */
	public Domain(final String name, final DataType dataType) {
		this(name, dataType, null, Collections.<ConstraintDefinition> emptyList(), new PropertiesBuilder().build());
	}

	/**
	 * Constructor.
	 * @param name the name of the domain
	 * @param dataType the type of the domain
	 * @param formatterDefinition the formatter
	 * @param constraintDefinitions the list of constraints
	 * @param properties List of property-value tuples
	 */
	public Domain(final String name, final DataType dataType, final FormatterDefinition formatterDefinition, final List<ConstraintDefinition> constraintDefinitions, final Properties properties) {
		Assertion.checkArgNotEmpty(name);
		//formatterDefinition can be null
		Assertion.checkNotNull(constraintDefinitions);
		Assertion.checkNotNull(properties);
		//-----
		this.name = name;
		this.dataType = dataType;
		formatterDefinitionRef = formatterDefinition == null ? null : new DefinitionReference<>(formatterDefinition);
		//---Constraints
		final List<DefinitionReference<ConstraintDefinition>> myConstraintDefinitionRefs = new ArrayList<>();
		for (final ConstraintDefinition constraintDefinition : constraintDefinitions) {
			myConstraintDefinitionRefs.add(new DefinitionReference<>(constraintDefinition));
		}
		constraintDefinitionRefs = Collections.unmodifiableList(myConstraintDefinitionRefs);
		//---Properties
		this.properties = buildProperties(constraintDefinitions, properties);

		//---FK
		if (this.properties.getValue(DtProperty.TYPE) != null) {
			Assertion.checkArgument(!getDataType().isPrimitive(), "The type can only be used for DtObject or DtList");
			//-----
			dtDefinitionName = this.properties.getValue(DtProperty.TYPE);
		} else {
			dtDefinitionName = null;
		}
	}

	private static Properties buildProperties(final List<ConstraintDefinition> constraintDefinitions, final Properties inputProperties) {
		final PropertiesBuilder propertiesBuilder = new PropertiesBuilder();
		for (final Property property : inputProperties.getProperties()) {
			propertiesBuilder.addValue(property, inputProperties.getValue(property));
		}

		//Properties are inferred from constraints
		for (final ConstraintDefinition constraintDefinition : constraintDefinitions) {
			propertiesBuilder.addValue(constraintDefinition.getProperty(), constraintDefinition.getPropertyValue());
		}
		return propertiesBuilder.build();
	}

	/**
	 * Returns the dataType of the domain.
	 *
	 * @return the dataType.
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * Returns the formatter of the domain.
	 *
	 * @return the formatter.
	 */
	public FormatterDefinition getFormatter() {
		Assertion.checkNotNull(formatterDefinitionRef, "no formatter defined on {0}", this);
		//-----
		return formatterDefinitionRef.get();
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Chechs if the value is valid.
	 *
	 * @param value the value to check
	 * @throws ConstraintException if a constraint has failed
	 */
	public void checkValue(final Object value) throws ConstraintException {
		//1. We are checking the type .
		getDataType().checkValue(value);

		//2. we are checking all the constraints
		for (final DefinitionReference<ConstraintDefinition> constraintDefinitionRef : constraintDefinitionRefs) {
			//when a constraint fails, there is no validation
			if (!constraintDefinitionRef.get().checkConstraint(value)) {
				throw new ConstraintException(constraintDefinitionRef.get().getErrorMessage());
			}
		}
	}

	//==========================================================================
	//for these domains : DtList or DtObject
	//==========================================================================
	/**
	 * @return if there is a DtDefinition for this domain.
	 */
	public boolean hasDtDefinition() {
		return dtDefinitionName != null;
	}

	/**
	 * @returns the dtDefinition for the domains DtList or DtObject.
	 */
	public DtDefinition getDtDefinition() {
		if (dtDefinitionName == null) {
			//We are building an explicit error
			if (getDataType().isPrimitive()) {
				throw new VSystemException("the domain {0} is not a DtList/DtObject", getName());
			}
			throw new VSystemException("The domain is a dynamic DtList/DtObject, so there is no DtDefinition", getName());
		}
		return Home.getApp().getDefinitionSpace().resolve(dtDefinitionName, DtDefinition.class);
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return name;
	}
}
