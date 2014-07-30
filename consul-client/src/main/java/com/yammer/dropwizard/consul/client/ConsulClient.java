package com.yammer.dropwizard.consul.client;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.sun.jersey.api.client.*;
import com.yammer.dropwizard.consul.api.CatalogServiceModel;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class ConsulClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsulClient.class);
    protected static final String V1_CHECK_PASS_ID = "/v1/agent/check/pass";
    protected static final String V1_CATALOG_SERVICE_ID = "/v1/catalog/service";
    private final Timer v1AgentCheckPassTimer;
    private final Timer v1CatalogServiceTimer;
    protected final Client client;
    protected final URI consulUri;

    public ConsulClient(Client client,
                        URI consulUri) {
        this.v1AgentCheckPassTimer = Metrics.newTimer(ConsulClient.class, "v1AgentCheckPass");
        this.v1CatalogServiceTimer = Metrics.newTimer(ConsulClient.class, "v1CatalogService");
        this.client = client;
        this.consulUri = consulUri;
    }

    @SuppressWarnings("unused")
    public Optional<ClientResponse> v1AgentCheckPass(String checkId) {
        final TimerContext timerContext = v1AgentCheckPassTimer.time();
        try {
            ClientResponse clientResponse = null;
            try {
                return Optional.of(clientResponse = client.resource(consulUri)
                    .path(V1_CHECK_PASS_ID)
                    .path(checkId)
                    .get(ClientResponse.class));
            } catch (ClientHandlerException | UniformInterfaceException err) {
                LOGGER.warn("Exception on {}", V1_CHECK_PASS_ID, err);
            } finally {
                if (clientResponse != null) {
                    clientResponse.bufferEntity();
                    clientResponse.close();
                }
            }
            return Optional.absent();
        } finally {
            timerContext.stop();
        }
    }

    @SuppressWarnings("unused")
    public Optional<Iterable<CatalogServiceModel>> v1CatalogService(String serviceId) {
        TimerContext timerContext = v1CatalogServiceTimer.time();
        try {
            return Optional.<Iterable<CatalogServiceModel>>of(client.resource(consulUri)
                .path(V1_CATALOG_SERVICE_ID)
                .path(serviceId)
                .get(new GenericType<ImmutableList<CatalogServiceModel>>() {
                }));
        } catch (ClientHandlerException | UniformInterfaceException err) {
            LOGGER.warn("Exception on {}", V1_CATALOG_SERVICE_ID, err);
        } finally {
            timerContext.stop();
        }
        return Optional.absent();
    }
}