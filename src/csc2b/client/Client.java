package csc2b.client;
import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application{
	
    public static void main(String[] args){
    	launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		BUKAClientPane clientPane = new BUKAClientPane(primaryStage);
		
	}
}