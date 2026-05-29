package com.todo.backend.service

import com.todo.backend.dto.TaskRequest
import com.todo.backend.dto.TaskResponse
import com.todo.backend.model.Task
import com.todo.backend.repository.TaskRepository
import com.todo.backend.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {

    fun getAll(username: String): List<TaskResponse> {
        val user = getUser(username)
        return taskRepository.findAllByUserId(user.id).map { it.toResponse() }
    }

    fun create(username: String, request: TaskRequest): TaskResponse {
        val user = getUser(username)
        val task = Task(
            title = request.title,
            description = request.description,
            completed = request.completed,
            user = user
        )
        return taskRepository.save(task).toResponse()
    }

    fun update(username: String, id: Long, request: TaskRequest): TaskResponse {
        val user = getUser(username)
        val task = taskRepository.findByIdAndUserId(id, user.id)
            .orElseThrow { NoSuchElementException("Tarefa não encontrada") }

        val updated = task.copy(
            title = request.title,
            description = request.description,
            completed = request.completed
        )
        return taskRepository.save(updated).toResponse()
    }

    fun delete(username: String, id: Long) {
        val user = getUser(username)
        val task = taskRepository.findByIdAndUserId(id, user.id)
            .orElseThrow { NoSuchElementException("Tarefa não encontrada") }
        taskRepository.delete(task)
    }

    private fun getUser(username: String) =
        userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("Usuário não encontrado") }

    private fun Task.toResponse() = TaskResponse(
        id = id,
        title = title,
        description = description,
        completed = completed
    )
}