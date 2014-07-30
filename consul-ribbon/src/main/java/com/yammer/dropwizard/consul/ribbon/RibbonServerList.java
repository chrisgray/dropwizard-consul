package com.yammer.dropwizard.consul.ribbon;

import com.google.common.base.Optional;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.yammer.dropwizard.consul.api.CatalogServiceModel;
import com.yammer.dropwizard.consul.client.ConsulClient;

import java.util.List;

public class RibbonServerList implements ServerList<Server> {
    private List<Server> serverList;
    private final ConsulClient client;
    private final String serviceId;

    public RibbonServerList(List<Server> serverList, ConsulClient client, String serviceId) {
        this.serverList = serverList;
        this.client = client;
        this.serviceId = serviceId;
    }

    @Override
    public List<Server> getInitialListOfServers() {
        return serverList;
    }

    @Override
    public List<Server> getUpdatedListOfServers() {
        final Optional<Iterable<CatalogServiceModel>> models = client.v1CatalogService(serviceId);
        if (models.isPresent()) {
            serverList = Servers.from(models.get());
        }
        return serverList;
    }
}