package com.example.api.implementations;

import com.example.api.Dto.ParentDto;
import com.example.api.models.Parent;
import com.example.api.repositories.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.example.api.services.ParentServices;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParentServicesImpl implements ParentServices {
    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Parent createParent(@RequestBody Parent parent) {
        UUID id = UUID.randomUUID();
        parent.setId(id);
        parent.setPassword(passwordEncoder.encode(parent.getPassword()));
        parent.setRoles(List.of("ROLE_PARENT"));
        return parentRepository.save(parent);
    }

    //Get Parent and Child by ID
    public Parent getParent(@PathVariable UUID id) {
        return parentRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản!"
        ));
    }

    //Get Parent and Child by Phone Number
    public Parent getParentByPhone(@PathVariable String phoneNumber) {
        return parentRepository.findByphoneNumber(phoneNumber);
    }

    //Sign In Parent
    public boolean signIn(ParentDto parentDto, UUID id) {
        Parent parent = parentRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Thông tin đăng nhập không hợp lệ!")
        );

        boolean isValid = passwordEncoder.matches(
            parentDto.getPassword(), parent.getPassword()
        );

        if(isValid){
            return true;
        }
        return false;
    }

    //Delete Parent
    public Parent deleteParent(@PathVariable UUID id) {
        Parent parent = parentRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Tài khoản không tồn tại!")
        );

        if (!parent.getChildren().isEmpty()) {
            return null;
        }

       parentRepository.delete(parent);
       return parent;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Optional<Parent> parent = Optional.ofNullable(getParentByPhone(phoneNumber));

        if (parent.equals(null)) {
            throw new UsernameNotFoundException("User not found with phoneNumber: " + phoneNumber);
        }

        Parent user = parent.get();

        return new User(user.getPhoneNumber(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_PARENT")));
    }
}