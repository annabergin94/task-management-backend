package uk.gov.hmcts.reform.dev.dtos;

import jakarta.validation.constraints.NotBlank;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;

import java.time.LocalDateTime;

public record CreateTaskRequestDTO(@NotBlank String title, String description, TaskStatus status,
                                   LocalDateTime dueDate) {
}
