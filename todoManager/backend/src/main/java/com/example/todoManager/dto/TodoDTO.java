package com.example.todoManager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class TodoDTO {
    private String uuid;
    private String title;
    private String description;
//    private String todoOwnerUuid;
}
