package uk.gov.hmcts.reform.dev.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.dtos.CreatorTask;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusUpdate;
import uk.gov.hmcts.reform.dev.exceptions.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskManagementService {

    private final TaskRepository taskRepository;

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTaskStatus(Long id, TaskStatusUpdate statusUpdate) {
        Task task = getTaskById(id);
        task.setStatus(statusUpdate.status());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }

    public Task createTask(CreatorTask taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        task.setStatus(taskRequest.status());
        task.setDueDate(taskRequest.dueDate());
        return taskRepository.save(task);
    }

}
