package com.yammer.dropwizard.consul.healthcheck.tests;

import com.google.common.io.Resources;
import com.yammer.dropwizard.config.ConfigurationFactory;
import com.yammer.dropwizard.consul.healthcheck.ConsulHealthcheckConfiguration;
import com.yammer.dropwizard.util.Duration;
import com.yammer.dropwizard.validation.Validator;
import org.junit.Test;

import java.io.File;
import java.net.URI;

import static org.fest.assertions.api.Assertions.assertThat;


public class ConsulHealthcheckConfigurationTest {

    @Test
    public void parseConfiguration() throws Exception {
        final ConfigurationFactory<ConsulHealthcheckConfiguration> configurationFactory
            = ConfigurationFactory.forClass(ConsulHealthcheckConfiguration.class, new Validator());
        final File configFile = new File(Resources.getResource("healthcheckConfiguration.yml").toURI());
        final ConsulHealthcheckConfiguration configuration = configurationFactory.build(configFile);
        assertThat(configuration.getApplicationName()).isEqualTo("test");
        assertThat(configuration.getCheckInterval()).isEqualTo(Duration.milliseconds(2500));
        assertThat(configuration.getClient().getConnectionTimeout()).isEqualTo(Duration.milliseconds(100));
        assertThat(configuration.getClient().getTimeout()).isEqualTo(Duration.milliseconds(200));
        assertThat(configuration.getClient().getUri()).isEqualTo(URI.create("http://localhost:8080"));
    }
}
