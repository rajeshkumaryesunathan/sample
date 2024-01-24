package com.samplesite.core.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.samplesite.core.constants.Constants;
import com.samplesite.core.services.QueryService;
import com.samplesite.core.utilities.ResourceUtils;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * The Class QueryServiceImplTest.
 */
@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class QueryServiceImplTest {

	/** The resource utils. */
	@Mock
	ResourceUtils resourceUtils;

	/** The rrf. */
	@Mock
	ResourceResolverFactory rrf;

	/** The query builder. */
	@Mock
	QueryBuilder queryBuilder;

	/** The query. */
	@Mock
	Query query;

	/** The result. */
	@Mock
	SearchResult result;

	/** The query service. */
	@InjectMocks
	QueryService queryService = new QueryServiceImpl();

	/** The aem context. */
	private final AemContext aemContext = new AemContext();

	/**
	 * Gets the component instances.
	 *
	 * @return the component instances
	 */
	@Test
	void getComponentInstances() throws Exception {
		lenient().when(ResourceUtils.getServiceResolver(rrf, Constants.QUERY_SUBSERVICE))
				.thenReturn(aemContext.resourceResolver());
		Map<String, String> predicateMap = new HashMap<String, String>();
		predicateMap.put(Constants.PATH, "/content");
		predicateMap.put(Constants.PROPERTY, Constants.SLING_RESOURCE_TYPE);
		predicateMap.put(Constants.PROPERTY_VALUE, "samplesite/components/mycomponent");
		predicateMap.put(Constants.QUERY_LIMIT, Constants.QUERY_LIMIT_VALUE);
		lenient().when(queryBuilder.createQuery(any(), any())).thenReturn(query);
		lenient().when(query.getResult()).thenReturn(result);
		List<Hit> listHits = new ArrayList<>();
		Hit hit = mock(Hit.class);
		listHits.add(hit);
		lenient().when(result.getHits()).thenReturn(listHits);
		lenient().when(hit.getPath()).thenReturn("/content/samplesite/mycomponent");

		List<String> response = queryService.getComponentInstances("/content", "samplesite/components/mycomponent");

		assertEquals("/content/samplesite/mycomponent", response.get(0));
	}

	/**
	 * Gets the component instances no result.
	 *
	 * @return the component instances
	 * @throws Exception the exception
	 */
	@Test
	void getComponentInstances_NoResult() throws Exception {
		when(ResourceUtils.getServiceResolver(rrf, Constants.QUERY_SUBSERVICE))
				.thenReturn(aemContext.resourceResolver());
		Map<String, String> predicateMap = new HashMap<String, String>();
		predicateMap.put(Constants.PATH, "/content");
		predicateMap.put(Constants.PROPERTY, Constants.SLING_RESOURCE_TYPE);
		predicateMap.put(Constants.PROPERTY_VALUE, "samplesite/components/mycomponent");
		predicateMap.put(Constants.QUERY_LIMIT, Constants.QUERY_LIMIT_VALUE);
		lenient().when(queryBuilder.createQuery(any(), any())).thenReturn(query);
		lenient().when(query.getResult()).thenReturn(result);
		lenient().when(result.getHits()).thenReturn(null);

		List<String> response = queryService.getComponentInstances("/content", "samplesite/components/mycomponent");

		assertTrue(response.isEmpty());
	}

	/**
	 * Gets the component instances no resource resolver.
	 *
	 * @return the component instances
	 * @throws Exception the exception
	 */
	@Test
	void getComponentInstances_NoResourceResolver() throws Exception {
		lenient().when(ResourceUtils.getServiceResolver(rrf, Constants.QUERY_SUBSERVICE)).thenReturn(null);
		List<String> response = queryService.getComponentInstances("/content", "samplesite/components/mycomponent");

		assertTrue(response.isEmpty());
	}
}
