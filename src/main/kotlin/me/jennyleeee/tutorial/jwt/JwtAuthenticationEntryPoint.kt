package me.jennyleeee.tutorial.jwt

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *  유효한 자격증명을 제공하지 않고 접근하려고 할 때 제어
 */
@Component
class JwtAuthenticationEntryPoint: AuthenticationEntryPoint {
  
  override fun commence(
    request: HttpServletRequest?,
    response: HttpServletResponse?,
    authException: AuthenticationException?
  ) {
    response?.sendError(HttpServletResponse.SC_UNAUTHORIZED) // 401 ERROR
  }
}