package fr.vocaltech.spring.statemachine.server;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = ServerStateMachineConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServerStateMachineApplicationTest {
    @Autowired
    private StateMachine<States, Events> stateMachine;

    @BeforeAll
    void setUp() {
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
        assertEquals(States.STATE_INIT, stateMachine.getState().getId());
    }

    @AfterAll
    void tearDown() {
        stateMachine.stopReactively().subscribe();
    }
}