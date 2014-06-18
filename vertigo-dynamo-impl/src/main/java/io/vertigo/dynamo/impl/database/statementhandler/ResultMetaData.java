package io.vertigo.dynamo.impl.database.statementhandler;

import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.model.DtObject;

/**
 * Type de sortie du prepareStatement.
 * Permet de créer des types de sortie dynamiques.
 * 
 * @author  pchretien
 */
interface ResultMetaData {
	DtObject createDtObject();

	/***
	 * Récupération de la DtDefinition du type de retour du PrepareStatement.
	 * @return DtDefinition du type de retour du PrepareStatement
	 */
	DtDefinition getDtDefinition();

	/***
	 * @return Si le type de sortie est un DTO.
	 */
	boolean isDtObject();
}
