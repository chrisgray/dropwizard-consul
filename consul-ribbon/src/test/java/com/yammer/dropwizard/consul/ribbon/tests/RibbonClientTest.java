package com.yammer.dropwizard.consul.ribbon.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.codahale.metrics.MetricRegistry;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.yammer.dropwizard.consul.ribbon.RibbonClient;
import com.yammer.dropwizard.consul.ribbon.RibbonClientFactory;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Environment;
import io.dropwizard.validation.BaseValidator;
import org.junit.Before;
import org.junit.Test;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public class RibbonClientTest {
    private Client client;
    private ZoneAwareLoadBalancer<Server> zoneAwareLoadBalancer;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        final Environment environment = new Environment("test-environment",
            Jackson.newObjectMapper(), BaseValidator.newValidator(), new MetricRegistry(),
            getClass().getClassLoader());
        client = new JerseyClientBuilder(environment).build("consul");
        zoneAwareLoadBalancer = mock(ZoneAwareLoadBalancer.class);
    }

    @Test
    public void webResourceHasAValidUri() {
        when(zoneAwareLoadBalancer.chooseServer()).thenReturn(new Server("test.com", 8080));
        final RibbonClient ribbonClient = new RibbonClientFactory(zoneAwareLoadBalancer).wrap(client);
        final WebTarget webResource = ribbonClient.target();
        assertThat(webResource.getUri().getScheme()).isEqualTo("http");
        assertThat(webResource.getUri().getHost()).isEqualTo("test.com");
        assertThat(webResource.getUri().getPort()).isEqualTo(8080);
        assertThat(webResource.getUri()).isEqualTo(URI.create("http://test.com:8080"));
    }

    @Test(expected = IllegalStateException.class)
    public void webResourceHasANullUri() {
        when(zoneAwareLoadBalancer.chooseServer()).thenReturn(null);
        final RibbonClient ribbonClient = new RibbonClientFactory(zoneAwareLoadBalancer).wrap(client);
        final WebTarget webResource = ribbonClient.target();
        assertThat(webResource.getUri()).isEqualTo(URI.create("http://test.com:8080"));
    }

    @Test
    public void asyncWebResourceHasAValidUri() {
        when(zoneAwareLoadBalancer.chooseServer()).thenReturn(new Server("test.com", 8080));
        final RibbonClient ribbonClient = new RibbonClientFactory(zoneAwareLoadBalancer).wrap(client);
        final WebTarget asyncWebResource = ribbonClient.target();
        assertThat(asyncWebResource.getUri().getScheme()).isEqualTo("http");
        assertThat(asyncWebResource.getUri().getHost()).isEqualTo("test.com");
        assertThat(asyncWebResource.getUri().getPort()).isEqualTo(8080);
        assertThat(asyncWebResource.getUri()).isEqualTo(URI.create("http://test.com:8080"));
    }
}
