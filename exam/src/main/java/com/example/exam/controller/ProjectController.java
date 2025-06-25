package com.example.exam.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.exam.dto.ProjectDTO;
import com.example.exam.model.Project;
import com.example.exam.model.Task;
import com.example.exam.repository.ProjectRepository;
import com.example.exam.repository.TaskRepository;
import com.example.exam.repository.UserRepository;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

   @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO dto) {
    return userRepository.findById(dto.getCreatorId())
            .map(user -> {
                Project project = new Project();
                project.setName(dto.getName());
                project.setCreator(user);
                return ResponseEntity.ok(projectRepository.save(project));
            })
            .orElse(ResponseEntity.badRequest().build());
}

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<Task>> getProjectTasks(@PathVariable Long id) {
        if (!projectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskRepository.findByProjectId(id));
    }
}