package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MenuController;

import game.base.Client;

public class TestGameApplication extends Application { 
	@SuppressWarnings("exports")
	public static Stage stage;
	public static Client client;
	public static Client botClient;

	@Override
	public void start(@SuppressWarnings("exports") Stage primaryStage) throws Exception {
		new Thread(() -> {
			client = new Client();
			System.out.println(client);
		}).start();
		new Thread(() -> {
			botClient = new Client();
			System.out.println(client);
		}).start();
		stage = primaryStage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
		Parent root = fxmlLoader.load();
		MenuController controller = fxmlLoader.getController();
		client.setMenuController(controller); 
		stage.setResizable(false);
		stage.setScene(new Scene(root)); 
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}