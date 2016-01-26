package com.yammer.dropwizard.consul.client.tests;

import static org.assertj.core.api.Assertions.assertThat;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.net.InetAddresses;
import com.yammer.dropwizard.consul.api.CatalogServiceModel;
import com.yammer.dropwizard.consul.client.ConsulClient;
import com.yammer.dropwizard.consul.client.ConsulClientConfiguration;
import com.yammer.dropwizard.consul.client.ConsulClientFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Environment;
import io.dropwizard.validation.BaseValidator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import javax.ws.rs.core.Response;
import java.net.URI;

public class ConsulClientTest {
    private ConsulClientConfiguration configuration;
    private Environment environment;

    @Before
    public void setup() {
        configuration = new ConsulClientConfiguration();
        environment = new Environment("test-environment", Jackson.newObjectMapper(),
            BaseValidator.newValidator(), new MetricRegistry(), getClass().getClassLoader());
    }

    @Test
    public void noConsulAgent() {
        configuration.setUri(URI.create("http://localhost:12345"));
        final ConsulClient client = new ConsulClientFactory(configuration).create(environment);
        final Optional<Response> clientResponse = client.v1AgentCheckPass("does-not-matter");
        assertThat(clientResponse.isPresent()).isFalse();
    }

    @Test
    @Ignore //Need an actual real check and live consul agent running
    public void integrationWithInvalidCheckId() {
        final ConsulClient client = new ConsulClientFactory(configuration).create(environment);
        final Optional<Response> clientResponse = client.v1AgentCheckPass("invalid:id");
        assertThat(clientResponse.isPresent()).isTrue();
        assertThat(clientResponse.get().getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    @Ignore //Need an actual real check and live consul agent running
    public void integrationWithActualCheck() {
        final ConsulClient client = new ConsulClientFactory(configuration).create(environment);
        final Optional<Response> clientResponse = client.v1AgentCheckPass("service:foobar");
        assertThat(clientResponse.isPresent()).isTrue();
        assertThat(clientResponse.get().getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    @Ignore
    public void v1CatalogServiceId() {
        final ConsulClient client = new ConsulClientFactory(configuration).create(environment);
        final Optional<Iterable<CatalogServiceModel>> catalogServiceModels = client.v1CatalogService("foobar");
        assertThat(catalogServiceModels.isPresent()).isTrue();
        assertThat(catalogServiceModels.get()).containsExactly(new CatalogServiceModel(
            InetAddresses.forString("10.84.228.186"),
            "C02J42Q4DKQ4",
            "foobar",
            "foobar",
            (short) 8080,
            ImmutableList.of("production")));
    }
}