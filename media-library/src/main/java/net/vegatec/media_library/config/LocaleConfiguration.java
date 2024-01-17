package net.vegatec.media_library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import tech.jhipster.config.locale.AngularCookieLocaleResolver;

@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {

    private final ApplicationProperties applicationProperties;

    public LocaleConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new AngularCookieLocaleResolver("NG_TRANSLATE_LANG_KEY");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        registry.addInterceptor(localeChangeInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //   registry.addResourceHandler("/media/**").addResourceLocations("file:/home/robert/media/");
        final String mediaFolder = applicationProperties.getMediaFolder();
        System.out.println("------------------------------------media folder: '" + mediaFolder + "'");
        registry.addResourceHandler("/media/**").addResourceLocations(String.format("file://%s", mediaFolder));
    }
}
