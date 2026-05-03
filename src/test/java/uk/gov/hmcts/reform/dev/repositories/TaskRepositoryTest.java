package uk.gov.hmcts.reform.dev.repositories;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.models.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test repository using real H2 database
 */
@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private Task buildTask(String title, TaskStatus status) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription("A task to add details for the case");
        task.setStatus(status);
        task.setDueDate(LocalDateTime.now().plusDays(1));
        return task;
    }

    @Test
    void save_shouldPersist_taskAndID() {
        Task saved = taskRepository.save(buildTask("Update case details", TaskStatus.PENDING));
        assertNotNull(saved.getId());
        assertEquals("Update case details", saved.getTitle());
        assertEquals(TaskStatus.PENDING, saved.getStatus());
    }

    @Test
    void findById_shouldReturn_ifTaskExists() {
        Task saved = taskRepository.save(buildTask("Find Task", TaskStatus.IN_PROGRESS));
        Optional<Task> result = taskRepository.findById(saved.getId());
        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    void findById_shouldReturnEmpty_noTaskExists() {
        assertTrue(taskRepository.findById(73L).isEmpty());
    }

    @Test
    void findAll_shouldReturnAllTasks() {
        taskRepository.deleteAll(); // remove any records from taskData.sql

        taskRepository.save(buildTask("Case 1", TaskStatus.COMPLETED));
        taskRepository.save(buildTask("Case 2", TaskStatus.IN_PROGRESS));
        taskRepository.save(buildTask("Case 3", TaskStatus.PENDING));

        List<Task> result = taskRepository.findAll();
        assertEquals(3, result.size());
    }

    @Test
    void delete_shouldRemoveTask() {
        Task saved = taskRepository.save(buildTask("Delete Task", TaskStatus.PENDING));
        taskRepository.delete(saved);
        assertTrue(taskRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void save_shouldFail_whenTitleIsBlank(){
        Task task = buildTask("", TaskStatus.IN_PROGRESS);
        assertThrows(
            ConstraintViolationException.class, () -> {
            taskRepository.saveAndFlush(task);
        });
    }

    @Test
    void save_shouldFail_whenStatusIsNull() {
        Task task = buildTask("Case 1", null);
        assertThrows(ConstraintViolationException.class, () -> {
            taskRepository.saveAndFlush(task);
        });
    }
}
