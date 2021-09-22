package me.jennyleeee.tutorial.jwt

import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


class JwtFilter(private val tokenProvider: TokenProvider): GenericFilterBean() {
  
  private val logger = LoggerFactory.getLogger(JwtFilter::class.java)
  private val AUTHORIZATION_HEADER = "Authorization"
  
  @Throws(IOException::class, ServletException::class)
  override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
    /**
     * 실제 필터링 로직 여기에
     * 토큰의 인증정보를 SecurityContext에 저장하는 역할 수행
     */
    
    val httpServletRequest: HttpServletRequest = request as HttpServletRequest
    val jwt: String? = resolveToken(httpServletRequest)
    val requestURI: String = httpServletRequest.requestURI
    
    if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt!!)) {
      val authentication: Authentication = tokenProvider.getAuthentication(jwt)
      SecurityContextHolder.getContext().authentication = authentication
//      logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.name, requestURI)
    } else {
//      logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI)
    }
    chain?.doFilter(request, response)
  }
  
  private fun resolveToken(httpServletRequest: HttpServletRequest): String? {
    val bearerToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER)
    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7)
    }
    return null
  }
}
