package application;

import controller.ManTheSController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.viewController.*;


public class Main extends Application implements MainAUI, EventHandler<WindowEvent> {
	private MainWindowViewController mainWindowViewController;
	private Scene mainWindowScene;
	private Stage primaryStage;
	private Scene loginScene;
	private ManTheSController manTheSController;

	@Override
	public void start(Stage primaryStage) {
		Platform.setImplicitExit(false); //don't exit on close
		this.primaryStage = primaryStage;
		primaryStage.setResizable(false);
		try {
			FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
			BorderPane mainPane = mainViewLoader.load();

			mainWindowScene = new Scene(mainPane);
			mainWindowViewController = mainViewLoader.getController();

			FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/Login.fxml"));
			AnchorPane loginPane = loginLoader.load();
			loginScene = new Scene(loginPane);
			loginScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


			LoginViewController loginViewController = loginLoader.getController();
			mainWindowViewController.setLoginViewController(loginViewController);
			loginViewController.setMainWindowViewController(mainWindowViewController);
			loginViewController.setMainAUI(this);

			

			manTheSController = new ManTheSController(mainWindowViewController);
			manTheSController.getiOController().loadOffice();
			mainWindowViewController.setManTheSController(manTheSController);


			openLogin();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void openLogin(){
		primaryStage.setTitle("Login");
		primaryStage.setScene(loginScene);
		//primaryStage.centerOnScreen(); //buggy with linux
		primaryStage.show();
	}

	@Override
	public void onSuccessfulLogin() {
		primaryStage.setTitle("ManTheS - Tool for Managing Therapies");
		primaryStage.setScene(mainWindowScene);
		//primaryStage.centerOnScreen(); //buggy with linux
		primaryStage.show();
		primaryStage.setOnCloseRequest(this);
		mainWindowViewController.refresh();
	}

	@Override
	public void handle(WindowEvent event) {
		if(primaryStage.getScene() != loginScene){
			event.consume();
			manTheSController.getOffice().setCurrentUser(null);
			manTheSController.getiOController().saveOffice();
			openLogin();
		}else{
			Platform.exit();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
