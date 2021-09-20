package me.jennyleeee.tutorial.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter(){
  
  override fun configure(web: WebSecurity) {
    web
      .ignoring()
      .antMatchers("/h2-console/**", "/favicon.ico")
  }
  
  override fun configure(http: HttpSecurity) {
    http
      .authorizeRequests() // http servlet request를 사용하는 것에 대한 요청 접근 제한을 사용하겠다
      .antMatchers("/api/hello").permitAll() // "/api/hello 에 대한 요청은 제한 없이 허용함
      .anyRequest().authenticated() // 나머지는 모두 인증 받아야 한다
  }
}