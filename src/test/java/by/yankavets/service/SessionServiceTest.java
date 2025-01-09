package by.yankavets.service;

import by.yankavets.dao.impl.SessionDao;
import by.yankavets.dto.user.UserReadDto;
import by.yankavets.exception.session.InvalidSessionException;
import by.yankavets.exception.user.UserWithSuchIdNotFoundException;
import by.yankavets.mapper.user.UserReadMapper;
import by.yankavets.model.entity.SessionEntity;
import by.yankavets.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionDao sessionDao;
    @Mock
    private UserService userService;
    @Mock
    private UserReadMapper userReadMapper;
    @InjectMocks
    private SessionService sessionService;

    private SessionEntity mockSession;
    private static final UUID SESSION_ID = UUID.randomUUID();
    private User mockUser;
    private static final Integer USER_ID = 1;
    private static final String USER_NAME = "Dummy name";
    private static final String USER_EMAIL = "test@gmail.com";
    private static final String USER_PASSWORD = "password";
    private UserReadDto userReadDto;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
        mockSession = SessionEntity.builder()
                .id(SESSION_ID)
                .user(mockUser)
                .expireAt(LocalDateTime.now().plusHours(1))
                .build();
        userReadDto = UserReadDto.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .sessionId(SESSION_ID)
                .build();
    }

    @Test
    void createByUserIdWhenUserExists() {
        doReturn(Optional.of(mockUser)).when(userService).findById(USER_ID);
        doReturn(mockSession).when(sessionDao).save(any(SessionEntity.class));

        SessionEntity actualResult = sessionService.createByUserId(USER_ID);
        assertEquals(mockSession, actualResult);
    }

    @Test
    void createByUserIdWhenUserNotFound() {
        doReturn(Optional.empty()).when(userService).findById(USER_ID);

        Exception ex = assertThrows(
                UserWithSuchIdNotFoundException.class,
                () -> sessionService.createByUserId(USER_ID)
        );

        assertEquals("User with id %d not found!".formatted(USER_ID), ex.getMessage());
    }

    @Test
    void createByUserIdWhenUserIdIsNull() {
        Exception ex = assertThrows(
                UserWithSuchIdNotFoundException.class,
                () -> sessionService.createByUserId(null)
        );

        assertEquals("Invalid user id!", ex.getMessage());
    }

    @Test
    void findByIdWhenSessionExists() {
        doReturn(Optional.of(mockSession)).when(sessionDao).findById(SESSION_ID);

        Optional<SessionEntity> actualResult = sessionService.findById(SESSION_ID);
        assertTrue(actualResult.isPresent());
        assertEquals(mockSession, actualResult.get());
    }

    @Test
    void findByIdWhenSessionNotFound() {
        doReturn(Optional.empty()).when(sessionDao).findById(SESSION_ID);

        Optional<SessionEntity> actualResult = sessionService.findById(SESSION_ID);
        assertFalse(actualResult.isPresent());
    }

    @Test
    void findByIdWhenSessionIdIsNull() {

        Exception ex = assertThrows(
                InvalidSessionException.class,
                () -> sessionService.findById(null
                ));

        assertEquals("Invalid session!", ex.getMessage());
    }

    @Test
    void findUserByIdWhenSessionAndUserAreValidSession() {
        doReturn(Optional.of(mockSession)).when(sessionDao).findById(SESSION_ID);
        doReturn(Optional.of(mockUser)).when(userService).findById(USER_ID);
        doReturn(userReadDto).when(userReadMapper).mapToDto(mockUser);

        UserReadDto foundUser = sessionService.findUserBySessionId(SESSION_ID);

        assertEquals(userReadMapper.mapToDto(mockUser), foundUser);

    }

    @Test
    void findUserBySessionIdWhenSessionIsInvalid() {
        doReturn(Optional.empty()).when(sessionDao).findById(SESSION_ID);


        Exception ex = assertThrows(
                InvalidSessionException.class,
                () -> sessionService.findUserBySessionId(SESSION_ID)
        );

        assertEquals("Invalid session!", ex.getMessage());

    }

    @Test
    void findUserByIdWhenSessionIsValidAndUserNotFoundSession() {
        doReturn(Optional.of(mockSession)).when(sessionDao).findById(SESSION_ID);
        doReturn(Optional.empty()).when(userService).findById(USER_ID);

        Exception ex = assertThrows(
                UserWithSuchIdNotFoundException.class,
                () -> sessionService.findUserBySessionId(SESSION_ID)
        );
        assertEquals("User with id %d not found!".formatted(USER_ID), ex.getMessage());

    }

    @Test
    void deleteById() {
        sessionService.deleteById(SESSION_ID);
        verify(sessionDao, times(1)).delete(SESSION_ID);

    }
}