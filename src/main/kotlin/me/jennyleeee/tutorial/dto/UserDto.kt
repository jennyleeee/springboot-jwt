package me.jennyleeee.tutorial.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UserDto (
  @NotNull
  @Size(min=3, max = 50)
  private val username:String,

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotNull
  @Size(min=3, max = 100)
  private val password:String,

  @NotNull
  @Size(min=3, max = 100)
  private val nickname:String
 )