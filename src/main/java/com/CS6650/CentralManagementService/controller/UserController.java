package com.CS6650.CentralManagementService.controller;

import com.CS6650.CentralManagementService.model.User;
import com.CS6650.CentralManagementService.respository.UserRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/validateUser/{userName}")
  public User validateUser(@PathVariable String userName) {
    // TO DO: find user with userName and return, else return null
    return new User();
  }

  @PostMapping("/registerUser")
  User registerUser(@RequestBody User newUser) {
    return userRepository.save(newUser);
  }

}
