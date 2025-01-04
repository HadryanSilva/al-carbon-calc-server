package br.com.actionlabs.carboncalc.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCalcInfoRequestDTO {

  @NotBlank(message = "Id is mandatory")
  private String id;
  @NotNull(message = "Name is mandatory")
  private int energyConsumption;
  @NotNull(message = "Transportation is mandatory")
  private List<TransportationDTO> transportation;
  @NotNull(message = "Solid waste total is mandatory")
  private int solidWasteTotal;
  @NotNull(message = "Solid waste recyclable is mandatory")
  @DecimalMin(value = "0.0", message = "Recycle percentage must be between 0 and 1")
  @DecimalMax(value = "1.0", message = "Recycle percentage must be between 0 and 1")
  private double recyclePercentage;

}
