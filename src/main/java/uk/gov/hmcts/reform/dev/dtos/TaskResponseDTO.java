package uk.gov.hmcts.reform.dev.dtos;

import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.models.Task;

import java.time.LocalDateTime;

public record TaskResponseDTO(Long id, String title, String description, TaskStatus status, LocalDateTime dueDate) {
    // converts database entity to a DTO
    public static TaskResponseDTO from(Task task) {
        return new TaskResponseDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getDueDate()
        );
    }
}
