package ru.mailserver.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalResponse {
    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS", timezone = "Europe/Moscow")
    private Instant timestamp = Instant.now();
    @Builder.Default
    private HttpStatus status = HttpStatus.OK;
    private Object payload;
}
