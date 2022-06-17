package com.finalproject.secondhand.service;

import com.finalproject.secondhand.entity.Users;
import com.finalproject.secondhand.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Users addUser(Users body) {
        Users users = new Users();
        users.setUsername(body.getUsername());
        users.setEmail(body.getEmail());
        users.setPassword(body.getPassword());
        return userRepository.save(users);
    }

    @Override
    public List<Users> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Users> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Users updateUser(Users body, Integer id) {
        Users users = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found!"));
        users.setEmail(body.getEmail());
        users.setPassword(body.getPassword());
        users.setAddress(body.getAddress());
        users.setPhoneNumber(body.getPhoneNumber());
        return userRepository.save(users);
    }

    @Override
    public String deleteUser(Integer id) {
        Users users = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found!"));
        String result = users.getUsername();
        userRepository.deleteById(id);
        return "Username " + result + " has been deleted";
    }

}