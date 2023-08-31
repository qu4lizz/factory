package qu4lizz.factoryrest.exceptions;

public class BlockedUserException extends Exception {
    public BlockedUserException() {
        super("User is blocked");
    }

    public BlockedUserException(String message) {
        super(message);
    }
}
