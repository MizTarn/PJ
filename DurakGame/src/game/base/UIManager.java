package game.base;

import java.io.IOException;

import application.GameApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIManager {
	public <T> T openWait() {
		return loadFXML("/view/waiting.fxml", "WAITING ROOM", GameApplication.stage);

	}

	public <T> T openGameGUI() {
		return loadFXML("/view/game2.fxml", "GAME", GameApplication.stage); 
	}
	
	public <T> T openGameBase() {
		return loadFXML("/view/gamebase.fxml", "GAME", GameApplication.stage);
	}

	public <T> T openEnd() {
		return loadFXML("/view/end.fxml", "ENDING", GameApplication.stage); 
	}

	public <T> T loadFXML(String fxmlFile, String title, Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
			Parent root = loader.load();
			stage.close();
			stage.setTitle(title);
			stage.setScene(new Scene(root));
			stage.show();
//            return loader;
			return loader.getController(); // Trả về Controller
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error loading " + fxmlFile);
		}
		return null;
	}

}