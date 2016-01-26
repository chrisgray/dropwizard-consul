package com.yammer.dropwizard.consul.ribbon.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.net.InetAddresses;
import com.netflix.loadbalancer.Server;
import com.yammer.dropwizard.consul.api.CatalogServiceModel;
import com.yammer.dropwizard.consul.client.ConsulClient;
import com.yammer.dropwizard.consul.ribbon.RibbonServerList;
import com.yammer.dropwizard.consul.ribbon.RibbonServerListFactory;
import org.junit.Before;
import org.junit.Test;

public class RibbonServerListTest {
    private ConsulClient consulClient;
    private final String serviceId = "serviceId";

    @Before
    public void setup() {
        consulClient = mock(ConsulClient.class);
    }

    @Test
    public void initialServerList() {
        final ImmutableList<Server> servers = ImmutableList.of(new Server("test.com", 8080));
        final RibbonServerList serverList = new RibbonServerList(servers, consulClient, serviceId);
        assertThat(serverList.getInitialListOfServers()).isEqualTo(servers);
    }


    @Test
    public void updatedListWithFailingConsulClient() {
        when(consulClient.v1CatalogService(serviceId)).thenReturn(Optional.<Iterable<CatalogServiceModel>>absent());
        final RibbonServerList serverList = new RibbonServerListFactory(consulClient).create(serviceId);
        assertThat(serverList.getInitialListOfServers()).isEqualTo(ImmutableList.<Server>of());
        assertThat(serverList.getUpdatedListOfServers()).isEqualTo(ImmutableList.<Server>of());
    }

    @Test
    public void updatedListProperlyWorks() {
        when(consulClient.v1CatalogService(serviceId)).thenReturn(Optional
            .<Iterable<CatalogServiceModel>>of(
                ImmutableList.of(new CatalogServiceModel(
                    InetAddresses.forString("127.0.0.1"),
                    "test.com",
                    serviceId,
                    serviceId,
                    (short) 8080,
                    ImmutableList.<String>of()))
            ));
        final RibbonServerList serverList = new RibbonServerListFactory(consulClient).create(serviceId);
        assertThat(serverList.getInitialListOfServers()).containsExactly(new Server("test.com", 8080));

        when(consulClient.v1CatalogService(serviceId)).thenReturn(Optional
            .<Iterable<CatalogServiceModel>>of(
                ImmutableList.of(new CatalogServiceModel(
                    InetAddresses.forString("127.0.0.1"),
                    "test2.com",
                    serviceId,
                    serviceId,
                    (short) 9090,
                    ImmutableList.<String>of()))
            ));
        assertThat(serverList.getUpdatedListOfServers()).containsExactly(new Server("test2.com", 9090));
    }
}