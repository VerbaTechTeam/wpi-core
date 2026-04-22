package pl.vtt.wpi.core.domain.dto;

public record PasswordDto(String password, String passwordConfirmation) {
    @Override
    public String toString() {
        return "PasswordDto[password=***, passwordConfirmation=***]";
    }
}
