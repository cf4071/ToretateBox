package katachi.example.toretatebox.service;

import java.util.Optional;

import katachi.example.toretatebox.domain.model.User;

public interface AdminUserService {

    Optional<User> findById(Integer id);
}