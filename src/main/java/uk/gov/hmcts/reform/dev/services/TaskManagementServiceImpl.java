package uk.gov.hmcts.reform.dev.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskRequestDTO;
import uk.gov.hmcts.reform.dev.dtos.UpdatedTaskStatusDTO;
import uk.gov.hmcts.reform.dev.exceptions.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional
    public Task updateTaskStatus(Long id, UpdatedTaskStatusDTO statusUpdate) {
        Task task = getTaskById(id);
        task.setStatus(statusUpdate.status());
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.delete(getTaskById(id));
    }

    @Transactional
    public Task createTask(CreateTaskRequestDTO taskRequest) {
        return taskRepository.save(new Task(
            taskRequest.title(),
            taskRequest.description(),
            taskRequest.status(),
            taskRequest.dueDate()
        ));
    }

}
