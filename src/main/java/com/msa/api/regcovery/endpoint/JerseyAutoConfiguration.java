package com.msa.api.regcovery.endpoint;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * The type Jersey configuration.
 */
@Configuration
@ConditionalOnClass(ResourceConfig.class)
public class JerseyAutoConfiguration extends ResourceConfig {

    /**
     * Create a new resource configuration without any custom properties or
     * resource and provider classes.
     */
    public JerseyAutoConfiguration() {
        register(RequestContextFilter.class);
        register(ApiResource.class);
        //配置restful package.
        //packages(JerseyAutoConfiguration.class.getPackage().getName());
    }

}
