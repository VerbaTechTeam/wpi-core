package pl.vtt.wpi.core.application.service.impl;

import java.util.List;
import java.util.Objects;
import pl.vtt.wpi.core.application.exception.InvalidPasswordException;
import pl.vtt.wpi.core.application.exception.UserAlreadyExistsException;
import pl.vtt.wpi.core.application.exception.UserManagementServiceException;
import pl.vtt.wpi.core.application.exception.UserNotExistsException;
import pl.vtt.wpi.core.application.service.UserManagementService;
import pl.vtt.wpi.core.domain.dto.PasswordDto;
import pl.vtt.wpi.core.domain.dto.UserCreateRequest;
import pl.vtt.wpi.core.domain.model.User;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.domain.port.OutputPort;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;

public class UserManagementServiceImpl implements UserManagementService {
    private final OutputPort<List<User>> usersOutputPort;
    private final InputPort<UserCreateRequest> userCreateRequestInputPort;
    private final InputPort<PasswordDto> changePasswordInputPort;
    private final InputPort<User> removeUserInputPort;

    public UserManagementServiceImpl(OutputPort<List<User>> usersOutputPort,
                                     InputPort<UserCreateRequest> userCreateRequestInputPort,
                                     InputPort<PasswordDto> changePasswordInputPort,
                                     InputPort<User> removeUserInputPort) {
        this.usersOutputPort = Objects.requireNonNull(usersOutputPort, "usersOutputPort cannot be null");
        this.userCreateRequestInputPort = Objects.requireNonNull(userCreateRequestInputPort,
                "userCreateRequestInputPort cannot be null");
        this.changePasswordInputPort = Objects.requireNonNull(changePasswordInputPort,
                "changePasswordInputPort cannot be null");
        this.removeUserInputPort = Objects.requireNonNull(removeUserInputPort,
                "removeUserInputPort cannot be null");
    }

    @Override
    public void createUser(User user, PasswordDto passwordDto)
            throws UserAlreadyExistsException, InvalidPasswordException, UserManagementServiceException {
        validatePassword(passwordDto);
        if (user == null) {
            throw new UserManagementServiceException("User cannot be null");
        }
        boolean exists = loadUsers().stream().anyMatch(existingUser -> existingUser.username().equals(user.username()));
        if (exists) {
            throw new UserAlreadyExistsException();
        }
        try {
            userCreateRequestInputPort.send(new UserCreateRequest(user, passwordDto));
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new UserManagementServiceException("Cannot create user", cause);
        }
    }

    @Override
    public void changePassword(PasswordDto passwordDto)
            throws InvalidPasswordException, UserManagementServiceException {
        validatePassword(passwordDto);
        try {
            changePasswordInputPort.send(passwordDto);
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new UserManagementServiceException("Cannot change password", cause);
        }
    }

    @Override
    public void removeUser(User user)
            throws UserNotExistsException, UserManagementServiceException {
        if (user == null) {
            throw new UserManagementServiceException("User cannot be null");
        }
        boolean exists = loadUsers().stream().anyMatch(existingUser -> existingUser.username().equals(user.username()));
        if (!exists) {
            throw new UserNotExistsException();
        }
        try {
            removeUserInputPort.send(user);
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new UserManagementServiceException("Cannot remove user", cause);
        }
    }

    private List<User> loadUsers() throws UserManagementServiceException {
        try {
            List<User> users = usersOutputPort.load();
            return users == null ? List.of() : users;
        } catch (OutputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new UserManagementServiceException("Cannot load users", cause);
        }
    }

    private void validatePassword(PasswordDto passwordDto) throws InvalidPasswordException {
        if (passwordDto == null || passwordDto.password() == null || passwordDto.passwordConfirmation() == null) {
            throw new InvalidPasswordException("Password data is required");
        }
        if (passwordDto.password().isBlank()) {
            throw new InvalidPasswordException("Password cannot be blank");
        }
        if (!passwordDto.password().equals(passwordDto.passwordConfirmation())) {
            throw new InvalidPasswordException("Password and password confirmation must be identical");
        }
    }
}
