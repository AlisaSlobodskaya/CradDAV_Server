package com.application.carddav.resource;

import com.application.carddav.entity.User;
import com.application.carddav.utils.ChildUtils;
import io.milton.resource.CollectionResource;
import io.milton.resource.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

//a RootResource which contains users
@Slf4j
public class RootResource extends AbstractResource implements CollectionResource {

    private ArrayList<Resource> children;

    public RootResource(ContactManager contactManager) {
        super(contactManager);
    }

    @Override
    public List<? extends Resource> getChildren() {  //get a list of all users
        if (children == null) {
            children = new ArrayList<Resource>();
            for (User user : contactManager.getUsersDAO().getUsers()) {
                children.add(new UserResource(user, this));
            }
        }
        return children;
    }

    @Override
    public Resource child(String childName) {  //get user by name
        return ChildUtils.child(childName, getChildren());
    }

    @Override
    public String getName() {
        return "";
    }
}