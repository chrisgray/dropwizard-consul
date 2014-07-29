package com.yammer.dropwizard.consul.healthcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsulHealthcheckScheduledTask implements Runnable {
    private static Logger LOGGER = LoggerFactory.getLogger(ConsulHealthcheckScheduledTask.class);
    protected ConsulHealthcheck healthcheck;

    public ConsulHealthcheckScheduledTask(ConsulHealthcheck healthcheck) {
        this.healthcheck = healthcheck;
    }

    @Override
    public void run() {
        try {
            if (!healthcheck.execute().isHealthy()) {
                LOGGER.warn("Was not able to successfully contact Consul");
            }
        } catch (Exception err) {
            LOGGER.warn("Unexpected exception when running ConsulHealthcheck");
        }
    }
}