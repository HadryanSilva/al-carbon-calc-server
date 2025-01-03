package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.TransportationDTO;
import br.com.actionlabs.carboncalc.dto.UpdateCalcInfoRequestDTO;
import br.com.actionlabs.carboncalc.enums.TransportationType;
import br.com.actionlabs.carboncalc.exception.ResourceNotFoundException;
import br.com.actionlabs.carboncalc.model.TransportationEmissionFactor;
import br.com.actionlabs.carboncalc.repository.TransportationEmissionFactorRepository;
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
class TransportationCalculatorServiceTest {

    @InjectMocks
    private TransportationCalculatorService transportationCalculatorService;

    @Mock
    private TransportationEmissionFactorRepository transportationEmissionFactorRepository;

    @Test
    @DisplayName("Should calculate transportation emission successfully")
    void testCalculateTransportationEmissionSuccessfully() {
        var carFactor = 0.19;
        var motorcycleFactor = 0.09;
        var publicTransportFactor = 0.04;
        var bicycleFactor = 0.0;

        Mockito.when(transportationEmissionFactorRepository.findByType(TransportationType.CAR))
                .thenReturn(Optional.of(new TransportationEmissionFactor(TransportationType.CAR, carFactor)));
        Mockito.when(transportationEmissionFactorRepository.findByType(TransportationType.PUBLIC_TRANSPORT))
                .thenReturn(Optional.of(new TransportationEmissionFactor(TransportationType.PUBLIC_TRANSPORT, publicTransportFactor)));
        Mockito.when(transportationEmissionFactorRepository.findByType(TransportationType.MOTORCYCLE))
                .thenReturn(Optional.of(new TransportationEmissionFactor(TransportationType.MOTORCYCLE, motorcycleFactor)));
        Mockito.when(transportationEmissionFactorRepository.findByType(TransportationType.BICYCLE))
                .thenReturn(Optional.of(new TransportationEmissionFactor(TransportationType.BICYCLE, bicycleFactor)));

        var dto = new UpdateCalcInfoRequestDTO();
        dto.setTransportation(getTransportationDTOList());
        var result = transportationCalculatorService.calculateTransportationEmission(dto);
        Assertions.assertThat(result).isEqualTo(71);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when transportation emission factor is not found")
    void testCalculateTransportationEmissionResourceNotFoundException() {
        var dto = new UpdateCalcInfoRequestDTO();
        var transportationDTO = new TransportationDTO();
        transportationDTO.setType(null);
        transportationDTO.setMonthlyDistance(300);
        dto.setTransportation(List.of(transportationDTO));
        Mockito.when(transportationEmissionFactorRepository.findByType(null)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> transportationCalculatorService.calculateTransportationEmission(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("404 NOT_FOUND \"Transportation factor not found for type: " + null + "\"");
    }

    private List<TransportationDTO> getTransportationDTOList() {
        var transportationDTO = new TransportationDTO();
        transportationDTO.setType(TransportationType.CAR);
        transportationDTO.setMonthlyDistance(300);

        var transportationDTO2 = new TransportationDTO();
        transportationDTO2.setType(TransportationType.PUBLIC_TRANSPORT);
        transportationDTO2.setMonthlyDistance(250);

        var transportationDTO3 = new TransportationDTO();
        transportationDTO3.setType(TransportationType.MOTORCYCLE);
        transportationDTO3.setMonthlyDistance(50);

        var transportationDTO4 = new TransportationDTO();
        transportationDTO4.setType(TransportationType.BICYCLE);
        transportationDTO4.setMonthlyDistance(25);

        return List.of(transportationDTO, transportationDTO2, transportationDTO3, transportationDTO4);
    }

}