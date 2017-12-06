package com.whollyframework.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.whollyframework.constans.Environment;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/* 
 * Restful API 访问路径: 
 * http://IP:port/{context-path}/swagger-ui.html 
 * eg:http://localhost:8080/demo-web/swagger-ui.html 
 */  
@EnableWebMvc
@EnableSwagger2  
@Configuration  
public class SwaggerConfig{

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo()).select() // 选择那些路径和api会生成document
				.apis(RequestHandlerSelectors.basePackage( "com.whollyframework" )) // 对所有api进行监控
				//.paths(PathSelectors.any()) // 对所有路径进行监控
				.paths(PathSelectors.ant("/api/**")) 
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Rest API")
				.description("API 在线文档管理")
				.termsOfServiceUrl("www.baidu.com")
				.contact(Environment.getInstance().getWebSiteTitle())
				.version("V3.8.0").build();
	}
}
