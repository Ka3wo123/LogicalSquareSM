package pl.logicalsquare.IOproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.logicalsquare.IOproject.domain.Airplane;
import pl.logicalsquare.IOproject.repository.AirplaneRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AirplaneTrafficServiceTest {

    @Autowired
    AirplaneRepository repository;
    @Autowired
    AirplaneTrafficService service;
    Airplane airplane;

    @BeforeEach
    void setUp() {
        airplane = Airplane.builder().name("Lot").build();
    }
    @Test
    void airplaneStop() {
        Airplane airplane1 = service.newAirplane(airplane);
        service.airplaneStop(airplane1.getId());

        Optional<Airplane> stoppedAirplane = repository.findById(airplane1.getId());

        System.out.println(stoppedAirplane);
    }
}