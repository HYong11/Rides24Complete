package exceptions;

public class TravelerNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public TravelerNotFoundException() {
        super();
    }

    /**
     * This exception is triggered if the traveler is not found in the database.
     * @param s String of the exception
     */
    public TravelerNotFoundException(String s) {
        super(s);
    }
}
