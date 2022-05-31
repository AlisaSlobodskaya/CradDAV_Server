package com.application.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String userName;
    private String password;
}
