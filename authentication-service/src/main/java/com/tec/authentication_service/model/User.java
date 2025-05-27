package com.tec.authentication_service.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;

    public User() {
        // JPA needs this default constructor
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
