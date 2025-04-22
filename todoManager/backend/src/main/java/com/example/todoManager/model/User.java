package com.example.todoManager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements TodoOwner{
    @Id
    @Column(name = "uuid", nullable = false)
    private String uuid;

    private String name;
    private String email;
    private String password;

    @ManyToMany(mappedBy = "users")
    private List<Group> groups = new ArrayList<>();

    @OneToMany(mappedBy = "todoOwnerUuid")
    private List<Todo> todos = new ArrayList<>();
}