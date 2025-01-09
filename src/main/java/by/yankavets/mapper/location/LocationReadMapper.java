package by.yankavets.mapper.location;

import by.yankavets.dto.location.LocationReadDto;
import by.yankavets.mapper.Mapper;
import by.yankavets.model.api.LocationByNameResponse;
import by.yankavets.model.entity.Location;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LocationReadMapper implements Mapper<Location, LocationReadDto> {

    @Override
    public Location mapToEntity(LocationReadDto dto) {
        return Location.builder()
                .name(dto.getName())
                .longitude(dto.getLongitude())
                .latitude(dto.getLatitude())
                .build();
    }

    public LocationReadDto fromApiEntityToDto(LocationByNameResponse entity) {
        return new LocationReadDto(
                entity.getLocationName(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getCountryCode(),
                entity.getState()
        );
    }

    public List<LocationReadDto> fromApiEntityListToDtoList(List<LocationByNameResponse> entities) {
        List<LocationReadDto> dtos = new ArrayList<>();

        for (LocationByNameResponse entity : entities) {
            dtos.add(fromApiEntityToDto(entity));
        }
        return dtos;
    }
}
