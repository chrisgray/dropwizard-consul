package com.yammer.dropwizard.consul.healthcheck;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.sun.jersey.api.client.ClientResponse;
import com.yammer.dropwizard.consul.client.ConsulClient;
import com.yammer.metrics.core.HealthCheck;

import javax.ws.rs.core.Response;

public class ConsulHealthcheck extends HealthCheck {
    protected final String applicationName;
    protected final ConsulClient client;

    public ConsulHealthcheck(String applicationName, ConsulClient client) {
        super(applicationName);
        this.applicationName = applicationName;
        this.client = client;
    }

    @Override
    protected Result check() throws Exception {
        final String consulCheckId = Joiner.on(':').join("service", applicationName);
        final Optional<ClientResponse> clientResponse = client.v1AgentCheckPass(consulCheckId);
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