package me.jennyleeee.tutorial.jwt

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * TokenProvider, JwtFilter 를 SecurityConfig 에 적용할 때 사용
 */
class JwtSecurityConfig(
  private val tokenProvider: TokenProvider
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
  
  override fun configure(http: HttpSecurity?) {
    val customFilter = JwtFilter(tokenProvider)
    http?.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
  }
}