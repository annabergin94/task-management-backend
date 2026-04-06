package uk.gov.hmcts.reform.dev.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.dtos.CreatorTask;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusUpdate;
import uk.gov.hmcts.reform.dev.models.Task;
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
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskManagementService.getTaskById(id));
    }

    @Operation(summary = "Get all tasks")
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskManagementService.getAllTasks());
    }

    @Operation(summary = "Update the status of a given task")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatusUpdate taskStatusUpdate) {
        return ResponseEntity.ok(taskManagementService.updateTaskStatus(id, taskStatusUpdate));
    }

    @Operation(summary = "Delete a task")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskManagementService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new task")
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody CreatorTask task) {
        return ResponseEntity.ok(taskManagementService.createTask(task));
    }
}
