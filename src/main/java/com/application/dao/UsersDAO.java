package com.application.dao;

import com.application.entity.User;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class UsersDAO {
    private final Set<User> users = new HashSet<User>();

    public UsersDAO() {
        addUser("admin", "password");
        addUser("user", "hello_world");
    }

    public User addUser(String userName, String password) {
        User user = User.builder()
                .userName(userName)
                .password(password)
                .build();
        users.add(user);
        return user;
    }

    public final User findUser(String username) {
        for(User user : users ) {
            if( user.getUserName().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
