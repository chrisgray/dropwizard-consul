package com.yammer.dropwizard.consul.ribbon;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.yammer.dropwizard.consul.api.CatalogServiceModel;
import com.yammer.dropwizard.consul.client.ConsulClient;

public class RibbonServerListFactory {
    protected final ConsulClient client;

    public RibbonServerListFactory(ConsulClient client) {
        this.client = client;
    }

    public RibbonServerList create(String serviceId) {
        final Optional<Iterable<CatalogServiceModel>> models = client.v1CatalogService(serviceId);
        return new RibbonServerList(Servers
                .from(models.or(ImmutableList.<CatalogServiceModel>of())), client, serviceId);
    }
}