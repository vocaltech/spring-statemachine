package fr.vocaltech.spring.statemachine;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;
import org.springframework.messaging.support.MessageBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import fr.vocaltech.spring.statemachine.basic.BasicStateMachineConfiguration;
import fr.vocaltech.spring.statemachine.basic.Events;
import fr.vocaltech.spring.statemachine.basic.States;

@SpringBootTest
@ContextConfiguration(classes = BasicStateMachineConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BasicStatemachineApplicationTests {
	@Autowired
	private StateMachine<States, Events> stateMachine;

	@BeforeAll
	public void setUp() {
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
		// stateMachine.sendEvent(Events.EVENT1); ----> DEPRECATED

		stateMachine
				.sendEvent(Mono.just(MessageBuilder.withPayload(Events.EVENT1).build()))
				.subscribe();

		assertEquals(States.STATE2, stateMachine.getState().getId());
	}

	@Test
	@Order(4)
	void test_State2_to_State1() {
		// stateMachine.sendEvent(Events.EVENT2); ----> DEPRECATED

		stateMachine
				.sendEvent(Mono.just(MessageBuilder.withPayload(Events.EVENT2).build()))
				.subscribe();

		assertEquals(States.STATE1, stateMachine.getState().getId());
	}

	@AfterAll
	public void tearDown() {
		// stateMachine.stop(); ----> DEPRECATED
		stateMachine.stopReactively().subscribe();
	}
}
