package de.bdr.asset.management.user;

public class UserNotFoundException extends RuntimeException{

    private String message;

    public UserNotFoundException(String message) {
        this.message = message;
    }

}
