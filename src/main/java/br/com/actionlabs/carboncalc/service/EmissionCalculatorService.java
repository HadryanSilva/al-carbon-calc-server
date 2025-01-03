package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.*;
import br.com.actionlabs.carboncalc.exception.ResourceNotFoundException;
import br.com.actionlabs.carboncalc.mapper.CalculationDataMapper;
import br.com.actionlabs.carboncalc.repository.CalculationDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmissionCalculatorService {

    private final CalculationDataRepository calculationDataRepository;
    private final CalculationDataMapper calculationDataMapper;

    private final EnergyCalculatorService energyCalculatorService;
    private final SolidWasteCalculatorService solidWasteCalculatorService;
    private final TransportationCalculatorService transportationCalculatorService;

    public StartCalcResponseDTO startCalc(StartCalcRequestDTO request) {
        log.info("Saving initial user data");
        var response = new StartCalcResponseDTO();
        var calculationData = calculationDataMapper.requestToModel(request);
        var savedCalculationData = calculationDataRepository.save(calculationData);
        log.info("Saved initial user data with id: {}", savedCalculationData.getId());
        response.setId(savedCalculationData.getId());
        return response;
    }

    public UpdateCalcInfoResponseDTO calculate(UpdateCalcInfoRequestDTO request) {
        var response = new UpdateCalcInfoResponseDTO();
        try {
            var calculationData = calculationDataRepository.findById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Calculation data not found for id: " +
                            request.getId()));

            calculationData.setEnergy(energyCalculatorService
                    .calculateEnergyEmission(calculationData.getUf(), request));

            calculationData.setSolidWaste(solidWasteCalculatorService
                    .calculateSolidWasteEmission(calculationData.getUf(), request));

            calculationData.setTransportation(transportationCalculatorService
                    .calculateTransportationEmission(request));

            calculationDataRepository.save(calculationData);
            log.info("Calculation data successfully updated");
            response.setSuccess(true);
        } catch (RuntimeException e) {
            log.error("Error calculating emissions: {}", e.getMessage());
            response.setSuccess(false);
        }
        return response;
    }

    public CarbonCalculationResultDTO getTotalEmissions(String id) {
        var calculationData = calculationDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calculation data not found for id: " + id));

        var total = calculationData.getEnergy() +
                calculationData.getSolidWaste() +
                calculationData.getTransportation();

        var response = calculationDataMapper.modelToResponse(calculationData);
        response.setTotal(total);

        return response;
    }

}
