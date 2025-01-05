package br.com.actionlabs.carboncalc.controller;

import br.com.actionlabs.carboncalc.config.IntegrationTestContainers;
import br.com.actionlabs.carboncalc.dto.*;
import br.com.actionlabs.carboncalc.enums.TransportationType;
import br.com.actionlabs.carboncalc.utils.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test are made to run in a containerized environment,
 * so it will start a container with the database and run the tests.
 * Tests run individually may fail because of the database state
 *
 * @author Hadryan Silva
 * @since 03-01-2025
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({IntegrationTestContainers.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OpenRestControllerIT {

    public static final String BASE_URL = "/open";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FileUtils fileUtils;

    private static String ID;

    @Test
    @Order(1)
    @DisplayName("Start calculation successfully")
    void testStartCalcSuccessfully() {
        var requestData = new StartCalcRequestDTO();
        requestData.setName("Teste");
        requestData.setEmail("teste@teste.com");
        requestData.setPhoneNumber("62999999999");
        requestData.setUf("GO");

        var entity = new HttpEntity<>(requestData);

        var response = restTemplate.postForEntity(BASE_URL + "/start-calc", entity, StartCalcResponseDTO.class);
        ID = response.getBody().getId();
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotBlank();
    }

    @Test
    @Order(2)
    @DisplayName("Start calculation throws bad request")
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
        assertThat(response.getBody()).isEqualTo(fileUtils.readResourceFile("start-calc-bad-response.json"));
    }

    @Test
    @Order(3)
    @DisplayName("Update info successfully")
    void updateInfoSuccessfully() {
        var requestData = new UpdateCalcInfoRequestDTO();
        requestData.setId(ID);
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
    @DisplayName("Update info throws bad request")
    void updateInfoThrowsBadRequest() throws IOException {
        var requestData = new UpdateCalcInfoRequestDTO();
        requestData.setId(ID);
        requestData.setEnergyConsumption(120);
        requestData.setSolidWasteTotal(50);
        requestData.setRecyclePercentage(1.2); // invalid value, should be between 0 and 1
        requestData.setTransportation(getTransportationDTOList());

        var entity = new HttpEntity<>(requestData);

        var response = restTemplate.exchange(BASE_URL + "/info",
                HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(fileUtils.readResourceFile("update-info-bad-response.json"));
    }

    @Test
    @Order(4)
    @DisplayName("Get result successfully")
    void testGetResultSuccessfully() {
        var response = restTemplate.getForEntity(BASE_URL + "/result/" + ID, CarbonCalculationResultDTO.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEnergy()).isEqualTo(120);
        assertThat(response.getBody().getSolidWaste()).isEqualTo(171);
        assertThat(response.getBody().getTransportation()).isEqualTo(71);
        assertThat(response.getBody().getTotal()).isEqualTo(362);
    }

    @Test
    @Order(4)
    @DisplayName("Get result throws not found")
    void testGetResultThrowsNotFound() throws IOException {
        var response = restTemplate.getForEntity(BASE_URL + "/result/123", String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(fileUtils.readResourceFile("get-result-not-found-response.json"));
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
