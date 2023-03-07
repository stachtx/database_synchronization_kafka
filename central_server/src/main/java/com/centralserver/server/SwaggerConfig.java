package com.centralserver.server;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Lists.newArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket SwaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("centralserver")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(getSwaggerPaths())
                .build()
                .globalResponseMessage(RequestMethod.GET,
                        newArrayList(new ResponseMessageBuilder()
                                .code(500)
                                .message("500 message")
                                .responseModel(new ModelRef("Error"))
                                .build()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Central server")
                .description("Spring Demo with Swagger")
                .contact(new Contact("Tomasz Stachura",
                        "",
                        ""))
                .version("1.0.1")
                .build();
    }

    private Predicate<String> getSwaggerPaths() {
        return or(
                regex("/secured.*"),
                regex("/oauth/token.*"),
                regex("/test.*"));
    }
}