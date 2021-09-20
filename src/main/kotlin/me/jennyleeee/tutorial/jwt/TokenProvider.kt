package me.jennyleeee.tutorial.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import me.jennyleeee.tutorial.entity.User
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors
import org.slf4j.*;


@Component
class TokenProvider(
  @Value("\${jwt.secret}") _secret:String,
  @Value("\${jwt.token-validity-in-seconds}") _tokenValidityInSeconds: Long
): InitializingBean {
  
  private val AUTHORITIES_KEY = "auth"
  private var key: Key ?= null
  val secret = _secret
  val tokenValidityInSeconds = _tokenValidityInSeconds * 1000
  private val logger = LoggerFactory.getLogger(TokenProvider::class.java)
  
  fun createToken(authentication: Authentication):String {
    val authorities = authentication.authorities.stream().map (GrantedAuthority::getAuthority).collect(Collectors.joining(","))
    val now: Long = Date().time
    val validity: Date = Date(now + this.tokenValidityInSeconds)
    
    return Jwts.builder()
      .setSubject(authentication.name)
      .claim(AUTHORITIES_KEY, authorities)
      .signWith(key, SignatureAlgorithm.HS512)
      .setExpiration(validity)
      .compact()
  }
  
  // TODO User
  fun getAuthentication(token: String): Authentication {
    val claims: Claims = Jwts
      .parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token)
      .body
    
    val authorities: Collection<GrantedAuthority> = Arrays.stream(claims[AUTHORITIES_KEY]
        .toString().split(",").toTypedArray())
        .map { SimpleGrantedAuthority(it) }
        .collect(Collectors.toList())


    val principal = User(claims.subject,"",authorities)
    return UsernamePasswordAuthenticationToken(principal, token, authorities)
  }

  
  fun validateToken(token: String): Boolean {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
      return true
    } catch (e: SecurityException) {
      logger.info("잘못된 JWT 서명입니다.")
    } catch (e: MalformedJwtException) {
      logger.info("잘못된 JWT 서명입니다.")
    } catch (e: ExpiredJwtException) {
      logger.info("만료된 JWT 토큰입니다.")
    } catch (e: UnsupportedJwtException) {
      logger.info("지원되지 않는 JWT 토큰입니다.")
    } catch (e: IllegalArgumentException) {
      logger.info("JWT 토큰이 잘못되었습니다.")
    }
    return false
  }
  
  override fun afterPropertiesSet() {
    /**
     * bean 생성되고 의존성 주입까지 받은 다음에 주입받은 secret 값을 base64 decode 후에,
     * key 변수에 할당하기 위함
     */
    val keyBytes = Decoders.BASE64.decode(secret)
    this.key = Keys.hmacShaKeyFor(keyBytes)
  }
}

