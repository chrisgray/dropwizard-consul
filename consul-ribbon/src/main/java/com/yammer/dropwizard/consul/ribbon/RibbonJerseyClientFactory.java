package com.yammer.dropwizard.consul.ribbon;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.UriBuilder;

public class RibbonJerseyClientFactory {
    private final Client client;
    private final ZoneAwareLoadBalancer<Server> loadBalancer;

    public RibbonJerseyClientFactory(Client client, ZoneAwareLoadBalancer<Server> loadBalancer) {
        this.client = client;
        this.loadBalancer = loadBalancer;
    }

    private Server fetchServerOrThrow() {
        final Server server = loadBalancer.chooseServer();
        if (server == null) {
            throw new IllegalStateException("No available server for " + loadBalancer.getName());
        }
        return server;
    }

    public WebResource webResource() {
        return client.resource(UriBuilder
                .fromPath(fetchServerOrThrow().getHostPort())
                .build());
    }

    public AsyncWebResource asyncWebResource() {
        return client.asyncResource(UriBuilder
                .fromPath(fetchServerOrThrow().getHostPort())
                .build());
    }
}