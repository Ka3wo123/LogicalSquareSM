package pl.logicalsquare.IOproject.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import pl.logicalsquare.IOproject.domain.AirplaneTrafficEvent;
import pl.logicalsquare.IOproject.domain.AirplaneTrafficState;

import java.util.EnumSet;

@Slf4j
@EnableStateMachineFactory
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<AirplaneTrafficState, AirplaneTrafficEvent> {
    @Override
    public void configure(StateMachineStateConfigurer<AirplaneTrafficState, AirplaneTrafficEvent> states) throws Exception {
        states.withStates()
                .initial(AirplaneTrafficState.INIT)
                .states(EnumSet.allOf(AirplaneTrafficState.class))
                .end(AirplaneTrafficState.STOP)
                .end(AirplaneTrafficState.IMMOBILISING)
                .end(AirplaneTrafficState.ENGINE_OFF)
                .end(AirplaneTrafficState.OPEN);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<AirplaneTrafficState, AirplaneTrafficEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(AirplaneTrafficState.INIT)
                .target(AirplaneTrafficState.INIT)
                .event(AirplaneTrafficEvent.INIT_EVENT)
                .and()
                .withExternal()
                .source(AirplaneTrafficState.INIT)
                .target(AirplaneTrafficState.STOP)
                .event(AirplaneTrafficEvent.AIRPLANE_STOP)
                .and()
                .withExternal()
                .source(AirplaneTrafficState.OPEN)
                .target(AirplaneTrafficState.UNLOADING)
                .event(AirplaneTrafficEvent.TURN_OFF_ENGINE);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<AirplaneTrafficState, AirplaneTrafficEvent> config) throws Exception {
        StateMachineListenerAdapter<AirplaneTrafficState, AirplaneTrafficEvent> adapter = new StateMachineListenerAdapter<>(){
            @Override
            public void stateChanged(State<AirplaneTrafficState, AirplaneTrafficEvent> from, State<AirplaneTrafficState, AirplaneTrafficEvent> to) {
                log.info(String.format("State changed(from: %s to %s)", from, to));
            }
        };

    }
}
