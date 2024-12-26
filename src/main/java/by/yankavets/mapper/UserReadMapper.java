package by.yankavets.mapper;

import by.yankavets.dto.UserReadDto;
import by.yankavets.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {
    @Override
    public UserReadDto mapToDto(User entity) {
        return new UserReadDto(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                null
        );
    }


    @Override
    public User mapToEntity(UserReadDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }
}
