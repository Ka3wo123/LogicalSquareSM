package pl.logicalsquare.IOproject.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import pl.logicalsquare.IOproject.domain.Airplane;
import pl.logicalsquare.IOproject.domain.AirplaneTrafficEvent;
import pl.logicalsquare.IOproject.domain.AirplaneTrafficState;
import pl.logicalsquare.IOproject.repository.AirplaneRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AirplaneTrafficService {
    private final StateMachineFactory<AirplaneTrafficState, AirplaneTrafficEvent> stateMachineFactory;
    private final AirplaneRepository airplaneRepository;
    public final static String AIRPLANE_ID_HEADER = "airplane_id";
    private final AirplaneTrafficStateChangeInterceptor interceptor;

    public Airplane newAirplane(Airplane airplane) {
        airplane.setState(AirplaneTrafficState.INIT);
        return airplaneRepository.save(airplane);
    }


    @Transactional
    public StateMachine<AirplaneTrafficState, AirplaneTrafficEvent> airplaneStop(Integer id) {
        StateMachine<AirplaneTrafficState, AirplaneTrafficEvent> sm = build(id);

        sendEvent(id, sm, AirplaneTrafficEvent.AIRPLANE_STOP);
        return sm;
    }

    public StateMachine<AirplaneTrafficState, AirplaneTrafficEvent> airplaneTurnOff(Integer id) {
        StateMachine<AirplaneTrafficState, AirplaneTrafficEvent> sm = build(id);

        sendEvent(id, sm, AirplaneTrafficEvent.TURN_OFF_ENGINE);
        return sm;
    }

    public StateMachine<AirplaneTrafficState, AirplaneTrafficEvent> airplaneLock(Integer id) {
        StateMachine<AirplaneTrafficState, AirplaneTrafficEvent> sm = build(id);

        sendEvent(id, sm, AirplaneTrafficEvent.LOCK_AIRPLANE);
        return sm;
    }

    private void sendEvent(Integer id, StateMachine<AirplaneTrafficState, AirplaneTrafficEvent> sm, AirplaneTrafficEvent event) {
        Message<AirplaneTrafficEvent> msg = MessageBuilder.withPayload(event)
                .setHeader(AIRPLANE_ID_HEADER, id)
                .build();

        sm.sendEvent(msg);
    }

    private StateMachine<AirplaneTrafficState, AirplaneTrafficEvent> build(Integer airplaneID) {
        Optional<Airplane> airplane = airplaneRepository.findById(airplaneID);
        StateMachine<AirplaneTrafficState, AirplaneTrafficEvent> sm;

        if (airplane.isPresent()) {
            sm = stateMachineFactory.getStateMachine(Integer.toString(airplane.get().getId()));
        } else {
            return null;
        }

        sm.stop();

        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(interceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext<>(airplane.get().getState(), null, null, null));
                });

        sm.start();

        return sm;
    }







}
