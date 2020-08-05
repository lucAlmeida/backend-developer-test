package com.forleven.backenddevelopertest.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.forleven.backenddevelopertest"))
				.paths(PathSelectors.regex("/api.*"))
				.build()
				.apiInfo(metaInfo())
				.useDefaultResponseMessages(false);
	}
	
	private ApiInfo metaInfo() {
		return new ApiInfo(
				"Student Registration API",
				"REST API for Student Registration (Forleven Backend Developer Test)",
				"1.0",
				"",
				new Contact("Lucas Almeida", "", ""),
				"GNU General Public License v3.0",
				"https://www.gnu.org/licenses/gpl-3.0.html", Collections.emptyList()
		);
	}
}
