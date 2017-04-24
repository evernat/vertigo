<#import "macro_ao.ftl" as lib>
package ${dao.packageName};

import javax.inject.Inject;
<#if dao.hasSearchBehavior()>
import java.util.List;
</#if>
<#if dao.options >
import java.util.Optional;
</#if>
<#if !dao.taskDefinitions.empty || dao.hasSearchBehavior() >
import io.vertigo.app.Home;
</#if>
<#if dao.hasSearchBehavior()>
import io.vertigo.core.component.di.injector.DIInjector;
import io.vertigo.dynamo.search.SearchManager;
import io.vertigo.dynamo.search.metamodel.SearchIndexDefinition;
import io.vertigo.dynamo.search.model.SearchQuery;
import io.vertigo.dynamo.search.model.SearchQueryBuilder;
import io.vertigo.dynamo.domain.model.DtListState;
import io.vertigo.dynamo.collections.ListFilter;
import io.vertigo.dynamo.collections.metamodel.FacetedQueryDefinition;
import io.vertigo.dynamo.collections.metamodel.ListFilterBuilder;
import io.vertigo.dynamo.collections.model.FacetedQueryResult;
import ${dao.indexDtClassCanonicalName};
</#if>
<#if !dao.taskDefinitions.empty >
import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.dynamo.task.model.Task;
import io.vertigo.dynamo.task.model.TaskBuilder;
</#if>
<#if dao.keyConcept>
import io.vertigo.dynamo.domain.model.URI;
</#if>
import io.vertigo.dynamo.impl.store.util.DAO;
import io.vertigo.dynamo.store.StoreManager;
import io.vertigo.dynamo.store.StoreServices;
import io.vertigo.dynamo.task.TaskManager;
import ${dao.dtClassCanonicalName};

/**
 * DAO : Accès à un object (DTO, DTC). 
 * ${dao.classSimpleName}
 */
public final class ${dao.classSimpleName} extends DAO<${dao.dtClassSimpleName}, ${dao.idFieldType}> implements StoreServices {
	<#if dao.keyConcept && dao.hasSearchBehavior()>
	private final SearchManager searchManager;
	private final VTransactionManager transactionManager;
	</#if>

	/**
	 * Contructeur.
	 * @param storeManager Manager de persistance
	 * @param taskManager Manager de Task
	 <#if dao.keyConcept && dao.hasSearchBehavior()>
	 * @param searchManager Search Manager
	 * @param transactionManager Transaction Manager
	 </#if>
	 */
	@Inject
	public ${dao.classSimpleName}(final StoreManager storeManager, final TaskManager taskManager<#if dao.keyConcept && dao.hasSearchBehavior()>, final SearchManager searchManager, final VTransactionManager transactionManager</#if>) {
		super(${dao.dtClassSimpleName}.class, storeManager, taskManager);
		<#if dao.keyConcept && dao.hasSearchBehavior()>
		this.searchManager = searchManager;
		this.transactionManager = transactionManager;
		</#if>
	}

	<#if dao.keyConcept>
	/**
	 * Indique que le keyConcept associé à cette uri va être modifié.
	 * Techniquement cela interdit les opérations d'ecriture en concurrence 
	 * et envoie un évenement de modification du keyConcept (à la fin de transaction eventuellement) 
	 * @param uri URI du keyConcept modifié
	 * @return KeyConcept à modifier
	 */
	 public ${dao.dtClassSimpleName} readOneForUpdate(final URI<${dao.dtClassSimpleName}> uri) {
		return dataStore.readOneForUpdate(uri);
	}

	/**
	 * Indique que le keyConcept associé à cet id va être modifié.
	 * Techniquement cela interdit les opérations d'ecriture en concurrence 
	 * et envoie un évenement de modification du keyConcept (à la fin de transaction eventuellement) 
	 * @param id Clé du keyConcept modifié
	 * @return KeyConcept à modifier
	 */
	 public ${dao.dtClassSimpleName} readOneForUpdate(final ${dao.idFieldType} id) {
		return readOneForUpdate(createDtObjectURI(id));
	}
	</#if>
	<#if dao.keyConcept && dao.hasSearchBehavior()>

	<#list dao.facetedQueryDefinitions as facetedQueryDefinition>
	/**
	 * Création d'une SearchQuery de type : ${facetedQueryDefinition.simpleName}.
	 * @param criteria Critères de recherche
	 * @param listFilters Liste des filtres à appliquer (notament les facettes sélectionnées)
	 * @return SearchQueryBuilder pour ce type de recherche
	 */
	public SearchQueryBuilder createSearchQueryBuilder${facetedQueryDefinition.simpleName}(final ${facetedQueryDefinition.criteriaClassCanonicalName} criteria, final List<ListFilter> listFilters) {
		final FacetedQueryDefinition facetedQueryDefinition = Home.getApp().getDefinitionSpace().resolve("${facetedQueryDefinition.urn}", FacetedQueryDefinition.class);
		final ListFilterBuilder<${facetedQueryDefinition.criteriaClassCanonicalName}> listFilterBuilder = DIInjector.newInstance(facetedQueryDefinition.getListFilterBuilderClass(), Home.getApp().getComponentSpace());
		final ListFilter criteriaListFilter = listFilterBuilder.withBuildQuery(facetedQueryDefinition.getListFilterBuilderQuery()).withCriteria(criteria).build();
		return new SearchQueryBuilder(criteriaListFilter).withFacetStrategy(facetedQueryDefinition, listFilters);
	}
	</#list>

	/**
	 * Récupération du résultat issu d'une requête.
	 * @param searchQuery critères initiaux
	 * @param listState Etat de la liste (tri et pagination)
	 * @return Résultat correspondant à la requête (de type ${dao.indexDtClassSimpleName}) 
	 */
	public FacetedQueryResult<${dao.indexDtClassSimpleName}, SearchQuery> loadList(final SearchQuery searchQuery, final DtListState listState) {
		final SearchIndexDefinition indexDefinition = searchManager.findIndexDefinitionByKeyConcept(${dao.dtClassSimpleName}.class);
		return searchManager.loadList(indexDefinition, searchQuery, listState);
	}
	
/**
	 * Mark an entity as dirty. Index of these elements will be reindexed if Tx commited.
	 * Reindexation isn't synchrone, strategy is dependant of plugin's parameters.
	 *
	 * @param entityUri Key concept's uri
	 */
	public void markAsDirty(final URI<${dao.dtClassSimpleName}> entityUri) {
		transactionManager.getCurrentTransaction().addAfterCompletion((final boolean txCommitted) -> {
			if (txCommitted) {// reindex only is tx successful
				searchManager.markAsDirty(entityUri);
			}
		});
	}
	
	/**
	 * Mark an entity as dirty. Index of these elements will be reindexed if Tx commited.
	 * Reindexation isn't synchrone, strategy is dependant of plugin's parameters.
	 * 
	 * @param entity Key concept
	 */
	public void markAsDirty(final ${dao.dtClassSimpleName} entity) {
		markAsDirty(DtObjectUtil.createURI(entity));
	}
	</#if>
	<#if !dao.taskDefinitions.empty>
	<@lib.generateBody dao.taskDefinitions/>  
	</#if>
}