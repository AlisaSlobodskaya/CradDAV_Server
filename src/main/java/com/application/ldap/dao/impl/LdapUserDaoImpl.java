package com.application.ldap.dao.impl;

import com.application.ldap.entity.LdapUser;
import com.application.ldap.dao.LdapUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository
public class LdapUserDaoImpl implements LdapUserDao {
    @Autowired
    private LdapTemplate ldapTemplate;

    @Override
    public List<String> findByUid(String uid) {
        return ldapTemplate
                .search(
                        "ou=users",
                        "uid=" + uid,
                        (AttributesMapper<String>) attrs -> attrs.get("cn").get().toString());
    }

    @Override
    public List<String> findAllPersonNames() {
        return ldapTemplate.search(
                query().where("objectclass").is("inetOrgPerson"),
                new AttributesMapper<String>() {
                    public String mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        return attrs.get("cn").get().toString();
                    }
                });
    }

    @Override
    public List<LdapUser> findAllPersons() {
        return ldapTemplate.search(
                query().where("objectclass").is("inetOrgPerson"),
                new LdapUserAttributesMapper());
    }

    private class LdapUserAttributesMapper implements AttributesMapper<LdapUser> {
        public LdapUser mapFromAttributes(Attributes attrs) throws NamingException {
            LdapUser user = LdapUser.builder()
                    .uid((String) attrs.get("uid").get())
                    .commonName((String) attrs.get("cn").get())
//                    .givenName((String) attrs.get("givenName").get())
//                    .surname((String) attrs.get("sn").get())
//                    .ruGivenName((String) attrs.get("ruGivenName").get())
//                    .ruSurname((String) attrs.get("ruSn").get())
//                    .mail((String) attrs.get("mail").get())
//                    .mobile((String) attrs.get("mobile").get())
                    .build();
            return user;
        }
    }
}
