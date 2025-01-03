package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.UpdateCalcInfoRequestDTO;
import br.com.actionlabs.carboncalc.exception.ResourceNotFoundException;
import br.com.actionlabs.carboncalc.model.EnergyEmissionFactor;
import br.com.actionlabs.carboncalc.repository.EnergyEmissionFactorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class EnergyCalculatorService {

    private final EnergyEmissionFactorRepository energyEmissionFactorRepository;

    public Integer calculateEnergyEmission(String uf, UpdateCalcInfoRequestDTO dto) {
        log.info("Calculating energy emission for uf: {}", uf);
        var factor = energyEmissionFactorRepository.findByUf(uf)
                .map(EnergyEmissionFactor::getFactor)
                .orElseThrow(() -> new ResourceNotFoundException("Energy emission factor not found for uf: " + uf));
        double result = dto.getEnergyConsumption() * factor;
        return (int) result;
    }

}
