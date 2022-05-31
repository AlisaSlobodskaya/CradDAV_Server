package com.application.resource;

import com.application.entity.User;
import io.milton.http.Auth;
import io.milton.http.Request;
import io.milton.http.Request.Method;
import io.milton.http.http11.auth.DigestGenerator;
import io.milton.http.http11.auth.DigestResponse;
import io.milton.resource.CollectionResource;
import io.milton.resource.DigestResource;
import io.milton.resource.PropFindableResource;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public abstract class AbstractResource implements DigestResource, PropFindableResource {

    private AbstractResource parent;
    protected final ContactManager contactManager;

    public AbstractResource(ContactManager contactManager) {
        this.parent = null;
        this.contactManager = contactManager;
    }

    public AbstractResource(AbstractResource parent, ContactManager contactManager) {
        this.parent = parent;
        this.contactManager = contactManager;
    }

    @Override
    public Object authenticate(String userName, String requestedPassword) {
        User user = contactManager.getUsersDAO().findUser(userName);
        if( user != null ) {
            if( user.getPassword().equals(requestedPassword)) {
                return getRoot().child(userName); // return the webdav resource for this object
            }
        }
        return null;
    }

    @Override
    public Object authenticate(DigestResponse digestRequest) {
        User user = contactManager.getUsersDAO().findUser(digestRequest.getUser());
        if (user != null) {
            DigestGenerator gen = new DigestGenerator();
            String actual = gen.generateDigest(digestRequest, user.getPassword());
            if (actual.equals(digestRequest.getResponseDigest())) {
                return getRoot().child(digestRequest.getUser()); // return the webdav resource for this object
            } else {
                log.warn("that password is incorrect. Try '" + user.getPassword() + "'");
            }
        } else {
            log.warn("user not found: " + digestRequest.getUser() + " - try 'user'");
        }
        return null;
    }

    @Override
    public String getUniqueId() {
        return null;
    }

    @Override
    public String checkRedirect(Request request) {
        return null;
    }

    @Override
    public boolean authorise(Request request, Method method, Auth auth) {
        log.debug("authorise");
        return auth != null;
    }

    @Override
    public String getRealm() {
        return "testrealm@host.com";
    }

    @Override
    public Date getModifiedDate() {
        return null;
    }

    @Override
    public Date getCreateDate() {
        return null;
    }

    @Override
    public boolean isDigestAllowed() {
        return true;
    }

    public String getHref() {
        if( parent == null ) {
            return "/";
        } else {
            String href = parent.getHref() + getName();
            if( this instanceof CollectionResource) {
                href += "/";
            }
            return href;
        }
    }

    public RootResource getRoot() {
        if( this instanceof RootResource) {
            return (RootResource) this;
        } else {
            return parent.getRoot();
        }
    }
}