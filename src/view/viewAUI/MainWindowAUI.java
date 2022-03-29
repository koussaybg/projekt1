package view.viewAUI;

import java.util.List;

public interface MainWindowAUI {
	/**
     * refresh the Main Window 
     */

	public void refreshMainWindow(); 
	/**
     * Show an error message 
     * @param error the error message
     */

	void showError(String error);
	
	public void showValidationErrors(List<String> errors);

}
