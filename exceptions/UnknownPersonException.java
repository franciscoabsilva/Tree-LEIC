package exceptions;

public class UnknownPersonException extends Exception {
    private static final long serialVersionUID = 1L;

    private final int _fenixId;
    
    public UnknownPersonException(int fenixId) {
        _fenixId = fenixId;
    }
    
    public int getFenixId() {
        return _fenixId;
    }
}