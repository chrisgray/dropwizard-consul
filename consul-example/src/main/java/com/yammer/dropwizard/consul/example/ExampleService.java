package com.yammer.dropwizard.consul.example;

import com.sun.jersey.api.client.Client;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.client.JerseyClientBuilder;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.consul.client.ConsulClientConfiguration;
import com.yammer.dropwizard.consul.client.ConsulClientFactory;
import com.yammer.dropwizard.consul.ribbon.RibbonClientFactory;
import com.yammer.dropwizard.consul.ribbon.RibbonLoadBalancerConfiguration;
import com.yammer.dropwizard.consul.ribbon.RibbonLoadBalancerFactory;
import com.yammer.dropwizard.consul.ribbon.RibbonServerListFactory;
import com.yammer.dropwizard.util.Duration;

import java.net.URI;

public class ExampleService extends Service<Configuration> {
    public static void main(String[] args) throws Exception {
        new ExampleService().run(args);
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        final ConsulClientConfiguration consulClientConfiguration = new ConsulClientConfiguration();
        consulClientConfiguration.setUri(URI.create("http://localhost:8500"));
        final RibbonLoadBalancerConfiguration ribbonLoadBalancerConfiguration = new RibbonLoadBalancerConfiguration(
                "example",
                Duration.seconds(2));
        final Client client = new JerseyClientBuilder().using(environment).build();
        final RibbonClientFactory ribbonClientFactory = new RibbonClientFactory(
                new RibbonLoadBalancerFactory(
                        new RibbonServerListFactory(
                                new ConsulClientFactory(consulClientConfiguration).create(environment)))
                        .create(ribbonLoadBalancerConfiguration));
        ribbonClientFactory.wrap(client);
    }
}
