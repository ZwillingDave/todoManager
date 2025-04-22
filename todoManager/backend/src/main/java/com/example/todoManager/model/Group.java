package com.example.todoManager.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "groups")
public class Group implements TodoOwner{
    @Id
    private String uuid;

    private String name;

    @OneToMany(mappedBy = "todoOwnerUuid")
    private List<Todo> todos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "users_group",
            joinColumns = @JoinColumn(name = "group_uuid"),
            inverseJoinColumns = @JoinColumn(name = "user_uuid")
    )
    private List<User> users = new ArrayList<>();


}
