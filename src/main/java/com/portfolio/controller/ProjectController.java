package com.portfolio.controller;

import com.portfolio.model.Project;
import com.portfolio.repository.ProjectRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // GET /api/projects            -> all projects
    // GET /api/projects?featured=true
    // GET /api/projects?category=API
    @GetMapping
    public List<Project> getProjects(
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) String category) {

        if (Boolean.TRUE.equals(featured)) {
            return projectRepository.findByFeaturedTrueOrderByDisplayOrderAsc();
        }
        if (category != null && !category.isBlank()) {
            return projectRepository.findByCategoryIgnoreCaseOrderByDisplayOrderAsc(category);
        }
        return projectRepository.findAllByOrderByDisplayOrderAsc();
    }

    // GET /api/projects/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/projects
    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) {
        Project saved = projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/projects/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @Valid @RequestBody Project updated) {
        return projectRepository.findById(id).map(existing -> {
            existing.setTitle(updated.getTitle());
            existing.setSummary(updated.getSummary());
            existing.setDescription(updated.getDescription());
            existing.setTechStack(updated.getTechStack());
            existing.setCategory(updated.getCategory());
            existing.setGithubUrl(updated.getGithubUrl());
            existing.setLiveUrl(updated.getLiveUrl());
            existing.setImageUrl(updated.getImageUrl());
            existing.setFeatured(updated.isFeatured());
            existing.setDisplayOrder(updated.getDisplayOrder());
            return ResponseEntity.ok(projectRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/projects/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (!projectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        projectRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
