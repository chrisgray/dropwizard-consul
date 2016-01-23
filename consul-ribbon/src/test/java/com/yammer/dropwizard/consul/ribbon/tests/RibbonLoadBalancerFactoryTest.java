package com.yammer.dropwizard.consul.ribbon.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.yammer.dropwizard.consul.ribbon.RibbonLoadBalancerConfiguration;
import com.yammer.dropwizard.consul.ribbon.RibbonLoadBalancerFactory;
import com.yammer.dropwizard.consul.ribbon.RibbonServerList;
import com.yammer.dropwizard.consul.ribbon.RibbonServerListFactory;
import io.dropwizard.util.Duration;
import org.junit.Before;
import org.junit.Test;

public class RibbonLoadBalancerFactoryTest {
    private RibbonServerListFactory serverListFactory;
    private final String serviceId = "testServiceId";
    private RibbonServerList ribbonServerList;

    @Before
    public void setup() {
        ribbonServerList = mock(RibbonServerList.class);
        serverListFactory = mock(RibbonServerListFactory.class);
    }

    @Test
    public void configParameterers() {
        final Server server = new Server("test.com", 8080);
        final ImmutableList<Server> servers = ImmutableList.of(server);
        when(serverListFactory.create(serviceId)).thenReturn(ribbonServerList);
        when(ribbonServerList.getInitialListOfServers()).thenReturn(servers);
        when(ribbonServerList.getUpdatedListOfServers()).thenReturn(servers);
        final RibbonLoadBalancerFactory factory = new RibbonLoadBalancerFactory(serverListFactory);
        final ZoneAwareLoadBalancer<Server> zoneAwareLoadBalancer = factory.create(new RibbonLoadBalancerConfiguration(serviceId));
        assertThat(zoneAwareLoadBalancer.getName()).isEqualTo(serviceId);
        assertThat(zoneAwareLoadBalancer
            .getClientConfig()
            .getPropertyAsInteger(CommonClientConfigKey.ServerListRefreshInterval, -1))
            .isEqualTo(Ints.checkedCast(Duration.seconds(10).toMilliseconds()));
    }

    @Test
    public void loadBalancerChooseServer() {
        final Server server = new Server("test.com", 8080);
        final ImmutableList<Server> servers = ImmutableList.of(server);
        when(serverListFactory.create(serviceId)).thenReturn(ribbonServerList);
        when(ribbonServerList.getInitialListOfServers()).thenReturn(servers);
        when(ribbonServerList.getUpdatedListOfServers()).thenReturn(servers);
        final RibbonLoadBalancerFactory factory = new RibbonLoadBalancerFactory(serverListFactory);
        assertThat(factory.create(new RibbonLoadBalancerConfiguration(serviceId)).chooseServer()).isEqualTo(server);
    }

    @Test
    public void emptyServerList() {
        final ImmutableList<Server> servers = ImmutableList.of();
        when(serverListFactory.create(serviceId)).thenReturn(ribbonServerList);
        when(ribbonServerList.getInitialListOfServers()).thenReturn(servers);
        when(ribbonServerList.getUpdatedListOfServers()).thenReturn(servers);
        final RibbonLoadBalancerFactory factory = new RibbonLoadBalancerFactory(serverListFactory);
        assertThat(factory.create(new RibbonLoadBalancerConfiguration(serviceId)).chooseServer()).isEqualTo(null);
    }
}
