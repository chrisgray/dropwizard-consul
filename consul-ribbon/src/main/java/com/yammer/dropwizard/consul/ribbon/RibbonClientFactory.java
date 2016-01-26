package com.yammer.dropwizard.consul.ribbon;

import javax.ws.rs.client.Client;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;

public class RibbonClientFactory {
    private final ZoneAwareLoadBalancer<Server> loadBalancer;

    public RibbonClientFactory(ZoneAwareLoadBalancer<Server> loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public RibbonClient wrap(Client client) {
        return new RibbonClient(loadBalancer, client);
    }
}