package exceptions;

public class InterpreterError extends Exception {
    public InterpreterError() {
        super();
    }

    public InterpreterError(String message) {
        super(message);
    }
}
