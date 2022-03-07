package fr.vocaltech.spring.statemachine.basic;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = BasicStateMachineConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BasicStatemachineApplicationTest {
    @Autowired
    private StateMachine<States, Events> stateMachine;

    @BeforeAll
    void setUp() {
        // stateMachine.start(); ----> DEPRECATED
        stateMachine.startReactively().subscribe();
    }

    @Test
    @Order(1)
    void contextLoads() {
        assertNotNull(stateMachine);
    }

    @Test
    @Order(2)
    void testInitState() {
        assertEquals(States.STATE1, stateMachine.getState().getId());
    }

    @Test
    @Order(3)
    void test_State1_to_State2() {
        // trigger EVENT1
        // stateMachine.sendEvent(Events.EVENT1); ----> DEPRECATED
        stateMachine
                .sendEvent(Mono.just(MessageBuilder.withPayload(Events.EVENT1).build()))
                .subscribe();

        assertEquals(States.STATE2, stateMachine.getState().getId());
    }

    @Test
    @Order(4)
    void test_State2_to_State1() {
        // trigger EVENT2
        // stateMachine.sendEvent(Events.EVENT2); ----> DEPRECATED
        stateMachine
                .sendEvent(Mono.just(MessageBuilder.withPayload(Events.EVENT2).build()))
                .subscribe();

        assertEquals(States.STATE1, stateMachine.getState().getId());
    }

    @AfterAll
    void tearDown() {
        // stateMachine.stop(); ----> DEPRECATED
        stateMachine.stopReactively().subscribe();
    }
}