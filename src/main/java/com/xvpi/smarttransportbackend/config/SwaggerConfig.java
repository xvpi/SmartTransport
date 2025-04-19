package com.xvpi.smarttransportbackend.config;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import springfox.documentation.builders.ApiInfoBuilder;
        import springfox.documentation.builders.PathSelectors;
        import springfox.documentation.builders.RequestHandlerSelectors;
        import springfox.documentation.service.ApiInfo;
        import springfox.documentation.spi.DocumentationType;
        import springfox.documentation.spring.web.plugins.Docket;
        import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 指定构建api⽂档的详细信息的⽅法：apiInfo()
                .apiInfo(apiInfo())
                .select()
                // 指定要⽣成api接⼝的包路径，这⾥把controller作为包路径，⽣成controller中的所接⼝
                .apis(RequestHandlerSelectors.basePackage("com.xvpi.smarttransportbackend.controller"))
                .paths(PathSelectors.any())
                .build();
    }
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
}
