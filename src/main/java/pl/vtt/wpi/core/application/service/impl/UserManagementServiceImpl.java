package pl.vtt.wpi.core.application.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import pl.vtt.wpi.core.application.exception.InvalidPasswordException;
import pl.vtt.wpi.core.application.exception.UserAlreadyExistsException;
import pl.vtt.wpi.core.application.exception.UserManagementOperationException;
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
    private final Lock userLock = new ReentrantLock();

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
            throws UserAlreadyExistsException, InvalidPasswordException, UserManagementOperationException {
        if (user == null) {
            throw new UserManagementOperationException("User cannot be null");
        }
        validatePassword(passwordDto);
        userLock.lock();
        try {
            boolean exists = loadUsers().stream().anyMatch(existingUser -> existingUser.username().equals(user.username()));
            if (exists) {
                throw new UserAlreadyExistsException();
            }
            userCreateRequestInputPort.send(new UserCreateRequest(user, passwordDto));
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new UserManagementOperationException("Cannot create user", cause);
        } finally {
            userLock.unlock();
        }
    }

    @Override
    public void changePassword(PasswordDto passwordDto)
            throws InvalidPasswordException, UserManagementOperationException {
        validatePassword(passwordDto);
        try {
            changePasswordInputPort.send(passwordDto);
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new UserManagementOperationException("Cannot change password", cause);
        }
    }

    @Override
    public void removeUser(User user)
            throws UserNotExistsException, UserManagementOperationException {
        if (user == null) {
            throw new UserManagementOperationException("User cannot be null");
        }
        userLock.lock();
        try {
            boolean exists = loadUsers().stream().anyMatch(existingUser -> existingUser.username().equals(user.username()));
            if (!exists) {
                throw new UserNotExistsException();
            }
            removeUserInputPort.send(user);
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new UserManagementOperationException("Cannot remove user", cause);
        } finally {
            userLock.unlock();
        }
    }

    private List<User> loadUsers() throws UserManagementOperationException {
        try {
            List<User> users = usersOutputPort.load();
            if (users == null) {
                return List.of();
            }
            if (users.stream().anyMatch(Objects::isNull)) {
                throw new UserManagementOperationException("Users cannot contain null elements");
            }
            return users;
        } catch (OutputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new UserManagementOperationException("Cannot load users", cause);
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
