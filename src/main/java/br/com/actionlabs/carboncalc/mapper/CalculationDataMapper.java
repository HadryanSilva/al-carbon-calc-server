package br.com.actionlabs.carboncalc.mapper;

import br.com.actionlabs.carboncalc.dto.CarbonCalculationResultDTO;
import br.com.actionlabs.carboncalc.dto.StartCalcRequestDTO;
import br.com.actionlabs.carboncalc.model.CalculationData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CalculationDataMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "energy", ignore = true)
    @Mapping(target = "transportation", ignore = true)
    @Mapping(target = "solidWaste", ignore = true)
    CalculationData requestToModel(StartCalcRequestDTO request);

    @Mapping(target = "total", ignore = true)
    CarbonCalculationResultDTO modelToResponse(CalculationData model);

}
