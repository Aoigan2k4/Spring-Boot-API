package com.example.api.controllers;

import com.example.api.implementations.ChildServicesImpl;
import com.example.api.models.Child;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("parents/children")
public class ChildController {
    private final ChildServicesImpl childServices;

    public ChildController(ChildServicesImpl childServices) {
       this.childServices = childServices;
    }

    //Add Child to Parent
    @PostMapping("addChild/{parentId}")
    public ResponseEntity<?> addChild(
            @PathVariable UUID parentId,
            @RequestBody Child child
    ){
        Child c = childServices.addChild(parentId, child);
        if (c.equals(null)){
            return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "message", "Profile trẻ đã được đăng ký thành công!"
                ));
        }
        return ResponseEntity.ok(child);
    }

    //Get Child
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getChild(@PathVariable UUID id){

        Child child = childServices.getChild(id);
        if(child.equals(null)){
            return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "message", "Profile trẻ không tồn tại."
                ));
        }
        return ResponseEntity.ok(child);
    }

    //Delete a specific Child
    @DeleteMapping(path = "/del/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteChild(@PathVariable UUID id){
       boolean isValid = childServices.deleteChild(id);
       if(!isValid){
           return ResponseEntity
               .badRequest()
               .contentType(MediaType.APPLICATION_JSON)
               .body(Map.of(
                       "message", "Có lỗi xảy ra!"
               ));
       }

        return ResponseEntity.ok(Map.of(
                "message:", "Xoá thành công profile của trẻ!"
        ));
    }

    //Delete all children from a parent
    @DeleteMapping(path = "/delAll/{parentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteChildren(@PathVariable UUID parentId){
      boolean isValid = childServices.deleteChildren(parentId);

        if(!isValid){
            return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "message", "Có lỗi xảy ra!"
                ));
        }
        return ResponseEntity.ok(Map.of(
                "message:", "Xoá thành công profile của trẻ!"
        ));
    }
}