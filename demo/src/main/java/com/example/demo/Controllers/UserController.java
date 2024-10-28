package com.example.demo.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.UserModel;
import com.example.demo.Services.UserService;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping()
    public ArrayList<UserModel> getUsers() {
        return this.userService.getUsers();
    }

    @GetMapping("/byId/{id}")
    public UserModel findUserById(@PathVariable Long id) {
        Optional<UserModel> user = this.userService.findUserById(id);
        if(user.isPresent()) return user.get();
        else return null;
    }

    @PostMapping
    public UserModel saveUser(@RequestBody UserModel user) {
        return this.userService.saveUser(user);
    }
    
    @PutMapping("/update/{id}")
    public String updateById(@RequestBody UserModel user, @PathVariable Long id) {
        System.out.println("Id obtenida: " + id);
        Optional<UserModel> myUser = this.userService.updateUser(user, id);
        if(myUser.isPresent()) return "Usuario actualizado con exito!";
        else return "Error!";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return "Eliminado con exito!";
    }
    
}
