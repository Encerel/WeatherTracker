package by.yankavets.interceptor;

import by.yankavets.model.entity.SessionEntity;
import by.yankavets.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static by.yankavets.constant.ParametersAndAttribute.SESSION_UUID;
import static by.yankavets.constant.UrlPath.INDEX_URL;

@Component
public class RedirectIfAuthenticatedInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    @Autowired
    public RedirectIfAuthenticatedInterceptor(@Lazy SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();

        Optional<Cookie> sessionCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(SESSION_UUID))
                .findAny();


        if (sessionCookie.isPresent()) {
            Cookie session = sessionCookie.get();
            UUID sessionUUID = UUID.fromString(session.getValue());
            Optional<SessionEntity> foundSession = sessionService.findById(sessionUUID);

            if (foundSession.isPresent()) {
                SessionEntity sessionEntity = foundSession.get();

                if (sessionEntity.getExpireAt().isBefore(LocalDateTime.now())) {
                    return true;
                } else {
                    String prevPage = request.getHeader("referer");
                    response.sendRedirect(prevPage != null ? prevPage : INDEX_URL);
                    return false;
                }
            }

        }
        return true;
    }


}
