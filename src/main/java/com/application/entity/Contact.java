package com.application.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contact {
    private String fileName;
    private String uid;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String iCalData;
}
