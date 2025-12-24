package com.example.api.implementations;

import com.example.api.models.Child;
import com.example.api.models.Parent;
import com.example.api.repositories.ChildRepository;
import com.example.api.repositories.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.api.services.ChildServices;
import java.util.List;
import java.util.UUID;

@Service
public class ChildServicesImpl implements ChildServices {
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private ParentRepository parentRepository;

    //Add Child
    public Child addChild(UUID parentId, Child child){
        Parent parent = parentRepository.findById(parentId).orElseThrow(
                () -> new RuntimeException("Tài khoản không tồn tại!"));
        UUID id = UUID.randomUUID();
        child.setId(id);
        child.setParent(parent);
        return childRepository.save(child);
    }

    //Get Child
    public Child getChild(@PathVariable UUID id){
        return childRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Tài khoản không tồn tại!"));
    }

    //Delete a specific Child
    public boolean deleteChild(@PathVariable UUID id){
        Child child = childRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Tài khoản không tồn tại!")
        );
        if (child.equals(null)){
           return false;
        }

        childRepository.delete(child);
        return true;
    }

    //Delete all children from a parent
    public boolean deleteChildren(@PathVariable UUID parentId) {
        Parent parent = parentRepository.findById(parentId).orElseThrow(
                () -> new RuntimeException("Tài khoản không tồn tại!")
        );

        if (parent.equals(null)) {
            return false;
        }

        List<Child> children = parent.getChildren();
        for (Child i : children) {
            deleteChild(i.getId());
        }

        return true;
    }
}