package com.samplesite.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samplesite.core.constants.Constants;
import com.samplesite.core.services.QueryService;

/**
 * The Class ComponentInstancesServlet. Servlet to retrieve the instances of the
 * component. Accepts path and resourceType as input parameters and queries
 * against the path for the resourceType and returns the result
 */
@Component(service = { Servlet.class })
@SlingServletPaths(value = "/services/samplesite/componentinstances")
@ServiceDescription("Component Instances Servlet")
public class ComponentInstancesServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	/** The query service. */
	@Reference
	QueryService queryService;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ComponentInstancesServlet.class);

	/**
	 * Do get.
	 *
	 * @param req  the request
	 * @param resp the response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String basePath = req.getParameter(Constants.PATH);
		String resourceType = req.getParameter(Constants.RESOURCE_TYPE);
		if (StringUtils.isNotBlank(resourceType) && StringUtils.isNotBlank(basePath)) {
			LOG.debug("requested path -> " + basePath + " requested component -> " + resourceType);
			List<String> resultList = queryService.getComponentInstances(basePath, resourceType);
			responseMap.put("result", resultList);
		} else {
			responseMap.put("error", "Missing resourceType and/or path mandatory parameter(s)");
		}
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = resp.getWriter();
			writer.write(new ObjectMapper().writeValueAsString(responseMap));
		} catch (IOException e) {
			LOG.debug("IOException while returning the response ", e.getMessage());
		}
	}
}
