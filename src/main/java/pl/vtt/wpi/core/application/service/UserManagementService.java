package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.application.exception.InvalidPasswordException;
import pl.vtt.wpi.core.application.exception.UserAlreadyExistsException;
import pl.vtt.wpi.core.application.exception.UserNotExistsException;
import pl.vtt.wpi.core.application.exception.UserManagementServiceException;
import pl.vtt.wpi.core.domain.dto.PasswordDto;
import pl.vtt.wpi.core.domain.model.User;

public interface UserManagementService {
    void createUser(User user, PasswordDto passwordDto)
            throws UserAlreadyExistsException, InvalidPasswordException, UserManagementServiceException;

    void changePassword(PasswordDto passwordDto)
            throws InvalidPasswordException, UserManagementServiceException;

    void removeUser(User user)
            throws UserNotExistsException, UserManagementServiceException;
}
