package by.yankavets.mapper.session;

import by.yankavets.dto.session.SessionReadDto;
import by.yankavets.mapper.Mapper;
import by.yankavets.model.entity.SessionEntity;
import org.springframework.stereotype.Component;

@Component
public class SessionReadMapper implements Mapper<SessionEntity, SessionReadDto> {


    @Override
    public SessionReadDto mapToDto(SessionEntity entity) {
        return new SessionReadDto(
                entity.getId(),
                entity.getUser(),
                entity.getExpireAt()
        );
    }

    @Override
    public SessionEntity mapToEntity(SessionReadDto dto) {
        return new SessionEntity(
                dto.getId(),
                dto.getUser(),
                dto.getExpireAt()
        );
    }
}
