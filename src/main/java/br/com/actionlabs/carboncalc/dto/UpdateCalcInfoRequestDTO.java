package br.com.actionlabs.carboncalc.dto;

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
  private double recyclePercentage;

}
