package com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions;

import com.bitdubai.fermat_api.layer.osa_android.OsaAndroidException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 16/08/16.
 */
public class DatabaseRecordExistException extends OsaAndroidException {

    public static final String DEFAULT_MESSAGE = "RECORD EXISTS EXCEPTION";

    /**
     * Default constructor
     * @param message
     * @param cause
     * @param context
     * @param possibleReason
     */
    public DatabaseRecordExistException(
            final String message,
            final Exception cause,
            final String context,
            final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    /**
     * Default constructor with message as parameter
     * @param message
     */
    public DatabaseRecordExistException(final String message) {
        super(message, null, null, null);
    }

}
