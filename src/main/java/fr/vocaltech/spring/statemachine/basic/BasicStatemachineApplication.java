package fr.vocaltech.spring.statemachine.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class BasicStatemachineApplication implements CommandLineRunner {
	@Autowired
	private StateMachine<States, Events> stateMachine;

	public static void main(String[] args) {
		SpringApplication.run(BasicStatemachineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("---> Start SSM");
		System.out.println("curState: " + stateMachine.getState().getId());

		System.out.println("---> trigger EVENT1");

		stateMachine
				.sendEvent(Mono.just(MessageBuilder.withPayload(Events.EVENT1).build()))
				.subscribe();

		System.out.println("---> trigger EVENT2");

		stateMachine
				.sendEvent(Mono.just(MessageBuilder.withPayload(Events.EVENT2).build()))
				.subscribe();
	}
}
