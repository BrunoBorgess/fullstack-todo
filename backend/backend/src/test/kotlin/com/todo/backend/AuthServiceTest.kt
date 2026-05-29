package com.todo.backend

import com.todo.backend.dto.LoginRequest
import com.todo.backend.dto.RegisterRequest
import com.todo.backend.model.User
import com.todo.backend.repository.UserRepository
import com.todo.backend.security.JwtService
import com.todo.backend.service.AuthService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional

class AuthServiceTest {

    private val userRepository = mock(UserRepository::class.java)
    private val passwordEncoder = mock(PasswordEncoder::class.java)
    private val jwtService = mock(JwtService::class.java)
    private val authenticationManager = mock(AuthenticationManager::class.java)

    private val authService = AuthService(
        userRepository, passwordEncoder, jwtService, authenticationManager
    )

    @Test
    fun `register should create user and return token`() {
        val request = RegisterRequest("john", "john@email.com", "123456")

        `when`(userRepository.existsByUsername("john")).thenReturn(false)
        `when`(userRepository.existsByEmail("john@email.com")).thenReturn(false)
        `when`(passwordEncoder.encode("123456")).thenReturn("encoded")
        `when`(userRepository.save(any())).thenReturn(User(1, "john", "encoded", "john@email.com"))
        `when`(jwtService.generateToken("john")).thenReturn("token123")

        val result = authService.register(request)

        assertEquals("token123", result.token)
        assertEquals("john", result.username)
    }

    @Test
    fun `register should throw when username already exists`() {
        val request = RegisterRequest("john", "john@email.com", "123456")
        `when`(userRepository.existsByUsername("john")).thenReturn(true)

        assertThrows<IllegalArgumentException> {
            authService.register(request)
        }
    }

    @Test
    fun `login should return token when credentials are valid`() {
        val request = LoginRequest("john", "123456")
        val user = User(1, "john", "encoded", "john@email.com")

        `when`(authenticationManager.authenticate(any())).thenReturn(
            UsernamePasswordAuthenticationToken("john", "123456")
        )
        `when`(userRepository.findByUsername("john")).thenReturn(Optional.of(user))
        `when`(jwtService.generateToken("john")).thenReturn("token123")

        val result = authService.login(request)

        assertEquals("token123", result.token)
        assertEquals("john", result.username)
    }
}