package com.example.api.repositories;

import com.example.api.models.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChildRepository extends JpaRepository<Child, UUID>  {}
