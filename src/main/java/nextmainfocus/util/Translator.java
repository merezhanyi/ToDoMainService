package nextmainfocus.util;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class Translator {

	private static ResourceBundleMessageSource messageSource;

	@SuppressWarnings("squid:S3010")
	Translator(ResourceBundleMessageSource messageSource) {
		Translator.messageSource = messageSource;
	}

	public static String toLocale(String msg) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(msg, null, locale);
	}
}
