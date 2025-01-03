package br.com.actionlabs.carboncalc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StartCalcRequestDTO {

  @NotBlank
  private String name;
  @NotBlank
  @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
  private String email;
  @NotBlank
  @Pattern(regexp = "^[A-Z]{2}$")
  private String uf;
  @NotBlank
  private String phoneNumber;

}
