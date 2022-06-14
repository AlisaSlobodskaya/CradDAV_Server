package com.application.carddav.resource;

import com.application.carddav.entity.Contact;
import io.milton.http.Auth;
import io.milton.http.Range;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.ConflictException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.resource.GetableResource;
import io.milton.resource.ICalResource;
import io.milton.resource.ReplaceableResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Slf4j
public class ContactResource extends AbstractResource implements GetableResource, ReplaceableResource, ICalResource {

    private final UserAddressBookResource parent;
    private final Contact contact;

    public ContactResource(UserAddressBookResource parent, Contact contact) {
        super(parent, parent.contactManager);
        this.parent = parent;
        this.contact = contact;
    }

    @Override
    public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException {
        // we assume that the icalData on the Person object is consistent with other properties
        String ical = contact.getICalData();
        if( ical != null ) {
            out.write(ical.getBytes("UTF-8"));
        } else {
            log.warn("ICAL data is null on resource: " + contact.getFileName());
        }
    }

    @Override
    public void replaceContent(InputStream in, Long length) throws BadRequestException, ConflictException, NotAuthorizedException {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            IOUtils.copy(in, bout);
            String icalData = bout.toString("UTF-8");
            contactManager.update(contact, icalData);
        } catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    @Override
    public Long getContentLength() {
        return null;
    }

    @Override
    public String getContentType(String accept) {
        return "text/vcard";
    }

    @Override
    public String getName() {
        return contact.getFileName();
    }

    @Override
    public Long getMaxAgeSeconds(Auth auth) {
        return null;
    }

    @Override
    public String getICalData() {
        return contact.getICalData();
    }

    @Override
    public String getUniqueId() {
        return contact.getUid();
    }
}