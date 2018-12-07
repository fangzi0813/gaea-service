package com.cnuip.gaea.service.swagger;


import com.google.common.base.Predicates;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi(BuildProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(properties))
                .select()
                .paths(Predicates.and(Predicates.not(PathSelectors.regex("/log")),
                        Predicates.not(PathSelectors.regex("/error"))))
                .build();
    }

    private ApiInfo apiInfo(BuildProperties properties) {
        return new ApiInfoBuilder()
                .title(properties.getArtifact())
                .description(properties.getName())
                .version(properties.getVersion())
                .build();
    }
}
