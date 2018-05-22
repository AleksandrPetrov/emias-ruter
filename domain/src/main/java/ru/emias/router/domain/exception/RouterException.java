package ru.emias.router.domain.exception;

@SuppressWarnings("serial")
public class RouterException extends Exception {

    private String code;

    public RouterException() {
        super();
    }

    public RouterException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public RouterException(String code, String message) {
        super(message);
        this.code = code;
    }

    public RouterException(Throwable arg0) {
        super(arg0);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
