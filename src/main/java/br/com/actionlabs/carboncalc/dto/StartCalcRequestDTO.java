package br.com.actionlabs.carboncalc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StartCalcRequestDTO {
  @NotBlank(message = "Name cannot be empty")
  private String name;

  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Email format is invalid")
  private String email;

  @NotBlank(message = "The state cannot be empty")
  @Size(min = 2, max = 2, message = "The UF must be exactly 2 characters")
  private String uf;

  @NotBlank(message = "Phone number cannot be blank")
  private String phoneNumber;
}
