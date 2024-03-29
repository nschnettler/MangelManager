package applicationLogic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

/**
 * Helper methods
 * 
 * @author Niklas Schnettler
 *
 */
public class Util {
	public static String getCheckedNull(String string) {
		if (string == null) {
			return "";
		}
		return string;
	}
	
	/**
	 * Helper Method to simplify inserting multiple Objects into Database
	 * @param preparedStatement
	 * @param values
	 * @throws SQLException
	 */
	public static void setValues(PreparedStatement preparedStatement, Object... values) throws SQLException {
	    for (int i = 0; i < values.length; i++) {
	    	if (values[i] instanceof String && values[i].equals("")) {
	    		preparedStatement.setObject(i + 1, null);
	    	} else {
	    		preparedStatement.setObject(i + 1, values[i]);
	    	}
	    }
	}
	
	/**
	 * Writes write Integer into TextField if Integer is not null
	 * 
	 * @param tf - TextField
	 * @param value - The Integer
	 */
	public static void setNullableIntToTextField(TextField tf, Integer value) {
		if(value != null) {
			tf.setText(Integer.toString(value));
		}
	}
	
	/**
	 * Writes Double into TextField if Double is not null
	 * 
	 * @param tf - TextField
	 * @param value - The double
	 */
	public static void setNullableDoubleToTextField(TextField tf, Double value) {
		if(value != null) {
			tf.setText(Double.toString(value));
		}
	}
	
	/**
	 * Returns Nullable Boolean generated from Boolean and Object
	 * 
	 * @param obj - Object (which can be Null, 0,1)
	 * @param bln - Boolean (created from Object)
	 * @return Boolean
	 */
	public static Boolean getNullableBoolean(Object obj, Boolean bln) {
		return obj != null ? bln : null;
	}
	
	/**
	 * Validates if Input in textField is an Integer
	 * 
	 * @param tf - TextField
	 * @param canBeEmpty - If the textfield is allowed to be empty
	 * @return
	 */
	public static boolean validateInt(TextField tf, boolean canBeEmpty) {
		if (tf.getText().isEmpty()) {
			if(canBeEmpty) {
				tf.getStyleClass().removeAll(Collections.singleton("error"));
			} else {
				tf.getStyleClass().add("error");
			}
			return false;
		}
		try {
			Integer.valueOf(tf.getText());
			tf.getStyleClass().removeAll(Collections.singleton("error")); 
		} catch (NumberFormatException e) {
			tf.getStyleClass().add("error");
			return false;
		}
		return true;
	}
	
	/**
	 * Validates if Input in textField is an Double
	 * 
	 * @param tf - TextField
	 * @return
	 */
	public static boolean validateDouble(TextField tf) {
		if (tf.getText().isEmpty()) {
			tf.getStyleClass().removeAll(Collections.singleton("error"));
			return false;
		}
		try {
			Double.valueOf(tf.getText());
			tf.getStyleClass().removeAll(Collections.singleton("error")); 
		} catch (NumberFormatException e) {
			tf.getStyleClass().add("error");
			return false;
		}
		return true;
	}
	
	/**
	 * Validates that an TextField is not empty
	 * 
	 * @param tf
	 * @return
	 */
	public static boolean validateNotEmpty(TextField tf) {
		if (tf.getText().isEmpty()) {
			tf.getStyleClass().add("error");
			return false;
		}
		tf.getStyleClass().removeAll(Collections.singleton("error")); 
		return true;
	}
	
	/**
	 * Clears all childs of given Pane
	 * 
	 * @param parentPane - The ParentPane
	 */
	public static <T extends Pane> void clearNode(T parentPane) {
		for (Node newnode : parentPane.getChildren()) {
			if (newnode instanceof TextField) {
				((TextField) newnode).clear();
				((TextField) newnode).getStyleClass().removeAll(Collections.singleton("error"));
            } else {
            	if (newnode instanceof RadioButton) {
            		((RadioButton) newnode).setSelected(false);
            	} else {
            		if (newnode instanceof CheckBox) {
            			((CheckBox) newnode).setSelected(false);
            		} else {
            			if (newnode instanceof DatePicker) {
            				((DatePicker) newnode).setValue(null);
            			} else {
            				if (newnode instanceof ComboBox) {
            					((ComboBox) newnode).setValue(null);
            				} else {
            					if (newnode instanceof TextArea) {
            						((TextArea) newnode).clear();
            		            }
            				}
            			}
            		}
            	}
            }
            if (newnode instanceof Pane) {
                clearNode((Pane) newnode);
            }
		} 
	}
	
	/**
	 * Gets the selected Toggle of a given ToogleGroup
	 * 
	 * @param toggleGroup - A ToogleGroup (sorted ascending)
	 * @return The selected Toggle
	 */
	public static int getSelectedToggle(ToggleGroup toggleGroup) {
		ObservableList<Toggle> toggles = toggleGroup.getToggles();
		for (int i = 0; i < toggles.size(); i++) {
			if (toggles.get(i).isSelected()) {
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * Validates InspectionReport
	 * 
	 * @param inspectionReport - An InspectionReport
	 * @param isOpened - Wether the result of the Validation should be shown in GUI (Dialog)
	 * @return If the InspectionReport is Valid
	 */
	public static boolean validateInspectionReport(InspectionReportFull inspectionReport, boolean isOpened) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory(); 
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<InspectionReportFull>> constraintViolations = validator.validate(inspectionReport);
		if(!constraintViolations.isEmpty()){
			//Validation failed
			if (isOpened) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Der Befundschein ist nicht komplett. Bitte überprüfen Sie Ihre Eingaben");
				String content = "";
				for(ConstraintViolation<InspectionReportFull> error : constraintViolations){
					content += "- " +error.getMessage() + "\n";
				}
				alert.setContentText(content);
				alert.show();
			}
			return false;
		}
		return true;
	}
}
