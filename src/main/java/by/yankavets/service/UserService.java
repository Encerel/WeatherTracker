package by.yankavets.service;

import by.yankavets.dao.impl.UserDao;
import by.yankavets.dto.UserCreateDto;
import by.yankavets.dto.UserLoginDto;
import by.yankavets.dto.UserReadDto;
import by.yankavets.entity.User;
import by.yankavets.exception.InvalidEmailOrPasswordException;
import by.yankavets.exception.PassworsdNotMatchException;
import by.yankavets.exception.UserAlreadyExistException;
import by.yankavets.mapper.UserCreateMapper;
import by.yankavets.mapper.UserReadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
            throw new PassworsdNotMatchException();
        }

        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        User mappedUser = userCreateMapper.map(userCreateDto);


        return userReadMapper.map(userDao.save(mappedUser));
    }


    public UserReadDto findByEmailAndPassword(UserLoginDto userLoginDto) {

        Optional<User> foundUser = userDao.findByEmail(userLoginDto.getEmail());

        if (foundUser.isPresent()) {
            User user = foundUser.get();
            if (passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
                return userReadMapper.map(user);
            }
        }
        throw new InvalidEmailOrPasswordException();
    }

    public Optional<User> findById(Integer userId) {
        return userDao.findById(userId);
    }
}
