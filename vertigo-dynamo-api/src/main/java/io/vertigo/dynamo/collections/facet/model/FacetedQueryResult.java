package io.vertigo.dynamo.collections.facet.model;

import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.kernel.lang.Assertion;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Résultat de la recherche.
 * Tout résultat est facetté. 
 * Eventuellement il n'y a aucune facette. 
 * @author pchretien, dchallas
 * @param <R> Type de l'objet resultant de la recherche
 * @param <S> Type de l'objet source
 */
public final class FacetedQueryResult<R extends DtObject, S> implements Serializable {
	private static final long serialVersionUID = 1248453191954177054L;

	private final FacetedQuery query;
	private final DtList<R> dtc;
	private final List<Facet> facets;
	private final Map<R, Map<DtField, String>> highlights;
	private final long count;
	private final S source;

	/**
	 * Constructeur.
	 * @param query Requète 
	 * @param count  Nombre total de résultats
	 * @param dtc DTC résultat, éventuellement tronquée à n (ex 500) si trop d'éléments.
	 * @param facets Liste des facettes. (Peut être vide jamais null)
	 * @param highlights Liste des extraits avec mise en valeur par objet et par champs
	 * @param source Object source permettant rerentrer dans le mechanisme de filtrage
	 */
	public FacetedQueryResult(final FacetedQuery query, final long count, final DtList<R> dtc, final List<Facet> facets, final Map<R, Map<DtField, String>> highlights, final S source) {
		Assertion.checkNotNull(query);
		Assertion.checkNotNull(dtc);
		Assertion.checkNotNull(facets);
		Assertion.checkNotNull(source);
		Assertion.checkNotNull(highlights);
		//---------------------------------------------------------------------
		this.query = query;
		this.count = count;
		this.dtc = dtc;
		this.facets = facets;
		this.highlights = highlights;
		this.source = source;
	}

	/**
	 * @return Nombre total de résultats
	 */
	public long getCount() {
		return count;
	}

	/**
	 * Rappel de la requête initiale.
	 * @return Requète 
	 */
	public FacetedQuery getFacetedQuery() {
		return query;
	}

	/**
	 * @return DTC résultat, éventuellement tronquée à n (ex 500) si trop d'éléments.
	 */
	public DtList<R> getDtList() {
		return dtc;
	}

	/**
	 * @return Liste des facettes. (Peut être vide jamais null)
	 */
	public List<Facet> getFacets() {
		return facets;
	}

	/**
	 * @return Extrait avec mise en valeur par champs. (Peut être vide jamais null)
	 */
	public Map<DtField, String> getHighlights(final R document) {
		final Map<DtField, String> documentHightlights = highlights.get(document);
		return documentHightlights != null ? documentHightlights : Collections.<DtField, String> emptyMap();
	}

	/**
	 * @return Object source permettant réentrer dans le mécanisme de filtrage.
	 */
	public S getSource() {
		return source;
	}
}
