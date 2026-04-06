package uk.gov.hmcts.reform.dev.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.hmcts.reform.dev.dtos.CreatorTask;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusUpdate;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.services.TaskManagementService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TaskManagementControllerTest {

    @Mock
    private TaskManagementService taskManagementService;

    @InjectMocks
    private TaskManagementController taskManagementController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Task task;
    private CreatorTask creatorTask;
    private TaskStatusUpdate taskStatusUpdate;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(taskManagementController)
            .build();

        objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Description");
        task.setStatus(TaskStatus.PENDING);
        task.setDueDate(LocalDateTime.now());

        creatorTask = new CreatorTask("New Task", "New Description", TaskStatus.IN_PROGRESS, LocalDateTime.now());
        taskStatusUpdate = new TaskStatusUpdate(TaskStatus.COMPLETED);
    }

    @Test
    void getTaskById_shouldReturnTask() throws Exception {
        when(taskManagementService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/tasks/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskManagementService).getTaskById(1L);
    }

    @Test
    void getAllTasks_shouldReturnList() throws Exception {
        when(taskManagementService.getAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].title").value("Test Task"));

        verify(taskManagementService).getAllTasks();
    }

    @Test
    void updateTaskStatus_shouldReturnUpdatedTask() throws Exception {
        when(taskManagementService.updateTaskStatus(eq(1L), any(TaskStatusUpdate.class))).thenReturn(task);

        mockMvc.perform(patch("/tasks/1/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(taskStatusUpdate)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));

        verify(taskManagementService).updateTaskStatus(eq(1L), any(TaskStatusUpdate.class));
    }

    @Test
    void deleteTask_shouldReturnNoContent() throws Exception {
        doNothing().when(taskManagementService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/delete/1"))
            .andExpect(status().isNoContent());

        verify(taskManagementService).deleteTask(1L);
    }

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        when(taskManagementService.createTask(any(CreatorTask.class))).thenReturn(task);

        mockMvc.perform(post("/tasks/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(creatorTask)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskManagementService).createTask(any(CreatorTask.class));
    }
}
