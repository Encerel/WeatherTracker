package by.yankavets.service;

import by.yankavets.dao.impl.SessionDao;
import by.yankavets.dto.user.UserReadDto;
import by.yankavets.exception.session.InvalidSessionException;
import by.yankavets.exception.user.UserWithSuchIdNotFoundException;
import by.yankavets.mapper.user.UserReadMapper;
import by.yankavets.model.entity.SessionEntity;
import by.yankavets.model.entity.User;
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

        if (userId == null) {
            throw new UserWithSuchIdNotFoundException();
        }

        SessionEntity sessionEntity = new SessionEntity();
        Optional<User> foundUser = userService.findById(userId);
        if (foundUser.isPresent()) {
            sessionEntity.setUser(foundUser.get());
            sessionEntity.setExpireAt(LocalDateTime.now().plusDays(1));
            return sessionDao.save(sessionEntity);
        }
       throw new UserWithSuchIdNotFoundException(userId);
    }

    public Optional<SessionEntity> findById(UUID id) {
        if (id == null) {
            throw new InvalidSessionException();
        }
        return sessionDao.findById(id);
    }

    public UserReadDto findUserBySessionId(UUID sessionId) {

        Optional<SessionEntity> foundSession = sessionDao.findById(sessionId);
        if (foundSession.isEmpty()) {
            throw new InvalidSessionException();
        }

        Optional<User> foundUser = userService.findById(foundSession.get().getUser().getId());

        if (foundUser.isEmpty()) {
            throw new UserWithSuchIdNotFoundException(foundSession.get().getUser().getId());
        }


        return userReadMapper.mapToDto(foundUser.get());
    }

    public void deleteById(UUID sessionId) {
        sessionDao.delete(sessionId);
    }
}
