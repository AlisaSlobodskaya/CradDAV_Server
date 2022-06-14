package com.application.ldap.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LdapUser {
    private String uid;
    private String commonName;
//    private String givenName;
//    private String surname;
//    private String ruGivenName;
//    private String ruSurname;
//    private String mail;
//    private String mobile;
//    private LocalDate birthdate;
}
