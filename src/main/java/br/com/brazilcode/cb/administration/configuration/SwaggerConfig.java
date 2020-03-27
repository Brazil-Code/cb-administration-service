package br.com.brazilcode.cb.administration.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import io.swagger.models.auth.In;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Classe responsável por configurar o uso do Swagger na aplicação.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 20 de mar de 2020 19:38:42
 * @version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("br.com.brazilcode.cb.administration.controller"))
          .paths(PathSelectors.any())
          .build()
          .apiInfo(metaInfo())
          .securitySchemes(Arrays.asList(new ApiKey("Token Access", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
          .securityContexts(Arrays.asList(securityContext()));
    }

	@SuppressWarnings("rawtypes")
	private ApiInfo metaInfo() {
		return new ApiInfo(
				"Brazil Code - Administration Service REST APIs", 
				"Administration module of the Clean Budget Project - By: Brazil Code", 
				"1.0", 
				"Terms of Service",
				new Contact("Gabriel Guarido", "https://www.linkedin.com/in/gabriel-guarido-oliveira/", 
						"gabrielguarido.oliveira@gmail.com"),
				"MIT License",
				"https://opensource.org/licenses/MIT", new ArrayList<VendorExtension>()
		);
	}

	private SecurityContext securityContext() {
	    return SecurityContext.builder()
	        .securityReferences(defaultAuth())
	        .forPaths(PathSelectors.any())
	        .build();
	}

	List<SecurityReference> defaultAuth() {
	    AuthorizationScope authorizationScope
	        = new AuthorizationScope("ADMIN", "accessEverything");
	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	    authorizationScopes[0] = authorizationScope;
	    return Arrays.asList(
	        new SecurityReference("Token Access", authorizationScopes));
	}

}
