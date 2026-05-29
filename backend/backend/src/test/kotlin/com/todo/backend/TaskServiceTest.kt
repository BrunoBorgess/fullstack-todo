package com.todo.backend

import com.todo.backend.dto.TaskRequest
import com.todo.backend.model.Task
import com.todo.backend.model.User
import com.todo.backend.repository.TaskRepository
import com.todo.backend.repository.UserRepository
import com.todo.backend.service.TaskService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.util.Optional

class TaskServiceTest {

    private val taskRepository = mock(TaskRepository::class.java)
    private val userRepository = mock(UserRepository::class.java)
    private val taskService = TaskService(taskRepository, userRepository)

    private val user = User(1, "john", "encoded", "john@email.com")

    @Test
    fun `getAll should return tasks for user`() {
        val tasks = listOf(
            Task(1, "Task 1", "Desc 1", false, user),
            Task(2, "Task 2", "Desc 2", true, user)
        )

        `when`(userRepository.findByUsername("john")).thenReturn(Optional.of(user))
        `when`(taskRepository.findAllByUserId(1)).thenReturn(tasks)

        val result = taskService.getAll("john")

        assertEquals(2, result.size)
        assertEquals("Task 1", result[0].title)
        assertEquals("Task 2", result[1].title)
    }

    @Test
    fun `create should save and return task`() {
        val request = TaskRequest("Nova tarefa", "Descrição", false)
        val saved = Task(1, "Nova tarefa", "Descrição", false, user)

        `when`(userRepository.findByUsername("john")).thenReturn(Optional.of(user))
        `when`(taskRepository.save(any())).thenReturn(saved)

        val result = taskService.create("john", request)

        assertEquals("Nova tarefa", result.title)
        assertEquals(false, result.completed)
    }

    @Test
    fun `delete should throw when task not found`() {
        `when`(userRepository.findByUsername("john")).thenReturn(Optional.of(user))
        `when`(taskRepository.findByIdAndUserId(99, 1)).thenReturn(Optional.empty())

        assertThrows<NoSuchElementException> {
            taskService.delete("john", 99)
        }
    }

    @Test
    fun `update should update task fields`() {
        val request = TaskRequest("Atualizada", "Nova desc", true)
        val existing = Task(1, "Antiga", "Desc antiga", false, user)
        val updated = Task(1, "Atualizada", "Nova desc", true, user)

        `when`(userRepository.findByUsername("john")).thenReturn(Optional.of(user))
        `when`(taskRepository.findByIdAndUserId(1, 1)).thenReturn(Optional.of(existing))
        `when`(taskRepository.save(any())).thenReturn(updated)

        val result = taskService.update("john", 1, request)

        assertEquals("Atualizada", result.title)
        assertEquals(true, result.completed)
    }
}