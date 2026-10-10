package sirenko.mar.bank;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sirenko.mar.bank.operations.OperationsConsoleListener;

public class Main {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext("sirenko.mar.bank");

		OperationsConsoleListener operationsConsoleListener =
				context.getBean(OperationsConsoleListener.class);

		operationsConsoleListener.readFromConsole();
	}

}
