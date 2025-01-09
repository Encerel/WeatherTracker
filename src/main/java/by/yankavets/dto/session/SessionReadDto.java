package by.yankavets.dto.session;

import by.yankavets.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class SessionReadDto {

    private UUID id;
    private User user;
    private LocalDateTime expireAt;

}
