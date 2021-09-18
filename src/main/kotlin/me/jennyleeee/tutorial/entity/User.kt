package me.jennyleeee.tutorial.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.*
import javax.persistence.*

// TODO: 실무에서는 lombok 기능을 주의해서 사용해야 함
@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
data class User(
  
  @JsonIgnore
  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val userId: Long,

  @Column(name = "username", length = 50, unique = true)
  val userName: String,
  
  @Column(name = "password", length = 100)
  val password: String,

  @Column(name = "nickname", length = 50)
  val nickName: String,

  @JsonIgnore
  @Column( name="activated")
  val activated: Boolean,
  
  @ManyToMany
  @JoinTable(
    name = "user_authority",
    joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")],
    inverseJoinColumns = [JoinColumn(name="authority_name", referencedColumnName = "authority_name")]
  )
  val authorities: Set<Authority>
  
)