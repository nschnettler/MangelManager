package applicationLogic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
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
	
	public static void setNullableIntToTextField(TextField tf, Integer value) {
		if(value != null) {
			tf.setText(Integer.toString(value));
		}
	}
	
	public static void setNullableDoubleToTextField(TextField tf, Double value) {
		if(value != null) {
			tf.setText(Double.toString(value));
		}
	}
	
	public static Boolean getNullableBoolean(Object obj, Boolean bln) {
		return obj != null ? bln : null;
	}
	
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
	
	public static boolean validateNotEmpty(TextField tf) {
		if (tf.getText().isEmpty()) {
			tf.getStyleClass().add("error");
			return false;
		}
		tf.getStyleClass().removeAll(Collections.singleton("error")); 
		return true;
	}
	
	public static <T extends Pane> void clearNode(T parentPane) {
		for (Node newnode : parentPane.getChildren()) {
			if (newnode instanceof TextField) {
				((TextField) newnode).clear();
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
}
