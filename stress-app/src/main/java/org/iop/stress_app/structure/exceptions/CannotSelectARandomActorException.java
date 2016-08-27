package org.iop.stress_app.structure.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by Manuel Perez P. (darkpriestrelative@gmail.com) on 26/08/16.
 */
public class CannotSelectARandomActorException extends FermatException {

    /**
     * Represent the default message
     */
    public static final String DEFAULT_MESSAGE = "CANNOT SELECT A RANDOM ACTOR";

    /**
     * Constructor with parameters
     *
     * @param message
     * @param cause
     * @param context
     * @param possibleReason
     */
    public CannotSelectARandomActorException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    /**
     * Constructor with parameters
     *
     * @param cause
     * @param context
     * @param possibleReason
     */
    public CannotSelectARandomActorException(Exception cause, String context, String possibleReason) {
        super(DEFAULT_MESSAGE, cause, context, possibleReason);
    }

    /**
     * Constructor with parameters
     *
     * @param message
     * @param cause
     */
    public CannotSelectARandomActorException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    /**
     * Constructor with parameter
     *
     * @param message
     */
    public CannotSelectARandomActorException(final String message) {
        this(message, null);
    }

    /**
     * Constructor with parameter
     *
     * @param exception
     */
    public CannotSelectARandomActorException(final Exception exception) {
        this(exception.getMessage());
        setStackTrace(exception.getStackTrace());
    }

    /**
     * Constructor
     */
    public CannotSelectARandomActorException() {
        this(DEFAULT_MESSAGE);
    }

}
