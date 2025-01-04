package br.com.actionlabs.carboncalc.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

import static br.com.actionlabs.carboncalc.config.IntegrationTestContainers.MONGO_DB_CONTAINER;

@TestConfiguration(proxyBeanMethods = false)
@Import(IntegrationTestContainers.class)
public class DefaultTestContainersConfiguration {

    @Bean
    public MongoDBContainer mongoDBContainer() {
        return MONGO_DB_CONTAINER;
    }

}
