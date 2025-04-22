package com.example.todoManager.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "todo")
public class Todo {
    @Id
    @Column(name = "uuid", nullable = false)
    private String uuid;

    private String title;
    private String description;

    @Column(name = "todo_owner_uuid")
    private String todoOwnerUuid;


    public void setOwner(TodoOwner resolve) {
        this.todoOwnerUuid = resolve.getUuid();
    }

}