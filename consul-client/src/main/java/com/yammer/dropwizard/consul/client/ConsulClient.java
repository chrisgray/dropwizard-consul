package com.yammer.dropwizard.consul.client;

import static com.codahale.metrics.MetricRegistry.name;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.yammer.dropwizard.consul.api.CatalogServiceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

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
        this(new MetricRegistry(), client, consulUri);
    }

    public ConsulClient(MetricRegistry registry,
                        Client client,
                        URI consulUri) {
        this.v1AgentCheckPassTimer = registry.timer(name(ConsulClient.class, "v1AgentCheckPass"));
        this.v1CatalogServiceTimer = registry.timer(name(ConsulClient.class, "v1CatalogService"));
        this.client = client;
        this.consulUri = consulUri;
    }

    public Optional<Response> v1AgentCheckPass(String checkId) {
        try (Timer.Context timerContext = v1AgentCheckPassTimer.time()) {
            Response clientResponse = null;
            try {
                return Optional.of(clientResponse = client.target(consulUri)
                    .path(V1_CHECK_PASS_ID)
                    .path(checkId)
                    .request()
                    .get());
            } catch (ProcessingException err) {
                LOGGER.warn("Exception on {}", V1_CHECK_PASS_ID, err);
            } finally {
                if (clientResponse != null) {
                    clientResponse.bufferEntity();
                    clientResponse.close();
                }
            }
            return Optional.absent();
        }
    }

    public Optional<Iterable<CatalogServiceModel>> v1CatalogService(String serviceId) {
        try (Timer.Context timerContext = v1CatalogServiceTimer.time()) {
            return Optional.<Iterable<CatalogServiceModel>>of(client.target(consulUri)
                .path(V1_CATALOG_SERVICE_ID)
                .path(serviceId)
                .request()
                .get(new GenericType<ImmutableList<CatalogServiceModel>>() {
                }));
        } catch (ProcessingException err) {
            LOGGER.warn("Exception on {}", V1_CATALOG_SERVICE_ID, err);
        }
        return Optional.absent();
    }
}