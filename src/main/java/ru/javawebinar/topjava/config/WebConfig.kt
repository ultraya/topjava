package ru.javawebinar.topjava.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.view.InternalResourceViewResolver

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = ["ru.javawebinar.**.web"])
class WebConfig : WebMvcConfigurer {

    @Autowired
    private lateinit var env: Environment

    override fun configureViewResolvers(registry: ViewResolverRegistry) =
            registry.viewResolver(InternalResourceViewResolver().apply {
                setPrefix("/WEB-INF/jsp/")
                setSuffix(".jsp")
            })

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/")
    }

    @Bean
    fun messageSource() = ReloadableResourceBundleMessageSource().apply {
        setCacheSeconds(5)
        setDefaultEncoding("UTF-8")
        setBasename("file://${env["TOPJAVA_ROOT"]}/config/messages/app")
        setFallbackToSystemLocale(false)
    }
}