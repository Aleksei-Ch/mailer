package io.mailer.rest.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "api")
@Data
public class ApiKey implements Serializable {
    
    private static final long serialVersionUID = -1219175487392924231L;

    @Id
    private Long id;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "enabled")
    private Enabled enabled;

    @Column(name = "notes")
    private String notes;

    // In DataBase N = 0, Y = 1 
    public enum Enabled { N, Y }

}