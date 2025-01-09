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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserDao userDao;
    private final UserCreateMapper userCreateMapper;
    private final UserReadMapper userReadMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, UserCreateMapper userCreateMapper, UserReadMapper userReadMapper, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.userCreateMapper = userCreateMapper;
        this.userReadMapper = userReadMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserReadDto save(UserCreateDto userCreateDto) {

        Optional<User> userByEmail = userDao.findByEmail(userCreateDto.getEmail());

        if (userByEmail.isPresent()) {
            throw new UserAlreadyExistException(userCreateDto.getEmail());
        }

        if (!userCreateDto.getPassword().equals(userCreateDto.getConfirmedPassword())) {
            throw new PasswordsNotMatchException();
        }

        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        User mappedUser = userCreateMapper.mapToEntity(userCreateDto);


        return userReadMapper.mapToDto(userDao.save(mappedUser));
    }


    public UserReadDto findByEmailAndPassword(UserLoginDto userLoginDto) {

        Optional<User> foundUser = userDao.findByEmail(userLoginDto.getEmail());

        if (foundUser.isPresent()) {
            User user = foundUser.get();
            if (passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
                return userReadMapper.mapToDto(user);
            }
        }
        throw new InvalidEmailOrPasswordException();
    }

    public Optional<User> findById(Integer userId) {
        return userDao.findById(userId);
    }
}
