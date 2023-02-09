package nextmainfocus.account;

import lombok.Getter;
import nextmainfocus.util.Translator;

@Getter
public enum AccountMessenger {
	ACCOUNT_FOUND("account.found"),
	ACCOUNT_NOT_FOUND(
			"account.not.found"),
	ACCOUNT_CREATED(
			"account.created"),
	ACCOUNT_NOT_CREATED(
			"account.not.created"),
	ACCOUNT_DELETED("account.deleted"),
	ACCOUNT_NOT_DELETED("account.not.deleted"),
	ACCOUNT_UPDATED("account.updated"),
	;

	private String message;

	AccountMessenger(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return Translator.toLocale(this.message);
	}
}
