package m1.uasz.sn.ui.components.Action_Validation;

import javax.swing.*;

public interface FormValidationStrategy {
    boolean valider(JTextField[] fields);
    boolean valider(JTextField[] fields, String callingComponent);
    boolean valider(JTextField[] fields, String callingComponent, String typeForm);
}
