package com.file.uploader.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    // For Authentication

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "number_of_files")
    private Long number_of_files;

}
