package top.starrysea.filter;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebAppConfigurer extends WebMvcConfigurationSupport {

	/*
	 * 因为springboot2开始spring-mobile不再支持,所以需要显式注入才能试Device生效
	 */
	@Bean
	public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
		return new DeviceResolverHandlerInterceptor();
	}

	@Bean
	public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
		return new DeviceHandlerMethodArgumentResolver();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new PreSessionInterceptor()).addPathPatterns("/work/add", "/work/remove",
				"/activity/add", "/activity/modify", "/activity/remove", "/activity/funding/add",
				"/activity/funding/remove", "/order", "/order/modify", "/order/remove", "/order/export");
		registry.addInterceptor(new AfterLoginInterceptor()).addPathPatterns("/admin/login");
		registry.addInterceptor(new ErrorInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(new AfterExitInterceptor()).addPathPatterns("/admin/exit");
		registry.addInterceptor(new PreUserLoginInterceptor()).addPathPatterns("/user/changePassword", "/user/info",
				"/user/changePassword", "/order/add", "/order/toAddOrder", "/car/remove", "/car", "/car/removes");
		registry.addInterceptor(new PreUserLoginForAjaxInterceptor()).addPathPatterns("/car/add");
		registry.addInterceptor(deviceResolverHandlerInterceptor());
		registry.addInterceptor(new AfterUserExitInterceptor()).addPathPatterns("/user/exit");
		super.addInterceptors(registry);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(deviceHandlerMethodArgumentResolver());
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		super.addResourceHandlers(registry);
	}
}
