package br.com.actionlabs.carboncalc.repository;

import br.com.actionlabs.carboncalc.enums.TransportationType;
import br.com.actionlabs.carboncalc.model.TransportationEmissionFactor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportationEmissionFactorRepository
    extends MongoRepository<TransportationEmissionFactor, TransportationType> {

    Optional<TransportationEmissionFactor> findByType(TransportationType type);

}
