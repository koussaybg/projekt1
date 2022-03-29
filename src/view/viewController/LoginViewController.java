package view.viewController;

import application.MainAUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import view.viewAUI.LoginViewAUI;

/**
 * @author severin
 */
public class LoginViewController implements LoginViewAUI {

	private MainWindowViewController mainWindowViewController;

	@FXML
	private TextField userNameTextField;
	@FXML
	private PasswordField passwordTextField;
	@FXML
	private Label errorLabel;

	private MainAUI mainAUI;

	/**
	 * called by view if login button is clicked
	 * @param action action passed by ui
	 */
	public void onLoginButtonClicked(ActionEvent action) {
		String username = userNameTextField.getText();
		String password = passwordTextField.getText();
		this.mainWindowViewController.getManTheSController().getLoginController().requestLogin(username, password);
	}

	public void setMainWindowViewController(MainWindowViewController mwvc) {
		this.mainWindowViewController = mwvc;
	}

	public void setMainAUI(MainAUI mainAUI) {
		this.mainAUI = mainAUI;
	}


	/**
	 * @see view.viewAUI.LoginViewAUI#onLoginResult(boolean)
	 */
	public void onLoginResult(boolean loginSuccess) {
		userNameTextField.setText("");
		passwordTextField.setText("");
		userNameTextField.requestFocus();
		if(loginSuccess){
			mainAUI.onSuccessfulLogin();
		}else{
        	errorLabel.setVisible(true);
		}
	}
}
