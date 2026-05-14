package uk.gov.hmcts.reform.dev.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.dev.exceptions.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.services.TaskManagementServiceImpl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskManagementController.class)
class TaskManagementControllerIntegrationTest {

    private final MockMvc mockMvc;

    @Mock
    private TaskManagementServiceImpl taskManagementService;

    TaskManagementControllerIntegrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void getTaskById() throws Exception {
        when(taskManagementService.getTaskById(99L)).thenThrow(new TaskNotFoundException(99L));
        mockMvc.perform(get("/tasks/99")).andExpect(status().isNotFound());
    }
}
