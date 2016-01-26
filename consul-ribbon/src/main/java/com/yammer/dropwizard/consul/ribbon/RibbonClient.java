package com.yammer.dropwizard.consul.ribbon;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.JerseyClient;

public class RibbonClient extends JerseyClient {
    protected ZoneAwareLoadBalancer<Server> loadBalancer;
    protected Client client;

    public RibbonClient(ZoneAwareLoadBalancer<Server> loadBalancer, Client client) {
        this.loadBalancer = loadBalancer;
        this.client = client;
    }

    public WebTarget target() {
        return super.target(UriBuilder
                .fromPath("http://" + fetchServerOrThrow().getHostPort())
                .build());
    }

    private Server fetchServerOrThrow() {
        final Server server = loadBalancer.chooseServer();
        if (server == null) {
            throw new IllegalStateException("No available server for " + loadBalancer.getName());
        }
        return server;
    }
}