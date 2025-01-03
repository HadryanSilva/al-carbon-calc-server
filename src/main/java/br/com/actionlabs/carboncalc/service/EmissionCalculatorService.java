package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.*;
import br.com.actionlabs.carboncalc.exception.CalculationException;
import br.com.actionlabs.carboncalc.exception.ResourceNotFoundException;
import br.com.actionlabs.carboncalc.mapper.CalculationDataMapper;
import br.com.actionlabs.carboncalc.model.CalculationData;
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
            var calculationData = checkIfCalculationDataExists(request.getId());
            updateCalculationData(calculationData, request);
            saveCalculationData(calculationData);
            response.setSuccess(true);
        } catch (ResourceNotFoundException e) {
            log.error("Error updating calculation data: {}", e.getMessage());
            response.setSuccess(false);
        } catch (CalculationException e) {
            log.error("Error calculating emissions: {}", e.getMessage());
            response.setSuccess(false);
        }
        return response;
    }

    public CarbonCalculationResultDTO getTotalEmissions(String id) {
        var calculationData = checkIfCalculationDataExists(id);

        var total = calculationData.getEnergy() +
                calculationData.getSolidWaste() +
                calculationData.getTransportation();

        var response = calculationDataMapper.modelToResponse(calculationData);
        response.setTotal(total);

        return response;
    }

    private CalculationData checkIfCalculationDataExists(String id) {
        return calculationDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calculation data not found for id: " + id));
    }

    private void updateCalculationData(CalculationData data, UpdateCalcInfoRequestDTO request) {
        data.setEnergy(energyCalculatorService.calculateEnergyEmission(data.getUf(), request));
        data.setSolidWaste(solidWasteCalculatorService.calculateSolidWasteEmission(data.getUf(), request));
        data.setTransportation(transportationCalculatorService.calculateTransportationEmission(request));
    }

    private void saveCalculationData(CalculationData calculationData) {
        calculationDataRepository.save(calculationData);
        log.info("Calculation data successfully updated");
    }

}
