package exception;

public class DocumentNotAttachedException extends RuntimeException {
    public DocumentNotAttachedException(String message) {
        super(message);
    }
}
