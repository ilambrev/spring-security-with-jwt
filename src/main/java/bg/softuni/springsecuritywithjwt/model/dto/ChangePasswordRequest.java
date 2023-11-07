package bg.softuni.springsecuritywithjwt.model.dto;

public class ChangePasswordRequest {

    private String currentPassword;

    private String newPassword;

    private String confirmationPassword;

    public ChangePasswordRequest() {

    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public ChangePasswordRequest setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public ChangePasswordRequest setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public ChangePasswordRequest setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
        return this;
    }

}