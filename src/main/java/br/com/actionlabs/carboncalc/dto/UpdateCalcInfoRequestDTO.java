package br.com.actionlabs.carboncalc.dto;

import lombok.Data;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Data
public class UpdateCalcInfoRequestDTO {
  @NotBlank(message = "ID is required")
  private String id;

  @Min(value = 0, message = "Energy consumption must be zero or positive")
  private int energyConsumption;

  @NotNull(message = "Transportation list is required")
  @Size(min = 1, message = "At least one transportation entry is required")
  @Valid
  private List<TransportationDTO> transportation;

  @Min(value = 0, message = "Solid waste must be zero or positive")
  private int solidWasteTotal;

  @DecimalMin(value = "0.0", inclusive = true, message = "Recycle percentage must be at least 0.0")
  @DecimalMax(value = "1.0", inclusive = true, message = "Recycle percentage must be at most 1.0")
  private double recyclePercentage;
}
