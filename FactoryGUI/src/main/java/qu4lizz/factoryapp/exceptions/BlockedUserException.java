package qu4lizz.factoryapp.exceptions;

public class BlockedUserException extends Exception {
    public BlockedUserException() {
        super("User is blocked");
    }

    public BlockedUserException(String message) {
        super(message);
    }
}
