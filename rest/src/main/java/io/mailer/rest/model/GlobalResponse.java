package io.mailer.rest.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class GlobalResponse {
    
    @Default private Date timestamp = new Date();
    @Default private HttpStatus status = HttpStatus.OK;
    private List<String> errors;
    private Object payload;

    public String getStatus() {
        return status == null ? "0 Unknown" : status.toString();
    }

}