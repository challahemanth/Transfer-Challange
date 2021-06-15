package com.db.awmd.challenge.exception;

/**
 * This a custom exception that extends runtime exception
 * which is thrown when transfer is successfully  done
 *
 * @param - message - Error message that needs to be displayed to the user
 *
 */
public class BalanceSuccessException extends RuntimeException{

    public BalanceSuccessException(String message) {
        super(message);
    }

}
