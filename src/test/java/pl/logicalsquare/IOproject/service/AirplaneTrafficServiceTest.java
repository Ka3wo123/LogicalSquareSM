package pl.logicalsquare.IOproject.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import pl.logicalsquare.IOproject.domain.Airplane;
import pl.logicalsquare.IOproject.domain.AirplaneTrafficEvent;
import pl.logicalsquare.IOproject.domain.AirplaneTrafficState;
import pl.logicalsquare.IOproject.repository.AirplaneRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AirplaneTrafficServiceTest {

    @Autowired
    AirplaneRepository repository;
    @Autowired
    AirplaneTrafficService service;
    static Airplane airplane;
    static StateMachine<AirplaneTrafficState, AirplaneTrafficEvent> sm;

    @BeforeAll
    static void setUp() {
        String uuid = UUID.randomUUID().toString();
        airplane = Airplane.builder().name("Lot " + uuid.substring(0, uuid.indexOf("-"))).build();
    }

    @Test
    @DisplayName("stopping airplane")
    @Order(1)
    void airplaneStop() {
        airplane = service.newAirplane(airplane);

        System.out.println("Should be INIT");
        System.out.println("Actual state: " + airplane.getState());

        sm = service.airplaneStop(airplane.getId());

        Optional<Airplane> stoppedAirplane = repository.findById(airplane.getId());


        System.out.println("\nShould be STOP");
        System.out.println("Actual state: " + sm.getState().getId());
        stoppedAirplane.ifPresent(plane -> System.out.println(stoppedAirplane.orElse(null)));

    }

    @Test
    @DisplayName("turning off airplane's engine")
    @Order(2)
    void airplaneTurnOff() {

        System.out.println("\nShould be STOP");
        System.out.println("Actual state: " + sm.getState().getId());

        sm = service.airplaneTurnOff(airplane.getId());

        Optional<Airplane> engineOffAirplane = repository.findById(airplane.getId());


        System.out.println("\nShould be ENGINE_OFF");
        System.out.println("Actual state: " + sm.getState().getId());
        engineOffAirplane.ifPresent(plane -> System.out.println(engineOffAirplane.orElse(null)));

    }

    @Test
    @DisplayName("locking airplane")
    @Order(3)
    void airplaneLock() {
        System.out.println("\nShould be ENGINE OFF");
        System.out.println("Actual state: " + sm.getState().getId());

        sm = service.airplaneLock(airplane.getId());


        Optional<Airplane> lockedAirplane = repository.findById(airplane.getId());


        System.out.println("\nShould be LOCK");
        System.out.println("Actual state: " + sm.getState().getId());
        lockedAirplane.ifPresent(plane -> System.out.println(lockedAirplane.orElse(null)));

    }
}


