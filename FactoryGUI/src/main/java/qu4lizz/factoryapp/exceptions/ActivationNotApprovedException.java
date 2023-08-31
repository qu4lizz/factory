package qu4lizz.factoryapp.exceptions;

public class ActivationNotApprovedException extends Exception {
    public ActivationNotApprovedException() {
        super("User is not activated");
    }

    public ActivationNotApprovedException(String message) {
        super(message);
    }
}
