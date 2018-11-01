package ru.moolls.webchat.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import ru.moolls.webchat.security.interceptor.ActualUserUpdateInterceptor;
import ru.moolls.webchat.security.interceptor.BadUserStatusInterceptor;
import ru.moolls.webchat.security.interceptor.PermissionCheckerInterceptor;

import java.util.List;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "ru.moolls.webchat")
public class WebContextConfig implements WebMvcConfigurer {


    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/img/**").addResourceLocations("/img/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("/favicon.ico");
        registry.addResourceHandler("/main.js").addResourceLocations("/main.js");
        registry.addResourceHandler("/index.html").addResourceLocations("/index.html");
        registry.addResourceHandler("/").addResourceLocations("/index.html");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{x:[\\w\\-]+}")
                .setViewName("forward:/index.html");

        registry.addViewController("/{x:^(?!api$).*$}/**/{y:[\\w\\-]+}")
                .setViewName("forward:/index.html");
    }

    @Bean
    public ActualUserUpdateInterceptor setActualUserInterceptor() {
        return new ActualUserUpdateInterceptor();
    }

    @Bean
    public PermissionCheckerInterceptor permissionCheckerInterceptor() {
        return new PermissionCheckerInterceptor();
    }

    @Bean
    BadUserStatusInterceptor badUserStatusInterceptor() {
        return new BadUserStatusInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(setActualUserInterceptor())
                .addPathPatterns("/api/**");

        registry.addInterceptor(permissionCheckerInterceptor())
                .addPathPatterns("/api/**");

        registry.addInterceptor(badUserStatusInterceptor())
                .addPathPatterns("/api/**");
    }


}
