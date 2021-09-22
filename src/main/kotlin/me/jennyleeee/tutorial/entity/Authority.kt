package me.jennyleeee.tutorial.entity

import lombok.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "authority")
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
