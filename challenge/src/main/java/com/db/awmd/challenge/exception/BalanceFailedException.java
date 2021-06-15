package com.db.awmd.challenge.exception;

/**
 * This a custom exception that extends runtime exception
 * which is thrown when transfer is failed due to various reasons
 *
 * @param - message - Error message that needs to be displayed to the user
 *
 */
public class BalanceFailedException extends RuntimeException{

    public BalanceFailedException(String message) {
        super(message);
    }
}
