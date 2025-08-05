package br.com.actionlabs.carboncalc.dto;

import br.com.actionlabs.carboncalc.enums.TransportationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransportationDTO {
  @NotNull(message = "Transportation type is required")
  private TransportationType type;

  @Min(value = 0, message = "Monthly distance must be zero or positive")
  private int monthlyDistance;
}
