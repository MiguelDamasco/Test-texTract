package com.example.demo.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.UserModel;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, Long> {
    
}
