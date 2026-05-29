package com.todo.backend.controller

import com.todo.backend.dto.AuthResponse
import com.todo.backend.dto.LoginRequest
import com.todo.backend.dto.RegisterRequest
import com.todo.backend.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return try {
            ResponseEntity.ok(authService.register(request))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return try {
            ResponseEntity.ok(authService.login(request))
        } catch (e: Exception) {
            ResponseEntity.status(401).build()
        }
    }
}