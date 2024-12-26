package by.yankavets.service;

import by.yankavets.dao.impl.SessionDao;
import by.yankavets.dto.UserReadDto;
import by.yankavets.entity.SessionEntity;
import by.yankavets.entity.User;
import by.yankavets.exception.InvalidSessionException;
import by.yankavets.exception.UserNotWithSuchIdNotFoundException;
import by.yankavets.mapper.UserReadMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SessionService {

    private final SessionDao sessionDao;
    private final UserService userService;
    private final UserReadMapper userReadMapper;

    public SessionService(SessionDao sessionDao, UserService userService, UserReadMapper userReadMapper) {
        this.sessionDao = sessionDao;
        this.userService = userService;
        this.userReadMapper = userReadMapper;
    }

    @Transactional
    public SessionEntity createByUserId(Integer userId) {
        SessionEntity sessionEntity = new SessionEntity();
        Optional<User> foundUser = userService.findById(userId);
        if (foundUser.isPresent()) {
            sessionEntity.setUser(foundUser.get());
            sessionEntity.setExpireAt(LocalDateTime.now().plusDays(1));
            return sessionDao.save(sessionEntity);
        }
       throw new UserNotWithSuchIdNotFoundException(userId);
    }

    public Optional<SessionEntity> findByUserAndSessionId(UserReadDto userDto, UUID sessionId) {

        Optional<User> foundUser = userService.findById(userDto.getId());
        if (foundUser.isPresent()) {
            return sessionDao.findByUserIdAndSessionId(userReadMapper.mapToEntity(userDto), sessionId);
        }
        throw new InvalidSessionException();
    }
}
