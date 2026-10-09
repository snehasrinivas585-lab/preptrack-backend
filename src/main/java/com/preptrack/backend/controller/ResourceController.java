package com.preptrack.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.preptrack.backend.entity.Resource;
import com.preptrack.backend.repository.ResourceRepository;

@RestController
@RequestMapping("/resources")
@CrossOrigin(origins = "http://localhost:5173")
public class ResourceController {

    private final ResourceRepository resourceRepository;

    public ResourceController(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    // Get all resources
    @GetMapping
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    // Get resource by ID
    @GetMapping("/{id}")
    public Resource getResourceById(@PathVariable Long id) {

        Optional<Resource> resource =
                resourceRepository.findById(id);

        return resource.orElse(null);
    }

    // Get resources by category
    @GetMapping("/category/{category}")
    public List<Resource> getResourcesByCategory(
            @PathVariable String category) {

        return resourceRepository.findByCategory(category);
    }

    // Add resource
    @PostMapping
    public Resource addResource(@RequestBody Resource resource) {

        return resourceRepository.save(resource);
    }

    // Update resource
    @PutMapping("/{id}")
    public Resource updateResource(
            @PathVariable Long id,
            @RequestBody Resource updatedResource) {

        Optional<Resource> existingResource =
                resourceRepository.findById(id);

        if (existingResource.isPresent()) {

            Resource resource = existingResource.get();

            resource.setTitle(updatedResource.getTitle());
            resource.setDescription(updatedResource.getDescription());
            resource.setCategory(updatedResource.getCategory());
            resource.setContent(updatedResource.getContent());

            return resourceRepository.save(resource);
        }

        return null;
    }

    // Delete resource
    @DeleteMapping("/{id}")
    public String deleteResource(@PathVariable Long id) {

        if (resourceRepository.existsById(id)) {

            resourceRepository.deleteById(id);

            return "Resource deleted successfully";
        }

        return "Resource not found";
    }
}
