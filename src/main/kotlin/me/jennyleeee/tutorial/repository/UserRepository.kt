package me.jennyleeee.tutorial.repository

import me.jennyleeee.tutorial.entity.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User,Long> {
  
  @EntityGraph(attributePaths = ["authorities"]) // lazy 조회 아니고 eager 조회
  fun findOneWithAuthoritiesByUserName(name: String): Optional<User>
}