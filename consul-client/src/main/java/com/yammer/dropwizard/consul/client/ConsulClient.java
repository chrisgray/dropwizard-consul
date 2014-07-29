package com.yammer.dropwizard.consul.client;

import com.google.common.base.Optional;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class ConsulClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsulClient.class);
    protected static final String v1CheckPassId = "/v1/agent/check/pass";
    protected final Client client;
    protected final URI consulUri;

    public ConsulClient(Client client, URI consulUri) {
        this.client = client;
        this.consulUri = consulUri;
    }

    public Optional<ClientResponse> v1AgentCheckPass(String checkId) {
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