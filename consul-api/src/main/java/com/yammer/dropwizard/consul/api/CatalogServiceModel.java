package com.yammer.dropwizard.consul.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.InetAddress;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CatalogServiceModel {
    @NotNull
    @Valid
    private final InetAddress address;
    @NotNull
    @NotEmpty
    private final String node;
    @NotNull
    @NotEmpty
    private final String serviceId;
    @NotNull
    @NotEmpty
    private final String serviceName;
    @Min(value = 0)
    @Max(Short.MAX_VALUE)
    private final short servicePort;
    @NotNull
    @Valid
    private final ImmutableList<String> serviceTags;

    @JsonCreator
    public CatalogServiceModel(@JsonProperty("Address") InetAddress address,
                               @JsonProperty("Node") String node,
                               @JsonProperty("ServiceID") String serviceId,
                               @JsonProperty("ServiceName") String serviceName,
                               @JsonProperty("ServicePort") short servicePort,
                               @JsonProperty("ServiceTags") ImmutableList<String> serviceTags) {
        this.address = address;
        this.node = node;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.servicePort = servicePort;
        this.serviceTags = serviceTags;
    }

    public InetAddress getAddress() {
        return address;
    }

    public String getNode() {
        return node;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public short getServicePort() {
        return servicePort;
    }

    public ImmutableList<String> getServiceTags() {
        return serviceTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CatalogServiceModel that = (CatalogServiceModel) o;

        return Objects.equals(servicePort, that.servicePort)
            && Objects.equals(address, that.address)
            && Objects.equals(node, that.node)
            && Objects.equals(serviceId, that.serviceId)
            && Objects.equals(serviceName, that.serviceName)
            && Objects.equals(serviceTags, that.serviceTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, node, serviceId, serviceName, servicePort, serviceTags);
    }
}