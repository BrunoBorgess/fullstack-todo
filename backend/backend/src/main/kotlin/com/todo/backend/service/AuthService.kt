package com.todo.backend.service

import com.todo.backend.dto.AuthResponse
import com.todo.backend.dto.LoginRequest
import com.todo.backend.dto.RegisterRequest
import com.todo.backend.model.User
import com.todo.backend.repository.UserRepository
import com.todo.backend.security.JwtService
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    @Lazy private val authenticationManager: AuthenticationManager
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("Usuário não encontrado: $username") }

        return org.springframework.security.core.userdetails.User
            .withUsername(user.username)
            .password(user.password)
            .roles("USER")
            .build()
    }

    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException("Username já existe")
        }
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email já cadastrado")
        }

        val user = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password)
        )

        userRepository.save(user)
        val token = jwtService.generateToken(user.username)
        return AuthResponse(token = token, username = user.username)
    }

    fun login(request: LoginRequest): AuthResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )

        val user = userRepository.findByUsername(request.username)
            .orElseThrow { UsernameNotFoundException("Usuário não encontrado") }

        val token = jwtService.generateToken(user.username)
        return AuthResponse(token = token, username = user.username)
    }
}