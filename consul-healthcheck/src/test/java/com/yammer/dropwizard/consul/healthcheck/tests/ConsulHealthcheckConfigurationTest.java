package com.yammer.dropwizard.consul.healthcheck.tests;

import com.google.common.io.Resources;
import com.yammer.dropwizard.consul.healthcheck.ConsulHealthcheckConfiguration;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.valuehandling.OptionalValidatedValueUnwrapper;
import org.hibernate.validator.HibernateValidator;
import org.junit.Test;

import javax.validation.Validation;
import java.io.File;
import java.net.URI;

import static org.fest.assertions.api.Assertions.assertThat;

public class ConsulHealthcheckConfigurationTest {
    @Test
    public void parseConfiguration() throws Exception {
        final ConsulHealthcheckConfiguration configuration =
                new ConfigurationFactory<>(
                        ConsulHealthcheckConfiguration.class,
                        Validation
                                .byProvider(HibernateValidator.class)
                                .configure()
                                .addValidatedValueHandler(new OptionalValidatedValueUnwrapper())
                                .buildValidatorFactory()
                        .getValidator(),
                        Jackson.newObjectMapper(),
                        "dw.")
                .build(new File(Resources.getResource("healthcheckConfiguration.yml").toURI()));
        assertThat(configuration.getApplicationName()).isEqualTo("test");
        assertThat(configuration.getCheckInterval()).isEqualTo(Duration.milliseconds(2500));
        assertThat(configuration.getClient().getConnectionTimeout()).isEqualTo(Duration.milliseconds(100));
        assertThat(configuration.getClient().getTimeout()).isEqualTo(Duration.milliseconds(200));
        assertThat(configuration.getClient().getUri()).isEqualTo(URI.create("http://localhost:8080"));
    }
}
