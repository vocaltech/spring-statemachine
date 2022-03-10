package fr.vocaltech.spring.statemachine.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class ServerStateMachineConfiguration extends EnumStateMachineConfigurerAdapter<States, Events> {
    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config
            .withConfiguration()
            .autoStartup(false)
            .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states
            .withStates()
            .initial(States.STATE_INIT)
            .states(EnumSet.allOf(States.class));

        states
            .withStates()
            .state(States.STATE_NOT_REACHABLE, executeNotReachable());
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.STATE_INIT).target(States.STATE_REACHABLE).event(Events.EVENT_SERVER_REACHABLE).action(initAction())
                .and()
                .withExternal()
                .source(States.STATE_INIT).target(States.STATE_NOT_REACHABLE).event(Events.EVENT_SERVER_NOT_REACHABLE);
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<States, Events>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                System.out.println("[StateMachineListener.stateChanged()] from: " + (from == null ? "none": from.getId()) + " - to: " + to.getId());
            }
        };
    }

    @Bean
    public Action<States, Events> initAction() {
        return context ->
                System.out.println("[initAction()] source: " + context.getSource().getId() +
                        " - target: " + context.getTarget().getId() +
                        " - event: " + context.getEvent().name()
                );
    }

    @Bean
    public Action<States, Events> executeNotReachable() {
        return context -> System.out.println("[executeNotReachable()] Do: " + context.getTarget().getId());
    }
}
