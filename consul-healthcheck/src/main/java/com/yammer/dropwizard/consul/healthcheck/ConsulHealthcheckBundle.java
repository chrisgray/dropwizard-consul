package com.yammer.dropwizard.consul.healthcheck;

import com.yammer.dropwizard.consul.client.ConsulClientFactory;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        environment.healthChecks().register("consul-healthcheck", healthcheck);
        environment
                .lifecycle()
                .scheduledExecutorService("consul-healthcheck-scheduler")
                .threads(1)
                .build()
                .scheduleAtFixedRate(
                        new ConsulHealthcheckScheduledTask(healthcheck),
                        0,
                        configuration.getCheckInterval().getQuantity(),
                        configuration.getCheckInterval().getUnit());
    }
}
