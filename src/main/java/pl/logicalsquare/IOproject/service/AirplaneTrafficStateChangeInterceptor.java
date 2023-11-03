package pl.logicalsquare.IOproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import pl.logicalsquare.IOproject.domain.Airplane;
import pl.logicalsquare.IOproject.domain.AirplaneTrafficEvent;
import pl.logicalsquare.IOproject.domain.AirplaneTrafficState;
import pl.logicalsquare.IOproject.repository.AirplaneRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AirplaneTrafficStateChangeInterceptor extends StateMachineInterceptorAdapter<AirplaneTrafficState, AirplaneTrafficEvent> {
    private final AirplaneRepository airplaneRepository;

    @Override
    public void preStateChange(State<AirplaneTrafficState, AirplaneTrafficEvent> state, Message<AirplaneTrafficEvent> message, Transition<AirplaneTrafficState, AirplaneTrafficEvent> transition, StateMachine<AirplaneTrafficState, AirplaneTrafficEvent> stateMachine, StateMachine<AirplaneTrafficState, AirplaneTrafficEvent> rootStateMachine) {
        Optional
                .ofNullable(message).flatMap(msg -> Optional.ofNullable((Integer) msg.getHeaders().getOrDefault(AirplaneTrafficService.AIRPLANE_ID_HEADER, -1))).ifPresent(airplaneId -> {
                    Optional<Airplane> airplane = airplaneRepository.findById(airplaneId);
                    airplane.get().setState(state.getId());
                    airplaneRepository.save(airplane.get());
                });
    }
}
