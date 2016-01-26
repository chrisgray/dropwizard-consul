package com.yammer.dropwizard.consul.healthcheck;

import com.yammer.dropwizard.consul.client.ConsulClientConfiguration;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ConsulHealthcheckConfiguration {
    @NotNull
    @NotEmpty
    private String applicationName;

    @NotNull
    @Valid
    private Duration checkInterval = Duration.seconds(5);

    @NotNull
    @Valid
    private ConsulClientConfiguration client = new ConsulClientConfiguration();

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Duration getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(Duration checkInterval) {
        this.checkInterval = checkInterval;
    }

    public ConsulClientConfiguration getClient() {
        return client;
    }

    public void setClient(ConsulClientConfiguration client) {
        this.client = client;
    }
}