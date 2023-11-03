package pl.logicalsquare.IOproject;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.logicalsquare.IOproject.drawingLogic.SpanningTreeApplication;

@SpringBootApplication
public class IOprojectApplication {

	public static void main(String[] args) {
		Application.launch(SpanningTreeApplication.class, args);
	}

}
