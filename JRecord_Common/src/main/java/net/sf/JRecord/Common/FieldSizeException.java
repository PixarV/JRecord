package net.sf.JRecord.Common;

public class FieldSizeException extends RecordException{

    public FieldSizeException(boolean x, String msg) {
        super(x, msg);
    }

    public FieldSizeException(String msg) {
        super(msg);
    }

    public FieldSizeException(String msg, String parm) {
        super(msg, parm);
    }

    public FieldSizeException(String msg, Object[] parms) {
        super(msg, parms);
    }

    public FieldSizeException(String msg, Throwable exception) {
        super(msg, exception);
    }

    public FieldSizeException(String msg, Object[] parms, Throwable exception) {
        super(msg, parms, exception);
    }
}
