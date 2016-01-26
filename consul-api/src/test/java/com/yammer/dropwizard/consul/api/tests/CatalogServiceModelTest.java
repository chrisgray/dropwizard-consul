package com.yammer.dropwizard.consul.api.tests;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.net.InetAddresses;
import com.yammer.dropwizard.consul.api.CatalogServiceModel;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;
import java.io.IOException;
import javax.validation.Validator;

public class CatalogServiceModelTest {
    private final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();

    @Test
    public void toObject() throws IOException {

        final CatalogServiceModel model = MAPPER.readValue(
            fixture("catalogServiceModel.json"), CatalogServiceModel.class);

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
