package by.yankavets.mapper;

import by.yankavets.dto.UserCreateDto;
import by.yankavets.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserCreateMapper implements Mapper<UserCreateDto, User> {
    @Override
    public User mapToDto(UserCreateDto entity) {
        return User.builder()
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .build();
    }
}
