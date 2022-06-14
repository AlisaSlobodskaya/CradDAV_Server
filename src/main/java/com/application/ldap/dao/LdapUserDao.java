package com.application.ldap.dao;

import com.application.ldap.entity.LdapUser;

import java.util.List;

public interface LdapUserDao {
    List<String> findByUid(String uid);
    List<String> findAllPersonNames();
    List<LdapUser> findAllPersons();
}
