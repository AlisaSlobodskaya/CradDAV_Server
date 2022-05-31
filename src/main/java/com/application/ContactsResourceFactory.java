package com.application;

import com.application.dao.ContactsDAO;
import com.application.dao.UsersDAO;
import com.application.resource.ContactManager;
import com.application.resource.RootResource;
import io.milton.common.Path;
import io.milton.http.HttpManager;
import io.milton.http.ResourceFactory;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.resource.CollectionResource;
import io.milton.resource.Resource;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ContactsResourceFactory implements ResourceFactory {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ContactsResourceFactory.class);
    private final ContactsDAO contactsDAO = new ContactsDAO();
    private final UsersDAO usersDAO = new UsersDAO();
    private final ContactManager contactManager = new ContactManager(contactsDAO, usersDAO);

    @Override
    public Resource getResource(String host, String url) throws NotAuthorizedException, BadRequestException {
        log.debug("getResource: url: " + url);
        Path path = Path.path(url);
        Resource r = find(path);
        log.debug("_found: " + r + " for url: " + url + " and path: " + path);
        return r;
    }

    private Resource find(Path path) throws NotAuthorizedException, BadRequestException {
        if (path.isRoot()) {
            RootResource r = (RootResource) HttpManager.request().getAttributes().get("rootResource");
            if( r == null ) {
                r = new RootResource(contactManager);
                HttpManager.request().getAttributes().put("rootResource", r);
            }

            return r;
        }
        Resource rParent = find(path.getParent());
        if (rParent == null) {
            return null;
        }
        if (rParent instanceof CollectionResource) {
            CollectionResource folder = (CollectionResource) rParent;
            return folder.child(path.getName());
        }
        return null;
    }
}