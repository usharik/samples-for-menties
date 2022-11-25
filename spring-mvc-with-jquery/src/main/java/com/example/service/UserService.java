package com.example.service;

import com.example.persist.User;
import com.example.persist.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        userRepository.save(new User(null, "This User 1", "tttabcd", LocalDate.now().minusYears(15)));
        userRepository.save(new User(null, "New User 2", "afgabcde", LocalDate.now().minusYears(17)));
        userRepository.save(new User(null, "Some User 3", "agbgcd", LocalDate.now().minusYears(32)));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
