package by.yankavets.mapper.location;

import by.yankavets.dto.location.LocationCreateDeleteDto;
import by.yankavets.exception.user.UserWithSuchIdNotFoundException;
import by.yankavets.mapper.Mapper;
import by.yankavets.model.entity.Location;
import by.yankavets.model.entity.User;
import by.yankavets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Component
public class LocationCreateMapper implements Mapper<Location, LocationCreateDeleteDto> {

    private final UserService userService;

    @Autowired
    public LocationCreateMapper(UserService userService) {
        this.userService = userService;
    }

    @Override
    public LocationCreateDeleteDto mapToDto(Location entity) {
        return new LocationCreateDeleteDto(
                entity.getName(),
                entity.getUser().getId(),
                entity.getLatitude(),
                entity.getLatitude()
        );
    }

    @Override
    public Location mapToEntity(LocationCreateDeleteDto dto) {
        BigDecimal latitude = dto.getLatitude().setScale(4, RoundingMode.HALF_UP);
        BigDecimal longitude = dto.getLongitude().setScale(4, RoundingMode.HALF_UP);
        return Location.builder()
                .name(dto.getName())
                .latitude(latitude)
                .longitude(longitude)
                .user(findUserById(dto.getUserId()))
                .build();
    }

    private User findUserById(Integer id) {
        Optional<User> foundUser = userService.findById(id);
        return foundUser.orElseThrow(() -> new UserWithSuchIdNotFoundException(id));
    }
}
