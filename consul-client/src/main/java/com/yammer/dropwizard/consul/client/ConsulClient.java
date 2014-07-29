package com.yammer.dropwizard.consul.client;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.sun.jersey.api.client.*;
import com.yammer.dropwizard.consul.api.CatalogServiceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static com.codahale.metrics.MetricRegistry.name;

public class ConsulClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsulClient.class);
    protected static final String v1CheckPassId = "/v1/agent/check/pass";
    protected static final String v1CatalogServiceId = "/v1/catalog/service";
    private final Timer v1AgentCheckPassTimer;
    private final Timer v1CatalogServiceTimer;
    protected final Client client;
    protected final URI consulUri;

    public ConsulClient(MetricRegistry metricRegistry,
                        Client client,
                        URI consulUri) {
        this.v1AgentCheckPassTimer = metricRegistry.timer(name(ConsulClient.class, "v1AgentCheckPass"));
        this.v1CatalogServiceTimer = metricRegistry.timer(name(ConsulClient.class, "v1CatalogService"));
        this.client = client;
        this.consulUri = consulUri;
    }

    @SuppressWarnings("unused")
    public Optional<ClientResponse> v1AgentCheckPass(String checkId) {
        try (Timer.Context timerContext = v1AgentCheckPassTimer.time()) {
            ClientResponse clientResponse = null;
            try {
                return Optional.of(clientResponse = client.resource(consulUri)
                        .path(v1CheckPassId)
                        .path(checkId)
                        .get(ClientResponse.class));
            } catch (ClientHandlerException | UniformInterfaceException err) {
                LOGGER.warn("Exception on {}", v1CheckPassId, err);
            } finally {
                if (clientResponse != null) {
                    clientResponse.bufferEntity();
                    clientResponse.close();
                }
            }
            return Optional.absent();
        }
    }

    @SuppressWarnings("unused")
    public Optional<Iterable<CatalogServiceModel>> v1CatalogService(String serviceId) {
        try (Timer.Context timerContext = v1CatalogServiceTimer.time()) {
            return Optional.<Iterable<CatalogServiceModel>>of(client.resource(consulUri)
                .path(v1CatalogServiceId)
                .path(serviceId)
                .get(new GenericType<ImmutableList<CatalogServiceModel>>(){}));
        } catch (ClientHandlerException | UniformInterfaceException err) {
            LOGGER.warn("Exception on {}", v1CatalogServiceId, err);
        }
        return Optional.absent();
    }
}