package com.application.carddav.resource;

import com.application.carddav.dao.ContactsDAO;
import com.application.carddav.dao.UsersDAO;
import com.application.carddav.entity.Contact;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.cardme.engine.VCardEngine;
import net.sourceforge.cardme.vcard.VCard;
import net.sourceforge.cardme.vcard.features.EmailFeature;
import net.sourceforge.cardme.vcard.features.TelephoneFeature;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Getter
@Slf4j
@RequiredArgsConstructor
public class ContactManager {

    private final ContactsDAO contactsDao;
    private final UsersDAO usersDAO;

    public Contact createContact(String newName, String icalData) throws UnsupportedEncodingException {
        Contact contact = contactsDao.addContact(newName);
        update(contact, icalData);
        return contact;
    }

    public void update(Contact contact, String data) {
        log.info("update: " + data);
        VCard vcard = parse(data);
        if (vcard.getUID() != null && vcard.getUID().hasUID()) {
            contact.setUid(vcard.getUID().getUID());
        } else {
            log.warn("NO UID FOUND, making up our own");
            contact.setUid(UUID.randomUUID().toString());
        }
        if (vcard.getName() != null) {
            contact.setFirstName(vcard.getName().getGivenName());
            contact.setLastName(vcard.getName().getFamilyName());
            log.info("parsed name: " + contact.getFirstName() + " " + contact.getLastName());
        } else {
            log.warn("No name component found!");
        }
        contact.setEmail(""); // reset in case none given
        Iterator<EmailFeature> it = vcard.getEmails();
        while (it.hasNext()) {
            contact.setEmail(it.next().getEmail());
        }
        log.info("email: " + contact.getEmail());
        if (vcard.getOrganizations() != null) {
//            contact.setOrganizationName("");
//            Iterator<String> itOrg = vcard.getOrganizations().getOrganizations();
//            while (itOrg.hasNext()) {
//                contact.setOrganizationName(itOrg.next());
//            }
        }
        String ph = getPhone(vcard);
        contact.setPhone(ph);
        log.info("phone: " + contact.getPhone());
        contact.setICalData(data);
        contactsDao.incrementContactsVersion();
    }

    public VCard parse(String data) {
        VCardEngine cardEngine = new VCardEngine();
        try {
            return cardEngine.parse(data);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getPhone(VCard vcard) {
        Iterator<TelephoneFeature> itPhone = vcard.getTelephoneNumbers();
        while (itPhone.hasNext()) {
            return itPhone.next().getTelephone();
        }
        return null;
    }

    public List<VCard> parseMultiple(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, bout);
        String vcardText = bout.toString("UTF-8");
        VCardEngine cardEngine = new VCardEngine();
        List<VCard> vcards = cardEngine.parseMultiple(vcardText);
        return vcards;
    }
}