package com.example.product.controller;

import com.example.product.dto.request.CategoryCreateReq;
import com.example.product.dto.request.CategoryUpdateReq;
import com.example.product.dto.resp.CategoryResp;
import com.example.product.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryResp> create(@RequestBody @Valid CategoryCreateReq category) {

        log.info("Create category: {}", category);

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(category));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResp>> getAll() {

        log.info("Get all categories");

        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResp> get(@PathVariable Long id) {
        log.info("Find category by id: {}", id);

        return ResponseEntity.ok(service.get(id));
    }

    @PatchMapping
    public ResponseEntity<CategoryResp> update(@RequestBody @Valid CategoryUpdateReq category) {

        log.info("Update category: {}", category);

        return ResponseEntity.ok(service.update(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        log.info("Delete category: {}", id);
        service.delete(id);

        return ResponseEntity.ok().build();
    }
}
