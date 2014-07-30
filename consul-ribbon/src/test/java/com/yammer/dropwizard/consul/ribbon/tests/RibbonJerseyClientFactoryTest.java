package com.yammer.dropwizard.consul.ribbon.tests;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.yammer.dropwizard.client.JerseyClientBuilder;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.consul.ribbon.RibbonJerseyClientFactory;
import com.yammer.dropwizard.json.ObjectMapperFactory;
import com.yammer.dropwizard.validation.Validator;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RibbonJerseyClientFactoryTest {
    private Client client;
    private ZoneAwareLoadBalancer<Server> zoneAwareLoadBalancer;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        final Environment environment = new Environment("test-environment", new Configuration(), new ObjectMapperFactory(), new Validator());
        client = new JerseyClientBuilder()
            .using(environment)
            .build();
        zoneAwareLoadBalancer = mock(ZoneAwareLoadBalancer.class);
    }

    @Test
    public void webResourceHasAValidUri() {
        when(zoneAwareLoadBalancer.chooseServer()).thenReturn(new Server("test.com", 8080));
        final RibbonJerseyClientFactory factory = new RibbonJerseyClientFactory(client, zoneAwareLoadBalancer);
        final WebResource webResource = factory.webResource();
        assertThat(webResource.getURI()).isEqualTo(URI.create("test.com:8080"));
    }

    @Test(expected = IllegalStateException.class)
    public void webResourceHasANullUri() {
        when(zoneAwareLoadBalancer.chooseServer()).thenReturn(null);
        final RibbonJerseyClientFactory factory = new RibbonJerseyClientFactory(client, zoneAwareLoadBalancer);
        final WebResource webResource = factory.webResource();
        assertThat(webResource.getURI()).isEqualTo(URI.create("test.com:8080"));
    }

    @Test
    public void asyncWebResourceHasAValidUri() {
        when(zoneAwareLoadBalancer.chooseServer()).thenReturn(new Server("test.com", 8080));
        final RibbonJerseyClientFactory factory = new RibbonJerseyClientFactory(client, zoneAwareLoadBalancer);
        final AsyncWebResource asyncWebResource = factory.asyncWebResource();
        assertThat(asyncWebResource.getURI()).isEqualTo(URI.create("test.com:8080"));
    }
}
