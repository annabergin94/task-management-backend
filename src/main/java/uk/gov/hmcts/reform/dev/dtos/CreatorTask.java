package uk.gov.hmcts.reform.dev.dtos;

import jakarta.validation.constraints.NotBlank;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;

import java.time.LocalDateTime;

/**
 * DTO for creating a task
 * @param title, description, status, due date
 */
public record CreatorTask(@NotBlank String title, String description, TaskStatus status, LocalDateTime dueDate) {
}
