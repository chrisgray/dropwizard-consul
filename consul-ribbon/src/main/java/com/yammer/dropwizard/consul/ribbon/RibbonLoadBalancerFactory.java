package com.yammer.dropwizard.consul.ribbon;

import com.google.common.primitives.Ints;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.WeightedResponseTimeRule;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;

public class RibbonLoadBalancerFactory {
    protected final RibbonServerListFactory serverListFactory;

    public RibbonLoadBalancerFactory(RibbonServerListFactory serverListFactory) {
        this.serverListFactory = serverListFactory;
    }

    public ZoneAwareLoadBalancer<Server> create(RibbonLoadBalancerConfiguration configuration) {
        final DefaultClientConfigImpl clientConfig = new DefaultClientConfigImpl();
        clientConfig.setClientName(configuration.getServiceId());
        clientConfig.set(
                CommonClientConfigKey.ServerListRefreshInterval,
                Ints.checkedCast(configuration.getRefreshInterval().toMilliseconds()));
        return LoadBalancerBuilder
                .newBuilder()
                .withClientConfig(clientConfig)
                .withRule(new WeightedResponseTimeRule())
                .withDynamicServerList(serverListFactory.create(configuration.getServiceId()))
                .buildDynamicServerListLoadBalancer();
    }
}