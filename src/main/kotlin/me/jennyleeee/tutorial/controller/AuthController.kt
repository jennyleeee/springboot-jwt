package me.jennyleeee.tutorial.controller

import me.jennyleeee.tutorial.dto.LoginDto
import me.jennyleeee.tutorial.dto.TokenDto
import me.jennyleeee.tutorial.jwt.JwtFilter
import me.jennyleeee.tutorial.jwt.TokenProvider
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@RequestMapping("/api")
class AuthController (val tokenProvider: TokenProvider, val authenticationManagerBuilder: AuthenticationManagerBuilder){

  @PostMapping("/authenticate")
  fun authorize(@Valid @RequestBody loginDto: LoginDto): ResponseEntity<TokenDto> {
    val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.username, loginDto.password)
    val authentication: Authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
    val httpHeaders = HttpHeaders()
    SecurityContextHolder.getContext().authentication = authentication
    
    val jwt:String = tokenProvider.createToken(authentication)
    httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer $jwt")
  
    return ResponseEntity(TokenDto(jwt), httpHeaders, HttpStatus.OK)
  }
  
}