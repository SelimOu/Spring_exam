package com.example.exam.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.exam.dto.TaskDTO;
import com.example.exam.dto.TaskStatusDTO;
import com.example.exam.model.Task;
import com.example.exam.model.TaskStatus;
import com.example.exam.repository.ProjectRepository;
import com.example.exam.repository.TaskRepository;
import com.example.exam.repository.UserRepository;

    @RestController
    @RequestMapping("/api/tasks")
    public class TaskController {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskController(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO dto) {
    var projectOpt = projectRepository.findById(dto.getProjectId());
    var userOpt = userRepository.findById(dto.getAssigneeId());
    if (projectOpt.isEmpty() || userOpt.isEmpty()) {
        return ResponseEntity.badRequest().build();
    }  
    Task task = new Task();
    task.setTitle(dto.getName());
    task.setStatus(TaskStatus.TODO);
    task.setProject(projectOpt.get());
    task.setAssignee(userOpt.get());
    return ResponseEntity.ok(taskRepository.save(task));
}

    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatusDTO dto) {
    Optional<Task> taskOpt = taskRepository.findById(id);
    if (taskOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    Task task = taskOpt.get();
    task.setStatus(dto.getStatus());
    return ResponseEntity.ok(taskRepository.save(task));
}
}