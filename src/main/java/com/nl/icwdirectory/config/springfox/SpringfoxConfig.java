package com.nl.icwdirectory.config.springfox;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Configuration
@EnableSwagger2
public class SpringfoxConfig {

  @Bean
  public Docket gatewayApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.regex("/api/.*"))
        .build()
        .pathMapping("/")
        .useDefaultResponseMessages(false)
        .globalResponseMessage(RequestMethod.POST, defaultErrorsMessage())
        .apiInfo(apiInfo());
  }

  private List<ResponseMessage> defaultErrorsMessage() {
    return List.of(
        new ResponseMessageBuilder().code(INTERNAL_SERVER_ERROR.value())
            .message("Server error while processing request").build(),
        new ResponseMessageBuilder().code(NOT_FOUND.value())
            .message("Data not found for the input parameters").build(),
        new ResponseMessageBuilder().code(BAD_REQUEST.value())
            .message("Invalid request performed").build()
    );
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("International Creative Woman Directory")
        .description("Applicaton for businesses's directory management")
        .version("1.0")
        .build();
  }

}
