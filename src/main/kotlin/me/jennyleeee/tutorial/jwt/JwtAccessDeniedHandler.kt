package me.jennyleeee.tutorial.jwt

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *  필요한 권한이 존재하지 않은 경우에 제어
 */
@Component
class JwtAccessDeniedHandler: AccessDeniedHandler {
  override fun handle(
    request: HttpServletRequest?,
    response: HttpServletResponse?,
    accessDeniedException: AccessDeniedException?
  ) {
    response?.sendError(HttpServletResponse.SC_FORBIDDEN) // 403 ERROR
  }
  
}