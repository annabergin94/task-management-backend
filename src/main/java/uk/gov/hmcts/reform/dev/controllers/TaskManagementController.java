package uk.gov.hmcts.reform.dev.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskRequestDTO;
import uk.gov.hmcts.reform.dev.dtos.TaskResponseDTO;
import uk.gov.hmcts.reform.dev.dtos.UpdatedTaskStatusRequestDTO;
import uk.gov.hmcts.reform.dev.services.TaskManagementService;

import java.util.List;

/**
 * Controller for managing the tasks of a given case
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Task CRUD Operations", description = "REST Endpoints for creating, retrieving, updating and deleting tasks, as well as updating the status of a task.")
public class TaskManagementController {

    private final TaskManagementService taskManagementService;

    @Operation(summary = "Get a task by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(TaskResponseDTO.from(taskManagementService.getTaskById(id)));
    }

    @Operation(summary = "Get all tasks")
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskManagementService.getAllTasks().stream()
                                     .map(TaskResponseDTO::from)
                                     .toList()
        );
    }

    @Operation(summary = "Update the status of a given task")
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(@PathVariable Long id, @Valid @RequestBody UpdatedTaskStatusRequestDTO taskStatusUpdate) {
        return ResponseEntity.ok(TaskResponseDTO.from(taskManagementService.updateTaskStatus(id, taskStatusUpdate)));
    }

    @Operation(summary = "Delete a task")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskManagementService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new task")
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskRequestDTO task) {
        TaskResponseDTO created = TaskResponseDTO.from(taskManagementService.createTask(task));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
