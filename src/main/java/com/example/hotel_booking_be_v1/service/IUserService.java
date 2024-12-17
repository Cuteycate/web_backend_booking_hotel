package com.example.hotel_booking_be_v1.service;

import com.example.hotel_booking_be_v1.model.User;

import java.util.List;

public interface IUserService {
    User registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
    User getUserById (Long id);
    User getUserByEmail(String email);
}
