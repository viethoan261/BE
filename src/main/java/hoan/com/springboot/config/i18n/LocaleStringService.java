package hoan.com.springboot.config.i18n;

import java.util.Locale;

public interface LocaleStringService {
    Locale getCurrentLocale();

    String getMessage(String messageCode, String defaultMessage, Object... params);
}