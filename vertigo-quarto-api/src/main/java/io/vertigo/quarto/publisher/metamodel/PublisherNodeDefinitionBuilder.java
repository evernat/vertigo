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
package io.vertigo.quarto.publisher.metamodel;

import io.vertigo.kernel.lang.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder de la définition d'un modèle de noeud d'édition.
 * Un noeud d'edition compose l'arbre des des données d'une édition.
 *
 * @author npiedeloup, pchretien
 * @version $Id: PublisherNodeDefinitionBuilder.java,v 1.3 2014/02/27 10:32:26 pchretien Exp $
 */
public final class PublisherNodeDefinitionBuilder implements Builder<PublisherNodeDefinition> {
	private final List<PublisherField> publisherFields = new ArrayList<>();

	/**
	 * Ajoute un champ booléen.
	 * @param fieldName Nom du champ
	 */
	public PublisherNodeDefinitionBuilder withBooleanField(final String fieldName) {
		return registerField(fieldName, PublisherFieldType.Boolean, null);
	}

	/**
	 * Ajoute un champ String.
	 * @param fieldName Nom du champ
	 */
	public PublisherNodeDefinitionBuilder withStringField(final String fieldName) {
		return registerField(fieldName, PublisherFieldType.String, null);
	}

	/**
	 * Ajoute un champ Image.
	 * @param fieldName Nom du champ
	 */
	public PublisherNodeDefinitionBuilder withImageField(final String fieldName) {
		return registerField(fieldName, PublisherFieldType.Image, null);
	}

	/**
	 * Ajoute un champ Data (autre noeud).
	 * @param fieldName Nom du champ
	 * @param nodeDefinition Définition du noeud
	 */
	public PublisherNodeDefinitionBuilder withNodeField(final String fieldName, final PublisherNodeDefinition nodeDefinition) {
		return registerField(fieldName, PublisherFieldType.Node, nodeDefinition);
	}

	/**
	 * Ajoute un champ List (liste composée de noeud).
	 * @param fieldName Nom du champ
	 * @param nodeDefinition Définition des éléments de la liste
	 */
	public PublisherNodeDefinitionBuilder withListField(final String fieldName, final PublisherNodeDefinition nodeDefinition) {
		return registerField(fieldName, PublisherFieldType.List, nodeDefinition);
	}

	private PublisherNodeDefinitionBuilder registerField(final String fieldName, final PublisherFieldType fieldType, final PublisherNodeDefinition nodeDefinition) {
		publisherFields.add(new PublisherField(fieldName, fieldType, nodeDefinition));
		return this;
	}

	/**
	 * 
	 * @return PublisherDataNodeDefinition
	 */
	public PublisherNodeDefinition build() {
		return new PublisherNodeDefinition(publisherFields);
	}
}
