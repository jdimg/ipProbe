package aufgaben1;

import java.io.IOException;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Lookup2 extends Application{

	private Controller controller;
	private Stage primaryStage;
    private VBox gui;
	
	public static void main(String[] args) throws UnknownHostException {
		launch(args);
//		Scanner sc = new Scanner(System.in);
//		System.out.println("Internetadresse eingeben:");
//		String str = sc.next();
//		sc.close();
	}

	@Override
	public void start(Stage arg0) throws Exception {
		primaryStage = arg0;
		primaryStage.setTitle("IP/Host-Finder");
		initGui();
	}
	
	private void initGui() {
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Lookup2.class.getResource("sampleGui.fxml"));
            gui = (VBox) loader.load();
            primaryStage.setScene(new Scene(gui));
            primaryStage.setMinWidth(210);
            primaryStage.setMinHeight(250);
            primaryStage.setMaxWidth(400);
            primaryStage.setMaxHeight(350);
            controller = loader.getController();
//            controller.setMain(this);
            gui.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            	if(key.getCode() == KeyCode.ENTER) {
            		controller.enterPressed();
            	}
            	if(key.getCode() == KeyCode.ESCAPE) {			//Wenn ESC gedrückt wird, beendet sich das Programm.
            		System.exit(0);
            	}
            });
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
	}
}