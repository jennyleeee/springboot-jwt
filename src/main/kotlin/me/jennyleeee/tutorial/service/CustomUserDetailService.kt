package me.jennyleeee.tutorial.service

import me.jennyleeee.tutorial.entity.User
import me.jennyleeee.tutorial.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors


@Component("userDetailsService")
class CustomUserDetailService: UserDetailsService {
  
  @Autowired
  lateinit var userRepository: UserRepository
  
  @Transactional
  override fun loadUserByUsername(username: String): UserDetails {
    return userRepository.findOneWithAuthoritiesByUserName(username)
      .map { user -> createUser(username, user) }
      .orElseThrow { UsernameNotFoundException("$username -> 데이터베이스에서 찾을 수 없습니다.") }
  }
  
  
  fun createUser(username: String, user: User): org.springframework.security.core.userdetails.User {
    if(!user.activated) {
      throw RuntimeException("$username -> 활성화되어 있지 않습니다.")
    }
    val grantedAuthorities: List<GrantedAuthority> =
      user.authorities.stream().map { SimpleGrantedAuthority(it.authorityName) }.collect(Collectors.toList())
    return org.springframework.security.core.userdetails.User(user.userName, user.password, grantedAuthorities)
  }

//  fun loadUserByUserName(username: String): UserDetails {
//
//  }
}