package uk.gov.hmcts.reform.dev.dtos;

import jakarta.validation.constraints.NotNull;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;

/**
 * DTO for updating the status of a given task
 * @param status
 */
public record TaskStatusUpdate(@NotNull TaskStatus status) {
}
