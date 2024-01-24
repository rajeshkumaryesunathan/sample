package com.samplesite.core.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.samplesite.core.constants.Constants;
import com.samplesite.core.services.QueryService;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class ComponentInstancesServletTest {

	/** The query service. */
	@Mock
	QueryService queryService;

//	Gson gson;

	/** The servlet. */
	@InjectMocks
	ComponentInstancesServlet servlet = new ComponentInstancesServlet();

	/** The aem context. */
	private final AemContext aemContext = new AemContext();

	/**
	 * Do get.
	 */
	@Test
	void doGet() {
		MockSlingHttpServletRequest mockRequest = aemContext.request();
		MockSlingHttpServletResponse mockResponse = aemContext.response();
		aemContext.registerService(QueryService.class, queryService);
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put(Constants.PATH, "/content");
		requestParams.put(Constants.RESOURCE_TYPE, "samplesite/components/mycomponent");
		aemContext.request().setParameterMap(requestParams);
		List<String> resp = new ArrayList<String>();
		resp.add("path1");
		resp.add("path2");
		when(queryService.getComponentInstances("/content", "samplesite/components/mycomponent")).thenReturn(resp);
		servlet.doGet(mockRequest, mockResponse);

		assertEquals("{\"result\":[\"path1\",\"path2\"]}", mockResponse.getOutputAsString());
	}

	/**
	 * Do get missing param.
	 */
	@Test
	void doGet_MissingParam() {
		MockSlingHttpServletRequest mockRequest = aemContext.request();
		MockSlingHttpServletResponse mockResponse = aemContext.response();
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put(Constants.RESOURCE_TYPE, "samplesite/components/mycomponent");
		aemContext.request().setParameterMap(requestParams);
		servlet.doGet(mockRequest, mockResponse);

		assertEquals("{\"error\":\"Missing resourceType and/or path mandatory parameter(s)\"}",
				mockResponse.getOutputAsString());
	}

	/**
	 * Do get missing param 1.
	 */
	@Test
	void doGet_MissingParam1() {
		MockSlingHttpServletRequest mockRequest = aemContext.request();
		MockSlingHttpServletResponse mockResponse = aemContext.response();
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put(Constants.PATH, "/content");
		aemContext.request().setParameterMap(requestParams);
		servlet.doGet(mockRequest, mockResponse);

		assertEquals("{\"error\":\"Missing resourceType and/or path mandatory parameter(s)\"}",
				mockResponse.getOutputAsString());
	}

	/**
	 * Do get missing params.
	 */
	@Test
	void doGet_MissingParams() {
		MockSlingHttpServletRequest mockRequest = aemContext.request();
		MockSlingHttpServletResponse mockResponse = aemContext.response();
		servlet.doGet(mockRequest, mockResponse);

		assertEquals("{\"error\":\"Missing resourceType and/or path mandatory parameter(s)\"}",
				mockResponse.getOutputAsString());
	}
}
