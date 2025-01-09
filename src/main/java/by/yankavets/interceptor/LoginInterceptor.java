package by.yankavets.interceptor;

import by.yankavets.exception.session.InvalidSessionException;
import by.yankavets.exception.session.SessionExpiredException;
import by.yankavets.model.entity.SessionEntity;
import by.yankavets.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static by.yankavets.constant.ParametersAndAttribute.ERROR;
import static by.yankavets.constant.ParametersAndAttribute.SESSION_UUID;
import static by.yankavets.constant.UrlPath.SIGN_IN_URL;
import static by.yankavets.constant.UrlPath.SIGN_UP_URL;


@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;
    private static final Set<String> PUBLIC_PATH = Set.of(SIGN_IN_URL, SIGN_UP_URL);
    @Autowired
    public LoginInterceptor(@Lazy SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            Optional<UUID> sessionId = findSessionId(request.getCookies());

            if (sessionId.isEmpty()) {
                reject(request, response);
                return false;
            }

            UUID uuid = sessionId.get();
            Optional<SessionEntity> foundSession = sessionService.findById(uuid);

            if (foundSession.isEmpty()) {
                throw new InvalidSessionException();
            }

            SessionEntity sessionEntity = foundSession.get();

            if (sessionEntity.getExpireAt().isBefore(LocalDateTime.now())) {
                throw new SessionExpiredException();
            }
            return true;
        } catch (RuntimeException e) {
            request.setAttribute(ERROR, e.getMessage());
            request.getRequestDispatcher(SIGN_IN_URL).forward(request, response);
            return false;
        }
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
        response.sendRedirect(prevPage != null && isPublicPath(prevPage) ? prevPage : SIGN_IN_URL);
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATH.stream().anyMatch(path::startsWith);
    }


}