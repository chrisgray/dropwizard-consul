package com.yammer.dropwizard.consul.client;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;

public class ConsulClientFactory {
    private final ConsulClientConfiguration configuration;

    public ConsulClientFactory(ConsulClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public ConsulClient create(Environment environment) {
        return new ConsulClient(
                environment.metrics(),
                new JerseyClientBuilder(environment)
                .using(configuration)
                .build("consul-client"),
                configuration.getUri());
    }
}