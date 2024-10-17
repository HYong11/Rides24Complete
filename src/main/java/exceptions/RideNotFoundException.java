package exceptions;

public class RideNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public RideNotFoundException() {
        super();
    }

    /**
     * This exception is triggered if the ride is not found in the database.
     * @param s String of the exception
     */
    public RideNotFoundException(String s) {
        super(s);
    }
}
