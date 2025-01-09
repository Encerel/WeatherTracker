package by.yankavets.service;

import by.yankavets.dao.impl.UserDao;
import by.yankavets.dto.user.UserCreateDto;
import by.yankavets.dto.user.UserLoginDto;
import by.yankavets.dto.user.UserReadDto;
import by.yankavets.exception.user.InvalidEmailOrPasswordException;
import by.yankavets.exception.user.PasswordsNotMatchException;
import by.yankavets.exception.user.UserAlreadyExistException;
import by.yankavets.mapper.user.UserCreateMapper;
import by.yankavets.mapper.user.UserReadMapper;
import by.yankavets.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private UserCreateMapper userCreateMapper;
    @Mock
    private UserReadMapper userReadMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private UserCreateDto userCreateDto;
    private UserReadDto userReadDto;
    private UserLoginDto userLoginDto;


    private static final Integer USER_ID = 1;
    private static final String USER_NAME = "Dummy name";
    private static final String USER_EMAIL = "test@gmail.com";
    private static final String USER_PASSWORD = "password";
    private static final String USER_WRONG_PASSWORD = "wrong_password";
    private static final String ENCRYPTED_PASSWORD = "encryptedPassword";
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(ENCRYPTED_PASSWORD)
                .build();

        userCreateDto = UserCreateDto.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .confirmedPassword(USER_PASSWORD)
                .build();

        userReadDto = UserReadDto.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        userLoginDto = UserLoginDto.builder()
                .email(USER_EMAIL)
                .password(USER_WRONG_PASSWORD)
                .build();

    }

    @Test
    void findById() {
        doReturn(Optional.of(mockUser)).when(userDao).findById(USER_ID);
        doReturn(userReadDto).when(userReadMapper).mapToDto(any(User.class));

        var actualResult = userService.findById(USER_ID);

        assertTrue(actualResult.isPresent());

        assertEquals(userReadDto, userReadMapper.mapToDto(actualResult.get()));

    }

    @Test
    void saveWhenUserWithEmailAlreadyExists() {
        mockUser.setEmail(USER_EMAIL);
        doReturn(mockUser).when(userDao).save(any(User.class));
        doReturn(Optional.of(mockUser)).when(userDao).findByEmail(USER_EMAIL);
        assertEquals(userDao.save(mockUser).getEmail(), userCreateDto.getEmail());
        Exception ex = assertThrows(UserAlreadyExistException.class, () -> userService.save(userCreateDto));
        assertEquals("User with email %s already exists".formatted(USER_EMAIL), ex.getMessage());

    }

    @Test
    void saveWhenPasswordsNotMatch() {

        UserCreateDto userCreateDto = UserCreateDto.builder()
                .password(USER_PASSWORD)
                .confirmedPassword(USER_PASSWORD + USER_PASSWORD)
                .build();

        Exception ex = assertThrows(PasswordsNotMatchException.class, () -> userService.save(userCreateDto));
        assertEquals("Passwords don't match!", ex.getMessage());
    }

    @Test
    void saveWithValidCredentials() {
        doReturn(Optional.empty()).when(userDao).findByEmail(USER_EMAIL);
        doReturn(ENCRYPTED_PASSWORD).when(passwordEncoder).encode(anyString());
        doReturn(mockUser).when(userCreateMapper).mapToEntity(any(UserCreateDto.class));
        doReturn(mockUser).when(userDao).save(any(User.class));
        doReturn(userReadDto).when(userReadMapper).mapToDto(mockUser);
        UserReadDto savedUser = userService.save(userCreateDto);

        assertEquals(userReadMapper.mapToDto(mockUser), savedUser);
    }

    @Test
    void findByEmailAndPasswordWhenWrongPassword() {

        doReturn(Optional.of(mockUser)).when(userDao).findByEmail(USER_EMAIL);
        doReturn(false).when(passwordEncoder).matches(anyString(), anyString());
        Exception ex = assertThrows(
                InvalidEmailOrPasswordException.class,
                () -> userService.findByEmailAndPassword(userLoginDto));

        assertEquals("Invalid email or password", ex.getMessage());
    }

    @Test
    void findByEmailAndPasswordWhenWrongEmail() {

        doReturn(Optional.empty()).when(userDao).findByEmail(USER_EMAIL);

        Exception ex = assertThrows(
                InvalidEmailOrPasswordException.class,
                () -> userService.findByEmailAndPassword(userLoginDto));

        assertEquals("Invalid email or password", ex.getMessage());
    }


    @Test
    void findByValidEmailAndPassword() {
        doReturn(Optional.of(mockUser)).when(userDao).findByEmail(USER_EMAIL);
        doReturn(true).when(passwordEncoder).matches(anyString(), anyString());

        UserReadDto foundUser = userService.findByEmailAndPassword(userLoginDto);

        assertEquals(userReadMapper.mapToDto(mockUser), foundUser);

    }

    @Test
    void findByIdWhenUserExists() {
        doReturn(Optional.of(mockUser)).when(userDao).findById(USER_ID);

        Optional<User> actualResult = userService.findById(USER_ID);

        assertTrue(actualResult.isPresent());
        assertEquals(mockUser, actualResult.get());
    }

    @Test
    void findByIdWhenUserDoesNotExist() {
        doReturn(Optional.empty()).when(userDao).findById(USER_ID);
        Optional<User> foundUser = userService.findById(USER_ID);

        assertFalse(foundUser.isPresent());
    }


}