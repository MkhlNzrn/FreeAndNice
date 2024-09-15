package com.example.freeandnice.services;

import com.example.freeandnice.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User save(User user);

    User create(User user);

    User getByUsername(String username);

    UserDetailsService userDetailsService();

    User getCurrentUser();

    boolean userExists(String username);

    void deleteByEmail(String username);

    User getById(Long id);

    List<User> getAllUsers();

    User update(User user);

    void deleteById(Long id);
}
