package pl.logicalsquare.IOproject.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
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
import pl.logicalsquare.IOproject.service.AirplaneTrafficService;

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
                .end(AirplaneTrafficState.UNLOADING)
                .end(AirplaneTrafficState.EMPTY)
                .end(AirplaneTrafficState.CLEANED)
                .end(AirplaneTrafficState.LOCKED);
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
                .action(stopAction())
                .and()
                .withExternal()
                .source(AirplaneTrafficState.STOP)
                .target(AirplaneTrafficState.ENGINE_OFF)
                .event(AirplaneTrafficEvent.TURN_OFF_ENGINE)
                .action(engineOffAction())
                .and()
                .withExternal()
                .source(AirplaneTrafficState.ENGINE_OFF)
                .target(AirplaneTrafficState.LOCKED)
                .event(AirplaneTrafficEvent.LOCK_AIRPLANE)
                .action(lockAction());
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<AirplaneTrafficState, AirplaneTrafficEvent> config) throws Exception {
        StateMachineListenerAdapter<AirplaneTrafficState, AirplaneTrafficEvent> adapter = new StateMachineListenerAdapter<>(){
            @Override
            public void stateChanged(State<AirplaneTrafficState, AirplaneTrafficEvent> from, State<AirplaneTrafficState, AirplaneTrafficEvent> to) {
                log.info(String.format("\nState changed(from: %s to %s)", from, to));
            }
        };
    }

    public Action<AirplaneTrafficState, AirplaneTrafficEvent> stopAction() {
        return context -> {
            System.out.println("---AIRPLANE_STOP was called---");
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(AirplaneTrafficEvent.AIRPLANE_STOP)
                    .setHeader(AirplaneTrafficService.AIRPLANE_ID_HEADER, context.getMessageHeader(AirplaneTrafficService.AIRPLANE_ID_HEADER))
                    .build());
        };
    }

    public Action<AirplaneTrafficState, AirplaneTrafficEvent> engineOffAction() {
        return context -> {
            System.out.println("---TURN_OFF_ENGINE was called---");
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(AirplaneTrafficEvent.TURN_OFF_ENGINE)
                    .setHeader(AirplaneTrafficService.AIRPLANE_ID_HEADER, context.getMessageHeader(AirplaneTrafficService.AIRPLANE_ID_HEADER))
                    .build());
        };
    }

    public Action<AirplaneTrafficState, AirplaneTrafficEvent> lockAction() {
        return context -> {
            System.out.println("---LOCK_AIRPLANE was called---");
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(AirplaneTrafficEvent.LOCK_AIRPLANE)
                    .setHeader(AirplaneTrafficService.AIRPLANE_ID_HEADER, context.getMessageHeader(AirplaneTrafficService.AIRPLANE_ID_HEADER))
                    .build());
        };
    }
}
