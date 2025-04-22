package com.example.todoManager.devTools;

import com.example.todoManager.model.Group;
import com.example.todoManager.model.Todo;
import com.example.todoManager.model.TodoOwner;
import com.example.todoManager.model.User;
import com.example.todoManager.service.GroupService;
import com.example.todoManager.service.TodoService;
import com.example.todoManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
public class DatabaseInitializer {
    private final UserService userService;
    private final TodoService todoService;
    private final GroupService groupService;

    @Autowired
    public DatabaseInitializer(UserService userService, TodoService todoService, GroupService groupService) {
        this.userService = userService;
        this.todoService = todoService;
        this.groupService = groupService;
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {

        // === USERS ===
        User user1 = User.builder()
                .uuid("user-1")
                .name("admin")
                .email("admin@admin.at")
                .password("password")
                .build();

        User user2 = User.builder()
                .uuid("user-2")
                .name("user2")
                .email("user2@user.at")
                .password("password")
                .build();

        User user3 = User.builder()
                .uuid("user-3")
                .name("user3")
                .email("user3@user.at")
                .password("password")
                .build();

        userService.save(user1);
        userService.save(user2);
        userService.save(user3);

// === GROUPS ===
        Group group1 = Group.builder()
                .uuid("group-1")
                .name("Group1")
                .users(List.of(user1, user2))
                .build();

        Group group2 = Group.builder()
                .uuid("group-2")
                .name("Group2")
                .users(List.of(user2, user3))
                .build();

        groupService.save(group1);
        groupService.save(group2);

// === TODOS ===
        Todo todo1 = createTodo("todo-1", "Todo1", "User-owned todo", user1);
        Todo todo2 = createTodo("todo-2", "Todo2", "Group-owned todo", group1);
        Todo todo3 = createTodo("todo-3", "Todo3", "Another user-owned todo", user3);
        Todo todo4 = createTodo("todo-4", "Todo4", "Yet another group task", group2);
        Todo todo5 = createTodo("todo-5", "Todo5", "Admin’s secret task", user1);

        todoService.save(todo1);
        todoService.save(todo2);
        todoService.save(todo3);
        todoService.save(todo4);
        todoService.save(todo5);



    }

    private Todo createTodo(String uuid, String title, String description, TodoOwner owner) {
        Todo todo = Todo.builder()
                .uuid(uuid)
                .title(title)
                .description(description)
                .build();
        todo.setOwner(owner); // this will set owner_uuid and owner_type
        return todo;
    }

}
