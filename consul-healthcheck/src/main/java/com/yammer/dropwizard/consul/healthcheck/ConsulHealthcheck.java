package com.yammer.dropwizard.consul.healthcheck;

import javax.ws.rs.core.Response;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.yammer.dropwizard.consul.client.ConsulClient;

public class ConsulHealthcheck extends HealthCheck {
    protected final String applicationName;
    protected final ConsulClient client;

    public ConsulHealthcheck(String applicationName, ConsulClient client) {
        this.applicationName = applicationName;
        this.client = client;
    }

    @Override
    protected Result check() throws Exception {
        final String consulCheckId = Joiner.on(':').join("service", applicationName);
        final Optional<Response> clientResponse = client.v1AgentCheckPass(consulCheckId);
        if (clientResponse.isPresent()) {
            final int statusCode = clientResponse.get().getStatus();
            if (statusCode == Response.Status.OK.getStatusCode()) {
                return Result.healthy();
            } else {
                return Result.unhealthy("Failed to contact consul for " + consulCheckId + " and received a " +
                    statusCode);
            }
        } else {
            return Result.unhealthy("Consul was unreachable!");
        }
    }
}