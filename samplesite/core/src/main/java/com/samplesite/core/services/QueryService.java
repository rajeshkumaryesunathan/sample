package com.samplesite.core.services;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface QueryService {

	List<String> getComponentInstances(String basePath, String resourceType);
}
