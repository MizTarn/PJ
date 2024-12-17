package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EndGameController implements Initializable{
	@FXML
	private VBox rootPane;
	
	@FXML
	private StackPane buttonContainer;
	
	@FXML
	private Button replay;
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		rootPane.setOpacity(0);
		buttonContainer.setOpacity(0);
		makeFadeInTransition();
	}

	private void makeFadeButtonTransition() {
		// TODO Auto-generated method stub
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.seconds(1));
		fadeTransition.setNode(buttonContainer);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		
		fadeTransition.play();
	}

	private void makeFadeInTransition() {
		// TODO Auto-generated method stub
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.seconds(1.5));
		fadeTransition.setNode(rootPane);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		
		fadeTransition.play();
		fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
	
				@Override
				public void handle(ActionEvent event) {
					makeFadeButtonTransition();
			}
			
		});
	}
	
	public void loadMainMenu(ActionEvent e) throws IOException 
	{
		SceneLoader.loadScene(e, "/fxml/Durak.fxml");
	}
	
	public void loadChooseModeScene (ActionEvent e) throws IOException
	{
		SceneLoader.loadScene(e, "/fxml/ChooseModeScene.fxml");
	}
}
