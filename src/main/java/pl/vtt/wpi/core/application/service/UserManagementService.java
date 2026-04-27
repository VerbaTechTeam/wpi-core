package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.application.exception.InvalidPasswordException;
import pl.vtt.wpi.core.application.exception.UserAlreadyExistsException;
import pl.vtt.wpi.core.application.exception.UserNotExistsException;
import pl.vtt.wpi.core.application.exception.UserManagementOperationException;
import pl.vtt.wpi.core.domain.dto.PasswordDto;
import pl.vtt.wpi.core.domain.model.User;

public interface UserManagementService {
    void createUser(User user, PasswordDto passwordDto)
            throws UserAlreadyExistsException, InvalidPasswordException, UserManagementOperationException;

    void changePassword(PasswordDto passwordDto)
            throws InvalidPasswordException, UserManagementOperationException;

    void removeUser(User user)
            throws UserNotExistsException, UserManagementOperationException;
}
