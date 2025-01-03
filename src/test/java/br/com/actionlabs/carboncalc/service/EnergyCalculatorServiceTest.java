package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.UpdateCalcInfoRequestDTO;
import br.com.actionlabs.carboncalc.exception.ResourceNotFoundException;
import br.com.actionlabs.carboncalc.model.EnergyEmissionFactor;
import br.com.actionlabs.carboncalc.repository.EnergyEmissionFactorRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EnergyCalculatorServiceTest {

    private static final String UF = "GO";

    @InjectMocks
    private EnergyCalculatorService energyCalculatorService;

    @Mock
    private EnergyEmissionFactorRepository energyEmissionFactorRepository;

    @BeforeEach
    void init() {
        var energyEmissionFactor = new EnergyEmissionFactor();
        energyEmissionFactor.setFactor(0.48);
        Mockito.when(energyEmissionFactorRepository.findByUf(UF)).thenReturn(Optional.of(energyEmissionFactor));
    }

    @Test
    @DisplayName("Should calculate energy emission successfully")
    void testCalculateEnergyEmissionSuccessfully() {
        var dto = new UpdateCalcInfoRequestDTO();
        dto.setEnergyConsumption(250);
        var result = energyCalculatorService.calculateEnergyEmission(UF, dto);
        Assertions.assertThat(result).isEqualTo(120);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when energy emission factor is not found")
    void testCalculateEnergyEmissionResourceNotFoundException() {
        var dto = new UpdateCalcInfoRequestDTO();
        dto.setEnergyConsumption(250);
        Mockito.when(energyEmissionFactorRepository.findByUf(UF)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> energyCalculatorService.calculateEnergyEmission(UF, dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("404 NOT_FOUND \"Energy emission factor not found for uf: " + UF + "\"");
    }

}