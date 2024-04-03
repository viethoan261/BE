package hoan.com.springboot.config.i18n;

import hoan.com.springboot.security.utils.StrUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class LocaleConfig extends AcceptHeaderLocaleResolver {

    private static final Locale DEFAULT_LOCALE = new Locale("vi");

    private static final String HRM_LOCALE_KEY = "hrm-language";

    private static final Map<String, Locale> SUPPORT_LOCALES = Stream.of(
            Locale.ENGLISH,
            DEFAULT_LOCALE
    ).collect(Collectors.toMap(Locale::getLanguage, locale -> locale));

    public LocaleConfig() {
        super();
        setSupportedLocales(new ArrayList<>(SUPPORT_LOCALES.values()));
    }

    @Bean(name = "messageResourceHRM")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
        messageResource.setBasenames("classpath:i18n/messages");
        messageResource.setDefaultEncoding("UTF-8");
        messageResource.setCacheSeconds(60);
        messageResource.setDefaultLocale(getDefaultLocale());
        return messageResource;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        String hrmLanguage = request.getHeader(HRM_LOCALE_KEY);

        String language;

        if (StrUtils.isNotBlank(hrmLanguage)) {
            language = hrmLanguage;
        } else {
            language = acceptLanguage;
        }

        if (!StringUtils.hasText(language)) {
            return getDefaultLocale();
        }
        List<Locale.LanguageRange> languageRanges = Locale.LanguageRange.parse(language);
        Locale foundLocale = Locale.lookup(languageRanges, getSupportedLocales());
        if (foundLocale == null)
            return getDefaultLocale();
        return foundLocale;
    }

    @Override
    public Locale getDefaultLocale() {
        return DEFAULT_LOCALE;
    }
}