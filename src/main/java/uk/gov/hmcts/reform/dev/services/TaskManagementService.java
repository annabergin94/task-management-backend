package uk.gov.hmcts.reform.dev.services;

import uk.gov.hmcts.reform.dev.dtos.CreateTaskRequestDTO;
import uk.gov.hmcts.reform.dev.dtos.UpdatedTaskStatusDTO;
import uk.gov.hmcts.reform.dev.models.Task;

import java.util.List;

public interface TaskManagementService {

    Task getTaskById(Long id);

    List<Task> getAllTasks();

    Task updateTaskStatus(Long id, UpdatedTaskStatusDTO taskStatusUpdate);

    void deleteTask(Long id);

    Task createTask(CreateTaskRequestDTO task);
}
