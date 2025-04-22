package com.xvpi.smarttransportbackend.config;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import springfox.documentation.builders.ApiInfoBuilder;
        import springfox.documentation.builders.PathSelectors;
        import springfox.documentation.builders.RequestHandlerSelectors;
        import springfox.documentation.service.ApiInfo;
        import springfox.documentation.service.ApiKey;
        import springfox.documentation.service.AuthorizationScope;
        import springfox.documentation.service.SecurityReference;
        import springfox.documentation.spi.DocumentationType;
        import springfox.documentation.spi.service.contexts.SecurityContext;
        import springfox.documentation.spring.web.plugins.Docket;
        import springfox.documentation.swagger2.annotations.EnableSwagger2;

        import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 构建api⽂档的详细信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 设置⻚⾯标题
                .title("重庆西站交通态势平台接口文档")
                // 设置接⼝描述
                .description("包含管理端、大屏端、交警端接口文档")
                // 设置版本
                .version("1")
                // 构建
                .build();
    }
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2) // 注意是 SWAGGER_2，不是 OAS_30
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xvpi.smarttransportbackend.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(List.of(apiKey()))
                .securityContexts(List.of(securityContext()));
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = authorizationScope;
        return List.of(new SecurityReference("JWT", scopes));
    }

}
