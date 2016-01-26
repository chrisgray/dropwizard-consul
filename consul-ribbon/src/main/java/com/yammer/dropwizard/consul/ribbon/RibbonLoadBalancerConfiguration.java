package com.yammer.dropwizard.consul.ribbon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RibbonLoadBalancerConfiguration {
    @NotNull
    @NotEmpty
    private String serviceId;

    @NotNull
    @Valid
    private Duration refreshInterval = Duration.seconds(10);

    public RibbonLoadBalancerConfiguration(String serviceId) {
        this.serviceId = serviceId;
    }

    @JsonCreator
    public RibbonLoadBalancerConfiguration(@JsonProperty("serviceId") String serviceId,
                                           @JsonProperty("refreshInterval") Duration refreshInterval) {
        this.serviceId = serviceId;
        this.refreshInterval = refreshInterval;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Duration getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(Duration refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RibbonLoadBalancerConfiguration that = (RibbonLoadBalancerConfiguration) o;

        return Objects.equals(refreshInterval, that.refreshInterval)
               && Objects.equals(serviceId,  that.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, refreshInterval);
    }
}