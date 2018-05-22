package com.msa.api.regcovery.endpoint;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * The interface Enable regcovery dashboard.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({JerseyAutoConfiguration.class, JerseyServletRegistrationBean.class})
public @interface EnableRegcoveryDashboard {
}
