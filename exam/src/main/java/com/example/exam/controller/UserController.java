package com.example.exam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.exam.dto.UserDTO;
import com.example.exam.model.User;
import com.example.exam.repository.ProjectRepository;
import com.example.exam.repository.TaskRepository;
import com.example.exam.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public UserController(UserRepository userRepository, ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public User createUser(@RequestBody UserDTO dto) {
    User user = new User();
    user.setUsername(dto.getUsername());
    return userRepository.save(user);
}

    @GetMapping("/{id}/projects")
    public ResponseEntity<?> getUserProjects(@PathVariable Long id) {
        if (!userRepository.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(projectRepository.findAll().stream().filter(p -> p.getCreator().getId().equals(id)).toList());
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<?> getUserTasks(@PathVariable Long id) {
        if (!userRepository.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(taskRepository.findByAssigneeId(id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
    return userRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    }
