package com.yammer.dropwizard.consul.example;

import com.yammer.dropwizard.consul.client.ConsulClientConfiguration;
import com.yammer.dropwizard.consul.client.ConsulClientFactory;
import com.yammer.dropwizard.consul.ribbon.RibbonClientFactory;
import com.yammer.dropwizard.consul.ribbon.RibbonLoadBalancerConfiguration;
import com.yammer.dropwizard.consul.ribbon.RibbonLoadBalancerFactory;
import com.yammer.dropwizard.consul.ribbon.RibbonServerListFactory;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;

import java.net.URI;

import javax.ws.rs.client.Client;

public class ExampleService extends Application<Configuration> {
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
        final Client client = new JerseyClientBuilder(environment).build("example");
        final RibbonClientFactory ribbonClientFactory = new RibbonClientFactory(
                new RibbonLoadBalancerFactory(
                        new RibbonServerListFactory(
                                new ConsulClientFactory(consulClientConfiguration).create(environment)))
                        .create(ribbonLoadBalancerConfiguration));
        ribbonClientFactory.wrap(client);
    }
}
