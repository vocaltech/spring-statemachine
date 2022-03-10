package fr.vocaltech.spring.statemachine.server;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = ServerStateMachineConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServerStateMachineApplicationTest {
    @Autowired
    private StateMachine<States, Events> stateMachine;

    @BeforeEach
    void initEach() {
        stateMachine.startReactively().subscribe();
    }

    @AfterEach
    void cleanupEach() {
        stateMachine.stopReactively().subscribe();
    }

    @Disabled
    @Test
    void test_Init_State() {
        assertEquals(States.STATE_INIT, stateMachine.getState().getId());
    }

    @Test
    void test_from_Init_to_NotReachable_State() {
        stateMachine
                .sendEvent(Mono.just(MessageBuilder.withPayload(Events.EVENT_SERVER_NOT_REACHABLE).build()))
                .subscribe();

        assertEquals(States.STATE_NOT_REACHABLE, stateMachine.getState().getId());
    }

    @Disabled
    @Test
    void test_from_Init_to_Reachable_State() {
        stateMachine
                .sendEvent(Mono.just(MessageBuilder.withPayload(Events.EVENT_SERVER_REACHABLE).build()))
                .subscribe();

        assertEquals(States.STATE_REACHABLE, stateMachine.getState().getId());
    }
}