package uk.gov.hmcts.reform.dev.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("No task found with id: " + id);
    }
}
