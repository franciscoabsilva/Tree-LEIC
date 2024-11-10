package exceptions;

public class DuplicatePersonException extends Exception {
    private static final long serialVersionUID = 1L;

    private final int _fenixId;
    
    public DuplicatePersonException(int fenixId) {
        _fenixId = fenixId;
    }
    
    public int getFenixId() {
        return _fenixId;
    }
}