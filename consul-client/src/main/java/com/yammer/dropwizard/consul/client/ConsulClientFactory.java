package com.yammer.dropwizard.consul.client;


import com.yammer.dropwizard.client.JerseyClientBuilder;
import com.yammer.dropwizard.config.Environment;

public class ConsulClientFactory {
    private final ConsulClientConfiguration configuration;

    public ConsulClientFactory(ConsulClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public ConsulClient create(Environment environment) {
        return new ConsulClient(
            new JerseyClientBuilder()
                .using(environment)
                .using(configuration)
                .build(),
            configuration.getUri()
        );
    }
}