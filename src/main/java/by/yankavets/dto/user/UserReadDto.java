package by.yankavets.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserReadDto {

    private Integer id;
    private String name;
    private String email;
    private UUID sessionId;
}
