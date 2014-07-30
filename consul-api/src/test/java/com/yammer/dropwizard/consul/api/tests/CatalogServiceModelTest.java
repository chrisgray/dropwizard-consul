package com.yammer.dropwizard.consul.api.tests;

import com.google.common.collect.ImmutableList;
import com.google.common.net.InetAddresses;
import com.yammer.dropwizard.consul.api.CatalogServiceModel;
import com.yammer.dropwizard.validation.Validator;
import org.junit.Test;

import java.io.IOException;

import static com.yammer.dropwizard.testing.JsonHelpers.fromJson;
import static com.yammer.dropwizard.testing.JsonHelpers.jsonFixture;
import static org.fest.assertions.api.Assertions.assertThat;

public class CatalogServiceModelTest {
    private final Validator validator = new Validator();

    @Test
    public void toObject() throws IOException {

        final CatalogServiceModel model = fromJson(jsonFixture("catalogServiceModel.json"), CatalogServiceModel.class);

        assertThat(validator.validate(model)).isEmpty();
        assertThat(model).isEqualTo(new CatalogServiceModel(
            InetAddresses.forString("10.84.228.186"),
            "test-node",
            "foobar",
            "foobar",
            (short) 8080,
            ImmutableList.of("production")));
    }
}
