package br.com.actionlabs.carboncalc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StartCalcRequestDTO {

  @NotBlank(message = "Name is mandatory")
  private String name;
  @NotBlank(message = "Email is mandatory")
  @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email")
  private String email;
  @NotBlank(message = "UF is mandatory")
  @Pattern(regexp = "^[A-Z]{2}$", message = "Invalid UF")
  private String uf;
  @NotBlank(message = "Phone number is mandatory")
  private String phoneNumber;

}
