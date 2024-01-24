package com.samplesite.core.utilities;

import java.util.Collections;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ResourceUtils.
 */
public class ResourceUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceUtils.class);

	/**
	 * Gets the service resolver.
	 *
	 * @param resourceResolverFactory the resource resolver factory
	 * @param subService              the sub service
	 * @return the service resolver
	 */
	public static ResourceResolver getServiceResolver(ResourceResolverFactory resourceResolverFactory,
			String subService) {
		ResourceResolver serviceResolver = null;
		if (resourceResolverFactory != null) {
			final Map<String, Object> authInfo = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
					subService);
			try {
				serviceResolver = resourceResolverFactory.getServiceResourceResolver(authInfo);
			} catch (LoginException e) {
				LOG.error("LoginException while trying to retrieve service resolver", e.getMessage());
			}
		}
		return serviceResolver;
	}
}
