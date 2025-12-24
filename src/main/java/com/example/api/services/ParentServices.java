package com.example.api.services;

import com.example.api.Dto.ParentDto;
import com.example.api.models.Parent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface ParentServices extends UserDetailsService {
    Parent createParent(Parent parent);
    Parent getParent(UUID id);
    boolean signIn(ParentDto parentDto, UUID id);
    Parent deleteParent(UUID parentId);
    Parent getParentByPhone(String phoneNumber);
}
