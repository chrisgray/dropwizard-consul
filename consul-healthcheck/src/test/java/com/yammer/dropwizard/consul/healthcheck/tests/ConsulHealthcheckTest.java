package com.yammer.dropwizard.consul.healthcheck.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Optional;
import com.yammer.dropwizard.consul.client.ConsulClient;
import com.yammer.dropwizard.consul.healthcheck.ConsulHealthcheck;
import org.junit.Before;
import org.junit.Test;
import javax.ws.rs.core.Response;

public class ConsulHealthcheckTest {
    private final String applicationName = "test";
    private ConsulClient client;
    private Response clientResponse;

    @Before
    public void setup() {
        client = mock(ConsulClient.class);
        clientResponse = mock(Response.class);
    }

    @Test
    public void healthyWhenReturnsOk() {
        when(client.v1AgentCheckPass("service:" + applicationName)).thenReturn(Optional.of(clientResponse));
        when(clientResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        final ConsulHealthcheck healthcheck = new ConsulHealthcheck(applicationName, client);
        assertThat(healthcheck.execute()).isEqualTo(HealthCheck.Result.healthy());
    }

    @Test
    public void unhealthyWhenUnknownCheckId() {
        when(client.v1AgentCheckPass("service:" + applicationName)).thenReturn(Optional.of(clientResponse));
        when(clientResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        final ConsulHealthcheck healthcheck = new ConsulHealthcheck(applicationName, client);
        assertThat(healthcheck.execute().isHealthy()).isFalse();
    }

    @Test
    public void unhealthyWhenUnreachable() {
        when(client.v1AgentCheckPass("service:" + applicationName)).thenReturn(Optional.<Response>absent());

        final ConsulHealthcheck healthcheck = new ConsulHealthcheck(applicationName, client);
        assertThat(healthcheck.execute().isHealthy()).isFalse();
    }
}
