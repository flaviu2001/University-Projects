package core.exceptions;

public class PetShopException extends RuntimeException{

    public PetShopException(String message) {
        super(message);
    }

    public PetShopException(String message, Throwable cause) {
        super(message, cause);
    }

    public PetShopException(Throwable cause) {
        super(cause);
    }
}
