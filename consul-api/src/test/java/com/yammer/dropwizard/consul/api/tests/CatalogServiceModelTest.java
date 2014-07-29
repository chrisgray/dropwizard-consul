package com.yammer.dropwizard.consul.api.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.net.InetAddresses;
import com.yammer.dropwizard.consul.api.CatalogServiceModel;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.validation.valuehandling.OptionalValidatedValueUnwrapper;
import org.hibernate.validator.HibernateValidator;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.api.Assertions.assertThat;

public class CatalogServiceModelTest {
    private final Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .addValidatedValueHandler(new OptionalValidatedValueUnwrapper())
            .buildValidatorFactory()
            .getValidator();
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();

    @Test
    public void toObject() throws IOException {
        final CatalogServiceModel model = objectMapper
                .readValue(fixture("catalogServiceModel.json"), CatalogServiceModel.class);
        assertThat(validator.validate(model)).isEmpty();
        assertThat(model).isEqualTo(new CatalogServiceModel(
                InetAddresses.forString("10.84.228.186"),
                "test-node",
                "foobar",
                "foobar",
                (short)8080,
                ImmutableList.of("production")));
    }
}
