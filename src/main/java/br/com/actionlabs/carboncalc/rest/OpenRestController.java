package br.com.actionlabs.carboncalc.rest;

import br.com.actionlabs.carboncalc.dto.*;
import br.com.actionlabs.carboncalc.service.EmissionCalculatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open")
@RequiredArgsConstructor
@Slf4j
public class OpenRestController {

  private final EmissionCalculatorService emissionCalculatorService;

  @PostMapping("start-calc")
  public ResponseEntity<StartCalcResponseDTO> startCalculation(
      @Valid @RequestBody StartCalcRequestDTO request) {
    var response = emissionCalculatorService.startCalc(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("info")
  public ResponseEntity<UpdateCalcInfoResponseDTO> updateInfo(
      @Valid @RequestBody UpdateCalcInfoRequestDTO request) {
    var response = emissionCalculatorService.calculate(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("result/{id}")
  public ResponseEntity<CarbonCalculationResultDTO> getResult(@PathVariable String id) {
    var response = emissionCalculatorService.getTotalEmissions(id);
    return ResponseEntity.ok(response);
  }
}
