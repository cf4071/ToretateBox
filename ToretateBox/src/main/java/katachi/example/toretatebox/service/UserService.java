package katachi.example.toretatebox.service;

import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.User;

public interface UserService {

    User register(User user);

    void registerWithAddress(User user, Address address);

    User findByEmail(String email);

    boolean checkPassword(String rawPassword, String encodedPassword);

    boolean existsByEmail(String email);
}