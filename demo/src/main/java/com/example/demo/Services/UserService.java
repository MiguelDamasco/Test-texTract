package com.example.demo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.example.demo.Models.UserModel;
import com.example.demo.Repositorys.IUserRepository;

@Service
public class UserService {
    
    @Autowired
    IUserRepository iUserRepository;

    public ArrayList<UserModel> getUsers() {
        return (ArrayList<UserModel>) iUserRepository.findAll();
    }

    public UserModel saveUser(UserModel user) {
        return this.iUserRepository.save(user);
    }

    public Optional<UserModel> findUserById(Long pId) {
        return this.iUserRepository.findById(pId);
    }

    public Optional<UserModel> updateUser(UserModel user, Long id) {
        Optional<UserModel> userUpdated = this.iUserRepository.findById(id);
        if(userUpdated.isPresent()) {
            userUpdated.get().setFirstName(user.getFirstName());
            userUpdated.get().setLastName(user.getLastName());
            userUpdated.get().setEmail(user.getEmail());
            this.iUserRepository.save(userUpdated.get());
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public void deleteUser(Long id) {
        this.iUserRepository.deleteById(id);
    }
}
