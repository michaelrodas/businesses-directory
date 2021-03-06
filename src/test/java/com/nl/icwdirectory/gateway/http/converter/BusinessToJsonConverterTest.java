package com.nl.icwdirectory.gateway.http.converter;

import com.nl.icwdirectory.domain.Address;
import com.nl.icwdirectory.domain.Business;
import com.nl.icwdirectory.gateway.http.json.BusinessJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class BusinessToJsonConverterTest {

    private BusinessToJsonConverter businessToJsonConverter;

    @BeforeEach
    void setupTest() {
        businessToJsonConverter = new BusinessToJsonConverter();
    }

    @Test
    void shouldConvertCreateUserJsonToUser() {
        Business businessToConvert = Business.builder()
                .name("Granny's clothing")
                .ownerFirstName("Satan")
                .ownerLastName("Lucifer")
                .address(Address.builder()
                        .city("Eindhoven").postCode("5618ZW").street("Bouteslaan 123")
                        .build())
                .email("klerengekste@gmail.com")
                .website("www.customclothing.nl")
                .phone("+316666666")
                .logo("aUrl")
                .images(Collections.singletonList("aUrl"))
                .description("The business purpose")
                .tags(List.of("clothing", "kleren"))
                .build();

        BusinessJson convertedUser = businessToJsonConverter.convert(businessToConvert);

        assertThat(convertedUser).usingRecursiveComparison().isEqualTo(businessToConvert);
    }

}