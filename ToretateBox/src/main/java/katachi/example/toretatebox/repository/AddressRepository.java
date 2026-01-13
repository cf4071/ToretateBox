package katachi.example.toretatebox.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Optional<Address> findTopByUserIdOrderByIdDesc(Integer userId);
}
