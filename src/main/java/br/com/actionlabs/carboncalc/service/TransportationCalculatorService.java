package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.UpdateCalcInfoRequestDTO;
import br.com.actionlabs.carboncalc.exception.ResourceNotFoundException;
import br.com.actionlabs.carboncalc.model.TransportationEmissionFactor;
import br.com.actionlabs.carboncalc.repository.TransportationEmissionFactorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class TransportationCalculatorService {

    private final TransportationEmissionFactorRepository transportationEmissionFactorRepository;

    public Integer calculateTransportationEmission(UpdateCalcInfoRequestDTO dto) {
        var totalEmission = 0.0;

        log.info("Calculating transportation emission");
        return dto.getTransportation().stream()
            .map(transport -> {
                TransportationEmissionFactor factor = transportationEmissionFactorRepository
                        .findByType(transport.getType())
                        .orElseThrow(() -> new ResourceNotFoundException("Transportation factor not found for type: " +
                                transport.getType()));
                return transport.getMonthlyDistance() * factor.getFactor();
            })
            .reduce(totalEmission, Double::sum)
            .intValue();
    }

}
