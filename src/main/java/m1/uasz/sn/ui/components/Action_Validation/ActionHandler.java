package m1.uasz.sn.ui.components.Action_Validation;

public interface ActionHandler {
    void onView(Object id, String callingComponent);
    void onEdit(Object id, String callingComponent);
    void onDelete(Object id, String callingComponent);
}
