package view.viewAUI;

public interface CostumerViewAUI {
	/**
	 * refresh the Customer List 
	 */
	void refreshCustomerList();

	/**
	 * To show a validation error.
	 * @param error The error to display.
	 */
	public abstract void showValidationError(String error);
}
