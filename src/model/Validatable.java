package model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides methods for validating the integrity of a model.
 * @author sopr057
 *
 */
public interface Validatable {
	
	/**
	 * Returns if getValidationErrors has any content by default.
	 * @return if the given class is valid
	 */
	public default boolean isValid() {
		return getValidationErrors().size() == 0;
	}

	/**
	 * Builds a list of Strings that describe current validation problems of the implementing class.
	 * @return List of validation error descriptions
	 */
	public List<String> getValidationErrors();
	
	/**
	 * Returns if the string is null or empty after trimming.
	 * @param str
	 * @return if if the string is null or empty after trimming
	 */
	public default boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}
	
	/**
	 * Returns if the given string is a valid phone number.
	 * @param phoneNumber2
	 * @return
	 */
	public default boolean isValidPhoneNumber(String phoneNumber) {
		if(isNullOrEmpty(phoneNumber))
			return false;
		try {
			Integer.valueOf(phoneNumber);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
}
