package com.samplesite.core.utilities;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class ResourceUtilsTest {

	/** The rrf. */
	@Mock
	ResourceResolverFactory rrf;

	/** The aem context. */
	private final AemContext aemContext = new AemContext();

	/**
	 * Gets the service resolver.
	 *
	 * @return the service resolver
	 * @throws LoginException the login exception
	 */
	@Test
	void getServiceResolver() throws LoginException {
		when(rrf.getServiceResourceResolver(any())).thenReturn(aemContext.resourceResolver());
		assertNotNull(ResourceUtils.getServiceResolver(rrf, "subservice"));
	}

	/**
	 * Gets the service resolver null rrf.
	 *
	 * @return the service resolver null rrf
	 */
	@Test
	void getServiceResolver_NullRrf() {
		assertNull(ResourceUtils.getServiceResolver(null, "subservice"));
	}

}
