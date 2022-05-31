package com.application.utils;

import io.milton.resource.Resource;

import java.util.Collection;

public class ChildUtils {
    public static Resource child(String childName, Collection<? extends Resource> children) {
        for (Resource r : children) {
            if (r.getName().equals(childName)) {
                return r;
            }
        }
        return null;
    }
}