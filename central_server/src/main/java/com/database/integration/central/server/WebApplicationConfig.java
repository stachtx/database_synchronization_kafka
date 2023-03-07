package com.database.integration.central.server;


import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebApplicationConfig implements WebMvcConfigurer {

  private String notFoundUrlPath = "/notFound";

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController(notFoundUrlPath).setViewName("forward:/ui/index.html");
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**");
  }

  @Bean
  public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
    return container -> {
      container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
          "/notFound"));
      container.addErrorPages(new ErrorPage(HttpStatus.LOOP_DETECTED, notFoundUrlPath));
    };
  }
}