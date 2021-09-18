package me.jennyleeee.tutorial.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.*
import javax.persistence.*

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
data class Authority (
  
  @Id
  @Column(name = "authority_name", length = 50)
  val authorityName: String
 
)
