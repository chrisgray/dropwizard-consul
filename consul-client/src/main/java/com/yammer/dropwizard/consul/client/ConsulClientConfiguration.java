package com.yammer.dropwizard.consul.client;

import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class ConsulClientConfiguration extends JerseyClientConfiguration {
    @NotNull
    protected URI uri = URI.create("http://localhost:8500");

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}