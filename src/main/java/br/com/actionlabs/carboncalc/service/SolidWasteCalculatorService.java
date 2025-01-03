package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.UpdateCalcInfoRequestDTO;
import br.com.actionlabs.carboncalc.exception.ResourceNotFoundException;
import br.com.actionlabs.carboncalc.model.SolidWasteEmissionFactor;
import br.com.actionlabs.carboncalc.repository.SolidWasteEmissionFactorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class SolidWasteCalculatorService {

    private final SolidWasteEmissionFactorRepository solidWasteEmissionFactorRepository;

    public Integer calculateSolidWasteEmission(String uf, UpdateCalcInfoRequestDTO dto) {
        log.info("Calculating solid waste emission for UF: {}", uf);
        var factor = solidWasteEmissionFactorRepository.findByUf(uf)
                .orElseThrow(() -> new ResourceNotFoundException("Solid waste emission factor not found for uf: " + uf));
        return (int) (calculateRecyclableWasteEmission(factor, dto) +
                calculateNonRecyclableWasteEmission(factor, dto));
    }

    private Double calculateRecyclableWasteEmission(SolidWasteEmissionFactor factors, UpdateCalcInfoRequestDTO dto) {
        log.info("Calculating recyclable waste emission");
        var recyclableSolidWaste = dto.getSolidWasteTotal() * dto.getRecyclePercentage();
        return (recyclableSolidWaste * factors.getRecyclableFactor());
    }

    private Double calculateNonRecyclableWasteEmission(SolidWasteEmissionFactor factors, UpdateCalcInfoRequestDTO dto) {
        log.info("Calculating non-recyclable waste emission");
        var nonRecyclableSolidWaste = dto.getSolidWasteTotal() * (1 - dto.getRecyclePercentage());
        return (nonRecyclableSolidWaste * factors.getNonRecyclableFactor());
    }

}
