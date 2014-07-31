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
package io.vertigo.quarto.publisher.model;

import io.vertigo.kernel.lang.Assertion;
import io.vertigo.quarto.publisher.metamodel.PublisherDataDefinition;

/**
 * Données à fusionner.
 *
 * @author npiedeloup
 * @version $Id: PublisherData.java,v 1.3 2013/10/22 12:07:11 pchretien Exp $
 */
public final class PublisherData {
	private final PublisherDataDefinition publisherDataDefinition;
	private final PublisherNode root;

	/**
	 * Constructeur.
	 * @param dataDefinition Definition des données de publication
	 */
	public PublisherData(final PublisherDataDefinition dataDefinition) {
		Assertion.checkNotNull(dataDefinition);
		//---------------------------------------------------------------------
		publisherDataDefinition = dataDefinition;
		root = new PublisherNode(dataDefinition.getRootNodeDefinition());
	}

	/**
	 * @return Définition des données.
	 */
	public PublisherDataDefinition getDefinition() {
		return publisherDataDefinition;
	}

	/**
	 * @return Noeud racine
	 */
	public PublisherNode getRootNode() {
		return root;
	}
}
