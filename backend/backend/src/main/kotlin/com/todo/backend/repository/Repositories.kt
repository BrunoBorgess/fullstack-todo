package com.todo.backend.repository

import com.todo.backend.model.Task
import com.todo.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}

interface TaskRepository : JpaRepository<Task, Long> {
    fun findAllByUserId(userId: Long): List<Task>
    fun findByIdAndUserId(id: Long, userId: Long): Optional<Task>
}