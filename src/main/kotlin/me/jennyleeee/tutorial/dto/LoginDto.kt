package me.jennyleeee.tutorial.dto

import lombok.Getter
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Getter
data class LoginDto (
  
  @NotNull
  @Size(min=3, max = 50)
  val username:String,
  
  @NotNull
  @Size(min=3, max = 100)
  val password:String
)