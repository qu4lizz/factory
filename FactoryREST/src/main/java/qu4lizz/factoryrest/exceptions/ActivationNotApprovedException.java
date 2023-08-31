package qu4lizz.factoryrest.exceptions;

public class ActivationNotApprovedException extends Exception {
    public ActivationNotApprovedException() {
        super("User is not activated");
    }

    public ActivationNotApprovedException(String message) {
        super(message);
    }
}
