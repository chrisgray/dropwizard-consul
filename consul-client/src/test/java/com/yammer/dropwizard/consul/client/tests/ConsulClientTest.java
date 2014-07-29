package com.yammer.dropwizard.consul.client.tests;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Optional;
import com.sun.jersey.api.client.ClientResponse;
import com.yammer.dropwizard.consul.client.ConsulClient;
import com.yammer.dropwizard.consul.client.ConsulClientConfiguration;
import com.yammer.dropwizard.consul.client.ConsulClientFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Environment;
import io.dropwizard.validation.valuehandling.OptionalValidatedValueUnwrapper;
import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.validation.Validation;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.fest.assertions.api.Assertions.assertThat;

public class ConsulClientTest {
    private ConsulClientConfiguration configuration;
    private Environment environment;

    @Before
    public void setup() {
        configuration = new ConsulClientConfiguration();
        environment = environment();
    }

    private Environment environment() {
        return new Environment(
                "test-environment",
                Jackson.newObjectMapper(),
                Validation
                        .byProvider(HibernateValidator.class)
                        .configure()
                        .addValidatedValueHandler(new OptionalValidatedValueUnwrapper())
                        .buildValidatorFactory()
                .getValidator(),
                new MetricRegistry(),
                Thread.currentThread().getContextClassLoader());

    }

    @Test
    public void noConsulAgent() {
        configuration.setUri(URI.create("http://localhost:12345"));
        final ConsulClient client = new ConsulClientFactory(configuration).create(environment);
        final Optional<ClientResponse> clientResponse = client.v1AgentCheckPass("does-not-matter");
        assertThat(clientResponse.isPresent()).isFalse();
    }

    @Test @Ignore //Need an actual real check and live consul agent running
    public void integrationWithInvalidCheckId() {
        final ConsulClient client = new ConsulClientFactory(configuration).create(environment);
        final Optional<ClientResponse> clientResponse = client.v1AgentCheckPass("invalid:id");
        assertThat(clientResponse.isPresent()).isTrue();
        assertThat(clientResponse.get().getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test @Ignore //Need an actual real check and live consul agent running
    public void integrationWithActualCheck() {
        final ConsulClient client = new ConsulClientFactory(configuration).create(environment);
        final Optional<ClientResponse> clientResponse = client.v1AgentCheckPass("service:foobar");
        assertThat(clientResponse.isPresent()).isTrue();
        assertThat(clientResponse.get().getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }
}