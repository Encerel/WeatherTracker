package by.yankavets.mapper.user;

import by.yankavets.dto.user.UserCreateDto;
import by.yankavets.mapper.Mapper;
import by.yankavets.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserCreateMapper implements Mapper<User, UserCreateDto> {

    @Override
    public User mapToEntity(UserCreateDto entity) {
        return User.builder()
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .build();
    }

}
