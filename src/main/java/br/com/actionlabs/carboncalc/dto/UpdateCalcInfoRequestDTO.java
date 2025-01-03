package br.com.actionlabs.carboncalc.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCalcInfoRequestDTO {

  @NotBlank
  private String id;
  @NotBlank
  private int energyConsumption;
  @NotNull
  private List<TransportationDTO> transportation;
  @NotBlank
  private int solidWasteTotal;
  @NotBlank
  @DecimalMin("0.0")
  @DecimalMax("1.0")
  private double recyclePercentage;

}
