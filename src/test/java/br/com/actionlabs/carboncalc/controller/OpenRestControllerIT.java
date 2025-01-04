package br.com.actionlabs.carboncalc.controller;

import br.com.actionlabs.carboncalc.config.IntegrationTestContainers;
import br.com.actionlabs.carboncalc.dto.*;
import br.com.actionlabs.carboncalc.enums.TransportationType;
import br.com.actionlabs.carboncalc.utils.FileUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({IntegrationTestContainers.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OpenRestControllerIT {

    public static final String BASE_URL = "/open";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FileUtils fileUtils;

    private String id;

    @Test
    @Order(1)
    void testStartCalcSuccessfully() {
        var requestData = new StartCalcRequestDTO();
        requestData.setName("Teste");
        requestData.setEmail("teste@teste.com");
        requestData.setPhoneNumber("62999999999");
        requestData.setUf("GO");

        var entity = new HttpEntity<>(requestData);

        var response = restTemplate.postForEntity(BASE_URL + "/start-calc", entity, StartCalcResponseDTO.class);
        id = response.getBody().getId();
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotBlank();
    }

    @Test
    @Order(2)
    void testStartCalcThrowsBadRequest() throws IOException {
        var requestData = new StartCalcRequestDTO();
        requestData.setName("");
        requestData.setEmail("teste@teste");
        requestData.setPhoneNumber("");
        requestData.setUf("go");

        var entity = new HttpEntity<>(requestData);

        var response = restTemplate.postForEntity(BASE_URL + "/start-calc", entity, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(fileUtils.readResourceFile("start-calc-bad-request.json"));
    }

    @Test
    @Order(3)
    void updateInfoSuccessfully() {
        var requestData = new UpdateCalcInfoRequestDTO();
        requestData.setId(id);
        requestData.setEnergyConsumption(250);
        requestData.setSolidWasteTotal(250);
        requestData.setRecyclePercentage(0.5);
        requestData.setTransportation(getTransportationDTOList());

        var entity = new HttpEntity<>(requestData);

        var response = restTemplate.exchange(BASE_URL + "/info",
                HttpMethod.PUT, entity, UpdateCalcInfoResponseDTO.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
    }

    @Test
    @Order(3)
    void updateInfoThrowsBadRequest() throws IOException {
        var requestData = new UpdateCalcInfoRequestDTO();
        requestData.setId(id);
        requestData.setEnergyConsumption(120);
        requestData.setSolidWasteTotal(50);
        requestData.setRecyclePercentage(1.2); // invalid value, should be between 0 and 1
        requestData.setTransportation(getTransportationDTOList());

        var entity = new HttpEntity<>(requestData);

        var response = restTemplate.exchange(BASE_URL + "/info",
                HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(fileUtils.readResourceFile("update-info-bad-request.json"));
    }

    private List<TransportationDTO> getTransportationDTOList() {
        var transportationDTO = new TransportationDTO();
        transportationDTO.setType(TransportationType.CAR);
        transportationDTO.setMonthlyDistance(300);

        var transportationDTO2 = new TransportationDTO();
        transportationDTO2.setType(TransportationType.PUBLIC_TRANSPORT);
        transportationDTO2.setMonthlyDistance(250);

        var transportationDTO3 = new TransportationDTO();
        transportationDTO3.setType(TransportationType.MOTORCYCLE);
        transportationDTO3.setMonthlyDistance(50);

        var transportationDTO4 = new TransportationDTO();
        transportationDTO4.setType(TransportationType.BICYCLE);
        transportationDTO4.setMonthlyDistance(25);

        return List.of(transportationDTO, transportationDTO2, transportationDTO3, transportationDTO4);
    }
}
