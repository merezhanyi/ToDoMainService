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

	public static final String HEALTHCHECK_RESPONSE = "healthcheck.response";
	public static final String TASK_FOUND = "task.found";
	public static final String TASK_NOT_FOUND = "task.not.found";
	public static final String TASK_CREATED = "task.created";
	public static final String TASK_NOT_CREATED = "task.not.created";
	public static final String TASK_UPDATED = "task.updated";
	public static final String TASK_DELETED = "task.deleted";
	public static final String TASK_NOT_DELETED = "task.not.deleted";
	public static final String ERROR_SERVER = "error.server";
	public static final String ERROR_INVALID_UUID = "error.invalid.uuid";
}
