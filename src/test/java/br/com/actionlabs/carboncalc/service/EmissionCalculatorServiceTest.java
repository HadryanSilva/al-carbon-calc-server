package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.*;
import br.com.actionlabs.carboncalc.enums.TransportationType;
import br.com.actionlabs.carboncalc.mapper.CalculationDataMapper;
import br.com.actionlabs.carboncalc.model.CalculationData;
import br.com.actionlabs.carboncalc.repository.CalculationDataRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EmissionCalculatorServiceTest {

    @InjectMocks
    private EmissionCalculatorService emissionCalculatorService;
    @Mock
    private CalculationDataMapper calculationDataMapper;
    @Mock
    private CalculationDataRepository calculationDataRepository;

    @Test
    void testStartCalcSuccessfully() {
        StartCalcRequestDTO requestDTO = new StartCalcRequestDTO();
        CalculationData calculationData = new CalculationData();
        CalculationData savedCalculationData = new CalculationData();
        savedCalculationData.setId("6777098c112b4802c17e318c");

        Mockito.when(calculationDataMapper.requestToModel(requestDTO)).thenReturn(calculationData);
        Mockito.when(calculationDataRepository.save(calculationData)).thenReturn(savedCalculationData);

        var actualResponse = emissionCalculatorService.startCalc(requestDTO);

        Mockito.verify(calculationDataMapper).requestToModel(requestDTO);
        Mockito.verify(calculationDataRepository).save(calculationData);

        var expectedResponse = new StartCalcResponseDTO();
        expectedResponse.setId("6777098c112b4802c17e318c");
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should calculate total emission successfully")
    void testCalculateTotalEmissionsSuccessfully() {
        var id = "6777098c112b4802c17e318c";
        var calculationData = new CalculationData();
        calculationData.setEnergy(100);
        calculationData.setSolidWaste(50);
        calculationData.setTransportation(70);

        var expectedResponse = new CarbonCalculationResultDTO();
        expectedResponse.setEnergy(100);
        expectedResponse.setSolidWaste(50);
        expectedResponse.setTransportation(70);

        Mockito.when(calculationDataRepository.findById(id)).thenReturn(Optional.of(calculationData));
        Mockito.when(calculationDataMapper.modelToResponse(calculationData)).thenReturn(expectedResponse);

        var actualResponse = emissionCalculatorService.getTotalEmissions(id);

        expectedResponse.setTotal(220);
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    private List<TransportationDTO> getTransportationDTOList() {
        var transportationDTO = new TransportationDTO();
        transportationDTO.setType(TransportationType.CAR);
        transportationDTO.setMonthlyDistance(300);

        return List.of(transportationDTO);
    }

}