package com.yammer.dropwizard.consul.ribbon;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class RibbonClient extends Client {
    protected ZoneAwareLoadBalancer<Server> loadBalancer;
    protected Client client;

    public RibbonClient(ZoneAwareLoadBalancer<Server> loadBalancer, Client client) {
        this.loadBalancer = loadBalancer;
        this.client = client;
    }

    public WebResource resource() {
        return super.resource(UriBuilder
                .fromPath(fetchServerOrThrow().getHostPort())
                .build());
    }

    public AsyncWebResource asyncResource() {
        return super.asyncResource(UriBuilder
                .fromPath(fetchServerOrThrow().getHostPort())
                .build());
    }

    @Override @Deprecated
    public WebResource resource(URI u) {
        return resource();
    }

    @Override @Deprecated
    public AsyncWebResource asyncResource(URI u) {
        return asyncResource();
    }

    private Server fetchServerOrThrow() {
        final Server server = loadBalancer.chooseServer();
        if (server == null) {
            throw new IllegalStateException("No available server for " + loadBalancer.getName());
        }
        return server;
    }
}