package com.yammer.dropwizard.consul.healthcheck;

import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.consul.client.ConsulClientFactory;


public class ConsulHealthcheckBundle implements ConfiguredBundle<ConsulHealthcheckConfiguration> {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(ConsulHealthcheckConfiguration configuration, Environment environment) throws Exception {

        final ConsulClientFactory clientFactory = new ConsulClientFactory(configuration.getClient());
        final ConsulHealthcheck healthcheck = new ConsulHealthcheck(
            configuration.getApplicationName(),
            clientFactory.create(environment));
        environment.addHealthCheck(healthcheck);
        environment
            .managedScheduledExecutorService("consul-healthcheck-scheduler", 1)
            .scheduleAtFixedRate(
                new ConsulHealthcheckScheduledTask(healthcheck),
                0,
                configuration.getCheckInterval().getQuantity(),
                configuration.getCheckInterval().getUnit());
    }
}
