package uk.gov.hmcts.reform.dev.exceptions;

/**
 * Thrown when a task ID does not exist in the database
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("No task found with id: " + id);
    }
}
