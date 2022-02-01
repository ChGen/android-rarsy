package rars.venus;

public abstract class VenusUIDelegate {
    public VenusUIDelegate() {

    }

    public abstract String getInputString(String prompt, int maxlength, Boolean isPopup);
}
