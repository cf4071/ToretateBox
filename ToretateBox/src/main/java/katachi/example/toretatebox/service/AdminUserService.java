package katachi.example.toretatebox.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }
}
