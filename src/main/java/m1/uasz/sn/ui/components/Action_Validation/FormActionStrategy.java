package m1.uasz.sn.ui.components.Action_Validation;

import javax.swing.*;

public interface FormActionStrategy {
    void executerApresValidation(JTextField[] textFields);
    void executerApresValidation(JTextField[] textFields, String callingComponent, String typeForm);
    void executerApresValidation(JComboBox<String>[] textFields, String callingComponent, String typeForm, String tabComponent, String ojectDetailed);
}
