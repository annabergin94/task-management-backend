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
import uk.gov.hmcts.reform.dev.dtos.CreateTaskRequestDTO;
import uk.gov.hmcts.reform.dev.dtos.UpdatedTaskStatusDTO;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.services.TaskManagementService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskManagementController).build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        task = new Task("Test Task", "Description", TaskStatus.PENDING, LocalDateTime.now());
    }

    @Test
    void getTaskById_shouldReturnTask() throws Exception {
        when(taskManagementService.getTaskById(1L)).thenReturn(task); // mock ID generation

        mockMvc.perform(get("/tasks/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Test Task"))
            .andExpect(jsonPath("$.status").value("PENDING"));

        verify(taskManagementService).getTaskById(1L);
    }

    @Test
    void getAllTasks_shouldReturnList() throws Exception {
        when(taskManagementService.getAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Test Task"));

        verify(taskManagementService).getAllTasks();
    }

    @Test
    void updateTaskStatus_shouldReturnUpdatedTask() throws Exception {
        when(taskManagementService.updateTaskStatus(eq(1L), any())).thenReturn(task);

        mockMvc.perform(patch("/tasks/1/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new UpdatedTaskStatusDTO(TaskStatus.COMPLETED))))
            .andExpect(status().isOk());
        verify(taskManagementService).updateTaskStatus(eq(1L), any());
    }

    @Test
    void deleteTask_shouldReturnNoContent() throws Exception {

        mockMvc.perform(delete("/tasks/1"))
            .andExpect(status().isNoContent());

        verify(taskManagementService).deleteTask(1L);
    }

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        when(taskManagementService.createTask(any())).thenReturn(task);

        mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                new CreateTaskRequestDTO("New Task", "Desc", TaskStatus.PENDING, LocalDateTime.now()))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskManagementService).createTask(any());
    }
}
