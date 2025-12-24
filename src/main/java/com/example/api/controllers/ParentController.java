package com.example.api.controllers;

import com.example.api.Dto.ParentDto;
import com.example.api.implementations.ParentServicesImpl;
import com.example.api.models.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.example.api.services.JwtServices;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class ParentController {

    @Autowired
    private ParentServicesImpl parentServices;

    private final AuthenticationManager authenticationManager;
    private final JwtServices jwtServices;

    public ParentController(AuthenticationManager authenticationManager,
                            JwtServices jwtServices) {
        this.authenticationManager = authenticationManager;
        this.jwtServices = jwtServices;
    }

    //Sign Up Parent
    @PostMapping("auth/addParent")
    public ResponseEntity<?> createParent(@RequestBody Parent parent) {
       Parent p = parentServices.createParent(parent);

       if(p.equals(null)){
           return ResponseEntity
               .badRequest()
               .contentType(MediaType.APPLICATION_JSON)
               .body(Map.of(
                  "message","Xảy ra lỗi!"
               ));
       }
       return ResponseEntity.ok(p);
    }

    //Get Parent and Child by ID
    @GetMapping(path = "/parents/{id}")
    public ResponseEntity<?> getParent(@PathVariable UUID id) {
        Parent p = parentServices.getParent(id);
        if(p.equals(null)){
            return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                    "message","Xảy ra lỗi!"
                ));
        }
        return ResponseEntity.ok(p);
    }

    //Get Parent and Child by Phone Number
    @GetMapping(path = "/parents/phone/{phoneNumber}")
    public ResponseEntity<?> getParent(@PathVariable String phoneNumber) {
        UserDetails p = parentServices.loadUserByUsername(phoneNumber);

        if(p.equals(null)){
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "message","Xảy ra lỗi!"
                    ));
        }
        return ResponseEntity.ok(p);
    }

    //Sign In Parent
//    @PostMapping(path = "signin/{id}")
//    public ResponseEntity<?> signIn(
//            @PathVariable UUID id,
//            @RequestBody ParentDto parentDto
//        ){
//        boolean isValid = parentServices.signIn(parentDto, id);
//        if(isValid){
//            return ResponseEntity.ok(Map.of(
//                "message", "Đăng nhập thành công!"
//            ));
//        }
//        else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
//                "message", "Thông tin đăng nhập không hợp lệ!"
//            ));
//        }
//    }

    //Delete Parent
    @DeleteMapping(path = "/parents/del/{id}")
    public ResponseEntity<?> deleteParent(@PathVariable UUID id){
        Parent parent = parentServices.deleteParent(id);

        if(parent.equals(null)){
            return ResponseEntity.ok(Map.of(
                    "message", "Không tìm thấy tài khoản!"
            ));
        }

        if (!parent.getChildren().isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "message","Vui lòng xoá hết profile của trẻ trước khi xoá tài khoản!"
                    ));
        }

        return ResponseEntity.ok(Map.of(
            "message", "Tài khoản được xoá thành công!"
        ));
    }

    @PostMapping("auth/generateToken")
    public String authenticateAndGetToken(@RequestBody ParentDto parentDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(parentDto.getPhoneNumber(), parentDto.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtServices.generateToken(parentDto.getPhoneNumber());
        } else {
            throw new UsernameNotFoundException("Thông tin đăng nhập không hợp lệ!");
        }
    }

    @GetMapping("auth/authority/{id}")
    public ResponseEntity<?> getAuth(@PathVariable UUID id){
        Parent p = parentServices.getParent(id);

        if(p.equals(null)){
            return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "message","Xảy ra lỗi!"
                ));
        }

        return ResponseEntity.ok(p.getRoles());
    }
}