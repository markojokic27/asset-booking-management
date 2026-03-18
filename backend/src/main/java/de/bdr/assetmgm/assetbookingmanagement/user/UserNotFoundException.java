package de.bdr.assetmgm.assetbookingmanagement.user;

public class UserNotFoundException extends RuntimeException{

    private String message;

    public UserNotFoundException(String message) {
        this.message = message;
    }

}
