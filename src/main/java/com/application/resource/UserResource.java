package com.application.resource;

import com.application.entity.User;
import com.application.utils.ChildUtils;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.http.values.HrefList;
import io.milton.principal.CardDavPrincipal;
import io.milton.principal.HrefPrincipleId;
import io.milton.resource.CollectionResource;
import io.milton.resource.Resource;

import java.util.ArrayList;
import java.util.List;

//each user will be represented by a collection which contains a single address book resource
public class UserResource extends AbstractResource implements CardDavPrincipal, CollectionResource{

    private final User user;
    private ArrayList<Resource> children;

    public UserResource(User user, RootResource parent) {
        super(parent, parent.contactManager);
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getUserName();
    }

    @Override
    public HrefList getAddressBookHomeSet() {
        return HrefList.asList(getAddress()); // the address books folder
    }

    @Override
    public String getAddress() {
        return getHref() + "abs/";
    }

    @Override
    public PrincipleId getIdenitifer() {
        return new HrefPrincipleId(getPrincipalURL());
    }

    @Override
    public String getPrincipalURL() {
        return getHref();
    }

    @Override
    public Resource child(String childName) throws NotAuthorizedException, BadRequestException {
        return ChildUtils.child(childName, getChildren());  //get UserAddressBookResource
    }

    @Override
    public List<? extends Resource> getChildren() throws NotAuthorizedException, BadRequestException {
        if( children == null ) {
            children = new ArrayList<>();
            children.add(new UserAddressBookResource(this));
        }
        return children;
    }
}