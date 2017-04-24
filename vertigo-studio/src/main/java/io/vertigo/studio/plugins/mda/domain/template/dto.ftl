package ${dtDefinition.packageName};

import ${dtDefinition.stereotypePackageName};
<#if dtDefinition.entity || dtDefinition.fragment>
import io.vertigo.dynamo.domain.model.URI;
</#if>
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;

/**
 * Attention cette classe est générée automatiquement !
 * Objet de données ${dtDefinition.classSimpleName}
 */
<#list annotations(dtDefinition.dtDefinition) as annotation>
${annotation}
</#list>
public final class ${dtDefinition.classSimpleName} implements ${dtDefinition.stereotypeInterfaceName} {

	/** SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	<#list dtDefinition.dtFields as dtField>
	private ${dtField.javaType} ${dtField.nameLowerCase?uncap_first};
	</#list>
	<#list dtDefinition.associations as association>
	<#if association.navigable>
		<#if association.multiple>
	private io.vertigo.dynamo.domain.model.DtList<${association.returnType}> ${association.role?uncap_first};
		<#else>
	private ${association.returnType} ${association.role?uncap_first};
		</#if>
	</#if>
	</#list>
	<#if dtDefinition.entity>

	/** {@inheritDoc} */
	<#list annotations("URI") as annotation>
	${annotation}
	</#list>
	@Override
	public URI<${dtDefinition.classSimpleName}> getURI() {
		return DtObjectUtil.createURI(this);
	}
	</#if>
	<#if dtDefinition.fragment>

	/** {@inheritDoc} */
	<#list annotations("URI") as annotation>
	${annotation}
	</#list>
	@Override
	public URI<${dtDefinition.entityClassSimpleName}> getEntityURI() {
		return DtObjectUtil.createEntityURI(this); 
	}
	</#if>

	<#list dtDefinition.dtFields as dtField>
	/**
	 * Champ : ${dtField.type}.
	 * Récupère la valeur de la propriété '${dtField.display}'.
	 * @return ${dtField.javaType} ${dtField.nameLowerCase?uncap_first}<#if dtField.required> <b>Obligatoire</b></#if>
	 */
	<#list annotations(dtField.dtField, dtField.dtDefinition) as annotation>
	${annotation}
	</#list>
	public ${dtField.javaType} get${dtField.nameLowerCase}() {
		return ${dtField.nameLowerCase?uncap_first};
	}

	/**
	 * Champ : ${dtField.type}.
	 * Définit la valeur de la propriété '${dtField.display}'.
	 * @param ${dtField.nameLowerCase?uncap_first} ${dtField.javaType}<#if dtField.required> <b>Obligatoire</b></#if>
	 */
	public void set${dtField.nameLowerCase}(final ${dtField.javaType} ${dtField.nameLowerCase?uncap_first}) {
		this.${dtField.nameLowerCase?uncap_first} = ${dtField.nameLowerCase?uncap_first};
	}

	</#list>
	<#list dtDefinition.dtComputedFields as dtField>
	/**
	 * Champ : ${dtField.type}.
	 * Récupère la valeur de la propriété calculée '${dtField.display}'.
	 * @return ${dtField.javaType} ${dtField.nameLowerCase?uncap_first}<#if dtField.required> <b>Obligatoire</b></#if>
	 */
	<#list annotations(dtField.dtField, dtField.dtDefinition) as annotation>
	${annotation}
	</#list>
	public ${dtField.javaType} get${dtField.nameLowerCase}() {
		${dtField.javaCode}
	}

	</#list>
	<#if dtDefinition.associations?has_content>
	<#list dtDefinition.associations as association>
	<#if association.navigable>
	/**
	 * Association : ${association.label}.
	 * @return <#if association.multiple>io.vertigo.dynamo.domain.model.DtList<${association.returnType}><#else>${association.returnType}</#if>
	 */
	<#if association.multiple>
	public io.vertigo.dynamo.domain.model.DtList<${association.returnType}> get${association.role?cap_first}List() {
//		return this.<${association.returnType}> getList(get${association.role?cap_first}ListURI());
		// On doit avoir une clé primaire renseignée. Si ce n'est pas le cas, on renvoie une liste vide
		if (io.vertigo.dynamo.domain.util.DtObjectUtil.getId(this) == null) {
			return new io.vertigo.dynamo.domain.model.DtList<>(${association.returnType}.class);
		}
		final io.vertigo.dynamo.domain.model.DtListURI fkDtListURI = get${association.role?cap_first}DtListURI();
		io.vertigo.lang.Assertion.checkNotNull(fkDtListURI);
		//---------------------------------------------------------------------
		//On est toujours dans un mode lazy.
		if (${association.role?uncap_first} == null) {
			${association.role?uncap_first} = io.vertigo.app.Home.getApp().getComponentSpace().resolve(io.vertigo.dynamo.store.StoreManager.class).getDataStore().findAll(fkDtListURI);
		}
		return ${association.role?uncap_first};
	}

	/**
	 * Association URI: ${association.label}.
	 * @return URI de l'association
	 */
	<#list annotations(association.definition) as annotation>
	${annotation}
	</#list>
	public io.vertigo.dynamo.domain.metamodel.association.DtListURIFor<#if association.simple>Simple<#else>NN</#if>Association get${association.role?cap_first}DtListURI() {
		return io.vertigo.dynamo.domain.util.DtObjectUtil.createDtListURIFor<#if association.simple>Simple<#else>NN</#if>Association(this, "${association.urn}", "${association.role}");
	}

	<#else>
	public ${association.returnType} get${association.role?cap_first}() {
		final io.vertigo.dynamo.domain.model.URI<${association.returnType}> fkURI = get${association.role?cap_first}URI();
		if (fkURI == null) {
			return null;
		}
		//On est toujours dans un mode lazy. On s'assure cependant que l'objet associé n'a pas changé
		if (${association.role?uncap_first} == null || !fkURI.equals(${association.role?uncap_first}.getURI())) {
			${association.role?uncap_first} = io.vertigo.app.Home.getApp().getComponentSpace().resolve(io.vertigo.dynamo.store.StoreManager.class).getDataStore().readOne(fkURI);
		}
		return ${association.role?uncap_first};
	}

	/**
	 * Retourne l'URI: ${association.label}.
	 * @return URI de l'association
	 */
	<#list annotations(association.definition) as annotation>
	${annotation}
	</#list>
	public io.vertigo.dynamo.domain.model.URI<${association.returnType}> get${association.role?cap_first}URI() {
		return io.vertigo.dynamo.domain.util.DtObjectUtil.createURI(this, "${association.urn}", ${association.returnType}.class);
	}

	</#if>
	<#else>
	// Association : ${association.label} non navigable
	</#if>
	</#list>
	<#else>
	//Aucune Association déclarée
	</#if>

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}