package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.UpdateCalcInfoRequestDTO;
import br.com.actionlabs.carboncalc.exception.ResourceNotFoundException;
import br.com.actionlabs.carboncalc.model.SolidWasteEmissionFactor;
import br.com.actionlabs.carboncalc.repository.SolidWasteEmissionFactorRepository;
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
class SolidWasteCalculatorServiceTest {

    private static final String UF = "GO";

    @InjectMocks
    private SolidWasteCalculatorService solidWasteCalculatorService;

    @Mock
    private SolidWasteEmissionFactorRepository solidWasteEmissionFactorRepository;

    @BeforeEach
    void init() {
        var solidWasteEmissionFactor = new SolidWasteEmissionFactor();
        solidWasteEmissionFactor.setRecyclableFactor(0.43);
        solidWasteEmissionFactor.setNonRecyclableFactor(0.94);
        Mockito.when(solidWasteEmissionFactorRepository.findByUf(UF)).thenReturn(Optional.of(solidWasteEmissionFactor));
    }

    @Test
    @DisplayName("Should calculate solid waste emission successfully")
    void testCalculateSolidWasteEmissionSuccessfully() {
        var dto = new UpdateCalcInfoRequestDTO();
        dto.setSolidWasteTotal(250);
        dto.setRecyclePercentage(0.5);
        var result = solidWasteCalculatorService.calculateSolidWasteEmission(UF, dto);
        Assertions.assertThat(result).isEqualTo(171);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when solid waste emission factor is not found")
    void testCalculateSolidWasteEmissionResourceNotFoundException() {
        Mockito.when(solidWasteEmissionFactorRepository.findByUf(UF)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> solidWasteCalculatorService.calculateSolidWasteEmission(UF, null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("404 NOT_FOUND \"Solid waste emission factor not found for uf: " + UF + "\"");
    }
}