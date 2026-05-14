package uk.gov.hmcts.reform.dev.dtos;

import jakarta.validation.constraints.NotNull;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;

public record UpdatedTaskStatusDTO(@NotNull TaskStatus status) {
}
