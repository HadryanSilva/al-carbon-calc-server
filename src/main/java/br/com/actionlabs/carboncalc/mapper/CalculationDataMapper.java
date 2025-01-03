package br.com.actionlabs.carboncalc.mapper;

import br.com.actionlabs.carboncalc.dto.CarbonCalculationResultDTO;
import br.com.actionlabs.carboncalc.dto.StartCalcRequestDTO;
import br.com.actionlabs.carboncalc.model.CalculationData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CalculationDataMapper {

    CalculationData requestToModel(StartCalcRequestDTO request);

    CarbonCalculationResultDTO modelToResponse(CalculationData model);

}
