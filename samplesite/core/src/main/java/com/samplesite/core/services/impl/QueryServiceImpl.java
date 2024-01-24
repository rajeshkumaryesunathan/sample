package com.samplesite.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.samplesite.core.constants.Constants;
import com.samplesite.core.services.QueryService;
import com.samplesite.core.utilities.ResourceUtils;

/**
 * The Class QueryServiceImpl.
 */
@Component(service = { QueryService.class })
public class QueryServiceImpl implements QueryService {

	/** The resource resolver factory. */
	@Reference
	ResourceResolverFactory rrf;

	@Reference
	QueryBuilder queryBuilder;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(QueryServiceImpl.class);

	/**
	 * Gets the component instances.
	 *
	 * @param basePath     the base path
	 * @param resourceType the resource type
	 * @return the component instances
	 */
	@Override
	public List<String> getComponentInstances(String basePath, String resourceType) {
		List<String> resultList = new ArrayList<String>();
		ResourceResolver serviceResolver = ResourceUtils.getServiceResolver(rrf, Constants.QUERY_SUBSERVICE);
		List<Hit> hits = null;
		if (serviceResolver != null) {
			hits = getQueryResults(basePath, resourceType, serviceResolver.adaptTo(Session.class));
		}
		if (null != hits) {
			LOG.debug("Results count -> " + hits.size());
			for (Hit hit : hits) {
				try {
					String path = hit.getPath();
					resultList.add(path);
				} catch (RepositoryException e) {
					LOG.error("RepositoryException while trying to read query result", e.getMessage());
				}
			}
		}
		return resultList;
	}

	/**
	 * Executes the query and returns the query result.
	 *
	 * @param basePath        the base path
	 * @param resourceType    the resource type
	 * @param serviceResolver the service resolver
	 * @return the query result
	 */
	private List<Hit> getQueryResults(String basePath, String resourceType, Session session) {
		Map<String, String> predicateMap = new HashMap<String, String>();
		predicateMap.put(Constants.PATH, basePath);
		predicateMap.put(Constants.PROPERTY, Constants.SLING_RESOURCE_TYPE);
		predicateMap.put(Constants.PROPERTY_VALUE, resourceType);
		predicateMap.put(Constants.QUERY_LIMIT, Constants.QUERY_LIMIT_VALUE);
		Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
		SearchResult result = query.getResult();
		return result.getHits();
	}
}
