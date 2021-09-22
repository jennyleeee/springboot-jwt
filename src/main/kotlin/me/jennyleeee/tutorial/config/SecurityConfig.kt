package me.jennyleeee.tutorial.config

import me.jennyleeee.tutorial.jwt.JwtAccessDeniedHandler
import me.jennyleeee.tutorial.jwt.JwtAuthenticationEntryPoint
import me.jennyleeee.tutorial.jwt.JwtSecurityConfig
import me.jennyleeee.tutorial.jwt.TokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션을 메소드 단위로 쓰기 위함
class SecurityConfig(private val tokenProvider: TokenProvider,
                     private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
                     private val jwtAccessDeniedHandler: JwtAccessDeniedHandler)
  : WebSecurityConfigurerAdapter() {
  
  @Bean
  fun passwordEncoder(): PasswordEncoder? {
    return BCryptPasswordEncoder()
  }
  
  override fun configure(web: WebSecurity) {
    web
      .ignoring()
      .antMatchers("/h2-console/**", "/favicon.ico")
  }
  
  override fun configure(http: HttpSecurity) {
    http
      .csrf().disable() // token 을 사용하기 때문에
     
      .exceptionHandling()
      .authenticationEntryPoint(jwtAuthenticationEntryPoint)
      .accessDeniedHandler(jwtAccessDeniedHandler)
     
      // h2 console 사용을 위함
      .and()
      .headers()
      .frameOptions()
      .sameOrigin()
     
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 은 사용하지 않겠다.
     
      .and()
      .authorizeRequests() // http servlet request를 사용하는 것에 대한 요청 접근 제한을 사용하겠다
      .antMatchers("/api/hello").permitAll() // "/api/hello 에 대한 요청은 제한 없이 허용함
      .antMatchers("/api/authenticate").permitAll() // 토큰이 없는 상태이므로 요청 허용
      .antMatchers("/api/signup").permitAll() // 토큰이 없는 상태이므로 요청 허용
      .anyRequest().authenticated() // 나머지는 모두 인증 받아야 한다
    
      .and()
      .apply(JwtSecurityConfig(tokenProvider))
  }
}