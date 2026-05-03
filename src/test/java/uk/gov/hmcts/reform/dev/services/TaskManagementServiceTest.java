package uk.gov.hmcts.reform.dev.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskRequestDTO;
import uk.gov.hmcts.reform.dev.dtos.UpdatedTaskStatusRequestDTO;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.exceptions.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repositories.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskManagementServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskManagementService taskManagementService;

    private Task task;

    private static final Long MOCK_ID = 10L;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.PENDING);
        task.setDueDate(LocalDateTime.now().plusDays(1));
    }


    @Test
    void getTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Task result = taskManagementService.getTaskById(1L);
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        verify(taskRepository).findById(1L);
    }

    @Test
    void getTaskByIdThrowsTaskNotFoundException() {
        when(taskRepository.findById(MOCK_ID)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskManagementService.getTaskById(MOCK_ID));
        verify(taskRepository).findById(MOCK_ID);
    }


    @Test
    void getAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        List<Task> result = taskManagementService.getAllTasks();
        assertEquals(1, result.size());
        assertEquals(task.getId(), result.get(0).getId());
        verify(taskRepository).findAll();
    }

    @Test
    void getAllTasksReturnsEmptyList() {
        when(taskRepository.findAll()).thenReturn(List.of());
        List<Task> result = taskManagementService.getAllTasks();
        assertEquals(0, result.size());
        verify(taskRepository).findAll();
    }

    @Test
    void updateTaskStatus() {
        UpdatedTaskStatusRequestDTO statusUpdate = new UpdatedTaskStatusRequestDTO(TaskStatus.COMPLETED);
        when(taskRepository.findById(MOCK_ID)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        Task result = taskManagementService.updateTaskStatus(MOCK_ID, statusUpdate);
        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        verify(taskRepository).findById(MOCK_ID);
        verify(taskRepository).save(task);
    }

    @Test
    void updateTaskStatusThrowsTaskNotFoundException() {
        UpdatedTaskStatusRequestDTO statusUpdate = new UpdatedTaskStatusRequestDTO(TaskStatus.COMPLETED);
        when(taskRepository.findById(MOCK_ID)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskManagementService.updateTaskStatus(MOCK_ID, statusUpdate));
        verify(taskRepository).findById(MOCK_ID);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTask() {
        when(taskRepository.findById(MOCK_ID)).thenReturn(Optional.of(task));
        taskManagementService.deleteTask(MOCK_ID);
        verify(taskRepository).findById(MOCK_ID);
        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTaskThrowsTaskNotFoundException() {
        assertThrows(TaskNotFoundException.class, () -> taskManagementService.deleteTask(MOCK_ID));
        verify(taskRepository, never()).delete(any());
    }


    @Test
    void createTask() {
        CreateTaskRequestDTO creatorTask = new CreateTaskRequestDTO(
            "Test Task",
            "Test Description",
            TaskStatus.PENDING,
            task.getDueDate()
        );
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task result = taskManagementService.createTask(creatorTask);
        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(task.getStatus(), result.getStatus());
        verify(taskRepository).save(any(Task.class));
    }
}
