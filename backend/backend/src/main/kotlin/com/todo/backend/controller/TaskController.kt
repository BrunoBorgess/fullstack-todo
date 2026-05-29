package com.todo.backend.controller

import com.todo.backend.dto.TaskRequest
import com.todo.backend.dto.TaskResponse
import com.todo.backend.service.TaskService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tasks")
class TaskController(
    private val taskService: TaskService
) {

    @GetMapping
    fun getAll(@AuthenticationPrincipal user: UserDetails): ResponseEntity<List<TaskResponse>> {
        return ResponseEntity.ok(taskService.getAll(user.username))
    }

    @PostMapping
    fun create(
        @AuthenticationPrincipal user: UserDetails,
        @RequestBody request: TaskRequest
    ): ResponseEntity<TaskResponse> {
        return ResponseEntity.ok(taskService.create(user.username, request))
    }

    @PutMapping("/{id}")
    fun update(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable id: Long,
        @RequestBody request: TaskRequest
    ): ResponseEntity<TaskResponse> {
        return try {
            ResponseEntity.ok(taskService.update(user.username, id, request))
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun delete(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        return try {
            taskService.delete(user.username, id)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }
}