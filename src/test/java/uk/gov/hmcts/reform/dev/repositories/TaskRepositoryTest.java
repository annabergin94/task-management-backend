package uk.gov.hmcts.reform.dev.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.models.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void save_shouldPersistTaskAndGenerateId() {
        Task saved = taskRepository.save(buildTask("Update case details", TaskStatus.PENDING));
        assertNotNull(saved.getId());
        assertEquals("Update case details", saved.getTitle());
        assertEquals(TaskStatus.PENDING, saved.getStatus());
    }

    @Test
    void findById() {
        Task saved = taskRepository.save(buildTask("Find Task", TaskStatus.IN_PROGRESS));
        Optional<Task> result = taskRepository.findById(saved.getId());
        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    void findById_shouldReturnEmpty_whenTaskDoesNotExist() {
        Optional<Task> result = taskRepository.findById(10L);
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_shouldReturnAllPersistedTasks() {
        taskRepository.save(buildTask("Review Case", TaskStatus.PENDING));
        taskRepository.save(buildTask("Update Case", TaskStatus.COMPLETED));
        List<Task> result = taskRepository.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void deletesTask() {
        Task saved = taskRepository.save(buildTask("Delete Task", TaskStatus.PENDING));
        taskRepository.delete(saved);
        assertFalse(taskRepository.findById(saved.getId()).isPresent());
    }
}
