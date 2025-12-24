package com.example.api.services;

import com.example.api.models.Child;

import java.util.UUID;

public interface ChildServices {
    Child addChild(UUID parentId, Child child);
    Child getChild(UUID childId);
    boolean deleteChild(UUID childId);
    boolean deleteChildren(UUID parentId);
}
