package pl.vtt.wpi.core.application.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.exception.InvalidPasswordException;
import pl.vtt.wpi.core.application.exception.UserAlreadyExistsException;
import pl.vtt.wpi.core.application.exception.UserManagementOperationException;
import pl.vtt.wpi.core.infrastructure.dto.PasswordDto;
import pl.vtt.wpi.core.infrastructure.dto.UserCreateRequest;
import pl.vtt.wpi.core.domain.model.User;
import pl.vtt.wpi.core.domain.model.endpoint.UserGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserManagementServiceImplTest {

    @Test
    void createUser_success_sendsUserCreateRequest() throws Exception {
        User user = new User("john", EnumSet.of(UserGroup.ADMIN));
        PasswordDto passwordDto = new PasswordDto("secret", "secret");
        AtomicReference<UserCreateRequest> sent = new AtomicReference<>();

        UserManagementServiceImpl service = new UserManagementServiceImpl(
                List::<User>of,
                sent::set,
                _ -> {},
                _ -> {}
        );

        service.createUser(user, passwordDto);

        assertEquals(new UserCreateRequest(user, passwordDto), sent.get());
    }

    @Test
    void createUser_existingUser_throwsUserAlreadyExistsException() throws Exception {
        User user = new User("john", EnumSet.of(UserGroup.ADMIN));
        PasswordDto passwordDto = new PasswordDto("secret", "secret");

        UserManagementServiceImpl service = new UserManagementServiceImpl(
                () -> List.of(user),
                _ -> {},
                _ -> {},
                _ -> {}
        );

        assertThrows(UserAlreadyExistsException.class, () -> service.createUser(user, passwordDto));
    }

    @Test
    void createUser_loadedNullUser_throwsUserManagementOperationException() {
        User user = new User("john", EnumSet.of(UserGroup.ADMIN));
        PasswordDto passwordDto = new PasswordDto("secret", "secret");

        UserManagementServiceImpl service = new UserManagementServiceImpl(
                () -> new ArrayList<>(Collections.singletonList(null)),
                _ -> {},
                _ -> {},
                _ -> {}
        );

        UserManagementOperationException exception = assertThrows(UserManagementOperationException.class,
                () -> service.createUser(user, passwordDto));

        assertEquals("Users cannot contain null elements", exception.getMessage());
    }

    @Test
    void removeUser_loadedNullUser_throwsUserManagementOperationException() {
        User user = new User("john", EnumSet.of(UserGroup.ADMIN));

        UserManagementServiceImpl service = new UserManagementServiceImpl(
                () -> new ArrayList<>(Collections.singletonList(null)),
                _ -> {},
                _ -> {},
                _ -> {}
        );

        UserManagementOperationException exception = assertThrows(UserManagementOperationException.class,
                () -> service.removeUser(user));

        assertEquals("Users cannot contain null elements", exception.getMessage());
    }

    @Test
    void changePassword_blankPassword_throwsInvalidPasswordException() {
        UserManagementServiceImpl service = new UserManagementServiceImpl(
                List::<User>of,
                _ -> {},
                _ -> {},
                _ -> {}
        );

        assertThrows(InvalidPasswordException.class,
                () -> service.changePassword(new PasswordDto(" ", " ")));
    }
}
