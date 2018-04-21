package edu.gatech.team7339.vetchain;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Scanner;

@SpringBootApplication
public class VetchainApplication implements WebMvcConfigurer {
    private static String dbUsername;
    private static String dbPassword;
    private static String dbUrl;

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
			"classpath:/META-INF/", "classpath:/resources/",
			"classpath:/static/", "classpath:/public/","classpath:/css","classpath:/js"};
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
        registry.addResourceHandler("/images/**").addResourceLocations("file:///C:/files/");
    }

	private class TomCatConfig {
	    @Bean
        public ServletWebServerFactory serverFactory(){
            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory(){
                @Override
                protected void postProcessContext(Context context) {
                    SecurityConstraint constraint = new SecurityConstraint();
                    SecurityCollection collection = new SecurityCollection();
                    collection.addPattern("/*");
                    constraint.setUserConstraint("CONFIDENTIAL");
                    constraint.addCollection(collection);
                    context.addConstraint(constraint);
                }
            };
            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setScheme("http");
            connector.setPort(8080);
            connector.setSecure(false);
            connector.setRedirectPort(9000);
            tomcat.addAdditionalTomcatConnectors(connector);
            return tomcat;
        }
    }
	public static void main(String[] args) {
		SpringApplication.run(VetchainApplication.class, args);
	}
}
