package com.turkeycrew;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

@SpringBootTest
@Testcontainers
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeliveryServiceTest {

    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    private KafkaTemplate<String, Object> kafkaTemplate;
    private DeliveryService deliveryService;

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0-debian"))
            .withDatabaseName("testDb")
            .withUsername("root")
            .withPassword("1234");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        // Set dynamic properties for the Spring Boot application
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
        String jdbcUrl = mySQLContainer.getJdbcUrl();
        String username = mySQLContainer.getUsername();
        String password = mySQLContainer.getPassword();

        Configuration configuration = new Configuration();
        configuration.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        configuration.addAnnotatedClass(DeliveryInfo.class);
        configuration.addAnnotatedClass(Courier.class);

    }

    @BeforeEach
    void setUp() {
        deliveryService = new DeliveryService(courierRepository, deliveryRepository, kafkaTemplate);
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
    }

    //unit test

    //component test


    //integrations test
    @Test
    void testContainerUp() {
        assert mySQLContainer.isRunning();
    }

    @Test
    @Order(1)
    void test_createCourier() {
        Courier courier = new Courier();
        courier.setEmail("test@test.test");

        ResponseEntity<String> savedCourier  = deliveryService.createCourier(courier);

        assertEquals(HttpStatus.CREATED, savedCourier.getStatusCode());
    }

    @Test
    @Order(2)
    void test_getCourierById() {

        Courier courier = new Courier();
        courier.setEmail("test1@test.test");
        courier.setAvailable(true);
        courierRepository.save(courier);

        ResponseEntity<String> response = deliveryService.getCourierById(2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Email: test1@test.test", "Availability: true");
    }

    @Test
    @Order(3)
    void test_updateCourierEmail() {

        Courier courier = new Courier();
        courier.setEmail("test2@test.test");
        courier.setAvailable(true);
        courierRepository.save(courier);

        ResponseEntity<String> response = deliveryService.updateCourierEmail(1, "newTestMail@test.test");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("newTestMail@test.test");
    }

    @Test
    @Order(4)
    void test_deleteCourier() {
        ResponseEntity<?> deleteResponse = deliveryService.deleteCourier(1);

        ResponseEntity<String> courierResponse = deliveryService.getCourierById(1);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(courierResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(5)
    void test_createDelivery() {
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setAddress("testAddress");

        ResponseEntity<String> savedDelivery  = deliveryService.createDelivery(deliveryInfo);

        assertEquals(HttpStatus.CREATED, savedDelivery.getStatusCode());
    }

    @Test
    @Order(6)
    void test_getDeliveryById() {
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setAddress("testAddress");

        deliveryRepository.save(deliveryInfo);

        ResponseEntity<String> response = deliveryService.getDeliveryById(2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Address: testAddress");
    }

    @Test
    @Order(7)
    void test_updateDeliveryStatus() {
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setStatus(true);

        ResponseEntity<String> response1 = deliveryService.updateDeliveryStatus(1);

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response1.getBody()).contains("true");
    }

    @Test
    @Order(8)
    void test_addCourierToDelivery() {
        Courier courier = courierRepository.getReferenceById(2);

        ResponseEntity<String> response = deliveryService.addCourierToDelivery(1, courier);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("CourierId: 2");
    }

    @Test
    @Order(9)
    void test_deleteDelivery() {
        ResponseEntity<?> deleteResponse = deliveryService.deleteDelivery(1);

        ResponseEntity<String> deliveryResponse = deliveryService.getDeliveryById(1);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deliveryResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
