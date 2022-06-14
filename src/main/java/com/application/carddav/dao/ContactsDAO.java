package com.application.carddav.dao;

import com.application.carddav.entity.Contact;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public final class ContactsDAO {
    private final Set<Contact> contacts = new HashSet<Contact>();

    private long contactsVersion = 0; // used for the ctag

    public synchronized void incrementContactsVersion() {
        contactsVersion++;
    }

    public long getContactsVersion() {
        return contactsVersion;
    }

    public Contact addContact(String fileName) {
        return addContact(fileName, null, null, null, null);
    }

    public Contact addContact(String firstName, String lastName) {
        return addContact(firstName, lastName, null, null);
    }

    public Contact addContact(String firstName, String lastName, String email, String phone) {
        String fileName = UUID.randomUUID().toString();
        return addContact(fileName, firstName, lastName, email, phone);
    }

    public Contact addContact(String fileName,
                              String firstName,
                              String lastName,
                              String email,
                              String phone) {
        Contact contact = Contact.builder()
                .fileName(fileName)
                .uid(fileName)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .build();
        contacts.add(contact);
        return contact;
    }

    public Contact findContact(String uid) {
        for( Contact c : contacts) {
            if( c.getUid().equals(uid)) {
                return c;
            }
        }
        return null;
    }
}
