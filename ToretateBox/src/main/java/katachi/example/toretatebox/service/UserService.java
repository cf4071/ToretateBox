package katachi.example.toretatebox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.AddressRepository;
import katachi.example.toretatebox.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User register(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                "このメールアドレスは既に使用されています。"
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setAdmin(false);     
        user.setDeleted(false);   

        return userRepository.save(user);
    }

    @Transactional
    public void registerWithAddress(User user, Address address) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                "このメールアドレスは既に使用されています。"
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setAdmin(false);
        user.setDeleted(false);

        User savedUser = userRepository.save(user);

        address.setUserId(savedUser.getId());

        addressRepository.save(address);
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
