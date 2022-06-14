package com.application.ldap.dao.impl;

import com.application.ldap.entity.LdapUser;
import com.application.ldap.dao.LdapUserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@TestPropertySource(locations="classpath:application-test.properties")
class LdapUserRepositoryImplTest {
    @Autowired
    LdapUserDao ldapUserRepository;

    private static final String USER_UID_1 = "alena.kozlova";
    private static final String USER_COMMON_NAME_1 = "Alena Kozlova";
    private static final int NUMBER_OF_ALL_USERS = 364;

    @Test
    void findByUid_SuccessfulCase_CommonNameMatches() {
        List<String> result = ldapUserRepository.findByUid(USER_UID_1);
        assertThat(result).isEqualTo(List.of(USER_COMMON_NAME_1));
    }

    @Test
    void findAllPersonNames_SuccessfulCase_NumberOfNamesMatches() {
        List<String> result = ldapUserRepository.findAllPersonNames();
        assertThat(result.size()).isEqualTo(NUMBER_OF_ALL_USERS);
    }

    @Test
    void findAllPersons_SuccessfulCase_NumberOfPersonsMatches() {
        List<LdapUser> result = ldapUserRepository.findAllPersons();
        assertThat(result.size()).isEqualTo(NUMBER_OF_ALL_USERS);
    }
}