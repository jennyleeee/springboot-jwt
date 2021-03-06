package me.jennyleeee.tutorial.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import me.jennyleeee.tutorial.entity.Authority
import me.jennyleeee.tutorial.entity.User
import org.slf4j.LoggerFactory
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


@Component
class TokenProvider(
  @Value("\${jwt.secret}") private val _secret: String,
  @Value("\${jwt.token-validity-in-seconds}") private val _tokenValidityInSeconds: Long
) : InitializingBean {
  
  lateinit var key: Key
  
  val secret = _secret
  val tokenValidityInSeconds = _tokenValidityInSeconds * 1000
  
  private val logger = LoggerFactory.getLogger(TokenProvider::class.java)
  
  fun createToken(authentication: Authentication): String {
    val authorities =
      authentication.authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","))
    val now: Long = Date().time
    val validity = Date(now + this.tokenValidityInSeconds)
    
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
    
    val authorities: Collection<GrantedAuthority> = Arrays.stream(
      claims[AUTHORITIES_KEY]
        .toString()
        .split(",")
        .toTypedArray()
    ).map(::SimpleGrantedAuthority).collect(Collectors.toList()).toSet()
    
    val principal = User(
      userName = claims.subject,
      nickName = "",
      authorities = authorities as Set<Authority>
    )
    
    return UsernamePasswordAuthenticationToken(principal, token, authorities)
  }
  
  
  fun validateToken(token: String): Boolean {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
      return true
    } catch (e: SecurityException) {
      logger.info("????????? JWT ???????????????.")
    } catch (e: MalformedJwtException) {
      logger.info("????????? JWT ???????????????.")
    } catch (e: ExpiredJwtException) {
      logger.info("????????? JWT ???????????????.")
    } catch (e: UnsupportedJwtException) {
      logger.info("???????????? ?????? JWT ???????????????.")
    } catch (e: IllegalArgumentException) {
      logger.info("JWT ????????? ?????????????????????.")
    }
    return false
  }
  
  override fun afterPropertiesSet() {
    /**
     * bean ???????????? ????????? ???????????? ?????? ????????? ???????????? secret ?????? base64 decode ??????,
     * key ????????? ???????????? ??????
     */
    val keyBytes = Decoders.BASE64.decode(secret)
    key = Keys.hmacShaKeyFor(keyBytes)
  }
  
  companion object {
    const val AUTHORITIES_KEY = "auth"
  }
}

