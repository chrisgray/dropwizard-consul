package com.yammer.dropwizard.consul.healthcheck.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.google.common.io.Resources;
import com.yammer.dropwizard.consul.healthcheck.ConsulHealthcheckConfiguration;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.BaseValidator;
import org.junit.Test;
import java.io.File;
import java.net.URI;

public class ConsulHealthcheckConfigurationTest {

    @Test
    public void parseConfiguration() throws Exception {
        final ConfigurationFactory<ConsulHealthcheckConfiguration> configurationFactory
            = new ConfigurationFactory<>(ConsulHealthcheckConfiguration.class, BaseValidator.newValidator(), Jackson.newObjectMapper(), "dw");
        final File configFile = new File(Resources.getResource("healthcheckConfiguration.yml").toURI());
        final ConsulHealthcheckConfiguration configuration = configurationFactory.build(configFile);
        assertThat(configuration.getApplicationName()).isEqualTo("test");
        assertThat(configuration.getCheckInterval()).isEqualTo(Duration.milliseconds(2500));
        assertThat(configuration.getClient().getConnectionTimeout()).isEqualTo(Duration.milliseconds(100));
        assertThat(configuration.getClient().getTimeout()).isEqualTo(Duration.milliseconds(200));
        assertThat(configuration.getClient().getUri()).isEqualTo(URI.create("http://localhost:8080"));
    }
}
