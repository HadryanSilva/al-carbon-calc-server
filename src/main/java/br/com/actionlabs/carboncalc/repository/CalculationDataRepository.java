package br.com.actionlabs.carboncalc.repository;

import br.com.actionlabs.carboncalc.model.CalculationData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationDataRepository extends MongoRepository<CalculationData, String> {
}
