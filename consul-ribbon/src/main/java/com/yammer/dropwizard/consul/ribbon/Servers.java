package com.yammer.dropwizard.consul.ribbon;

import com.google.common.collect.ImmutableList;
import com.netflix.loadbalancer.Server;
import com.yammer.dropwizard.consul.api.CatalogServiceModel;

public class Servers {
    private Servers() {
    }

    public static ImmutableList<Server> from(Iterable<CatalogServiceModel> models) {
        final ImmutableList.Builder<Server> servers = ImmutableList.builder();
        for (CatalogServiceModel model : models) {
            servers.add(new Server(model.getNode(), model.getServicePort()));
        }
        return servers.build();
    }
}
