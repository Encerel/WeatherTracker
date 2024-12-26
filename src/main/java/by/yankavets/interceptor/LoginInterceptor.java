package by.yankavets.interceptor;

import by.yankavets.dto.UserReadDto;
import by.yankavets.entity.SessionEntity;
import by.yankavets.exception.InvalidSessionException;
import by.yankavets.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static by.yankavets.constant.ParametersAndAttribute.*;
import static by.yankavets.constant.UrlPath.SIGN_IN_URL;


@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    @Autowired
    public LoginInterceptor(@Lazy SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        try {
            if (session != null && session.getAttribute(USER) != null) {
                UserReadDto userReadDto = (UserReadDto) session.getAttribute(USER);
                Optional<UUID> sessionId = findSessionId(request.getCookies());
                if (sessionId.isPresent()) {
                    Optional<SessionEntity> foundSession = sessionService.findByUserAndSessionId(
                            userReadDto,
                            sessionId.get()
                    );
                    if (foundSession.isPresent()) {
                        SessionEntity sessionEntity = foundSession.get();
                        if (sessionEntity.getExpireAt().isBefore(LocalDateTime.now())) {
                            session.invalidate();
                            throw new InvalidSessionException();
                        }
                        return true;
                    }
                }
            }
        } catch (RuntimeException e) {
            request.setAttribute(ERROR, e.getMessage());
            request.getRequestDispatcher(SIGN_IN_URL).forward(request, response);
            return false;
        }

        reject(request, response);
        return false;
    }

    private Optional<UUID> findSessionId(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_UUID)) {
                return Optional.of(UUID.fromString(cookie.getValue()));
            }
        }
        return Optional.empty();
    }

    private void reject(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String prevPage = request.getHeader("referer");
        response.sendRedirect(prevPage != null ? prevPage : SIGN_IN_URL);
    }


}