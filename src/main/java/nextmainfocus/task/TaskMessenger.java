package nextmainfocus.task;

import lombok.Getter;
import nextmainfocus.util.Translator;

@Getter
public enum TaskMessenger {
	TASK_FOUND("task.found"),
	TASK_NOT_FOUND(
			"task.not.found"),
	TASK_CREATED(
			"task.created"),
	TASK_NOT_CREATED(
			"task.not.created"),
	TASK_DELETED("task.deleted"),
	TASK_NOT_DELETED("task.not.deleted"),
	TASK_UPDATED("task.updated"),
	;

	private String message;

	TaskMessenger(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return Translator.toLocale(this.message);
	}
}
