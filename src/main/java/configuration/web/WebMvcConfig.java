package configuration.web;

import com.techbase.support.binding.PagerArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author nguyentanh
 */
@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackages = {
        "com.techbase.domain",
        "com.techbase.infrastructure"
})
@EnableTransactionManagement
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.pagination}")
    private int pagingDefaultSize;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PagerArgumentResolver(pagingDefaultSize));
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasenames(
                "classpath:messages", "classpath:ValidationMessages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheMillis(1000);
        return messageSource;
    }
}
