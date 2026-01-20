package katachi.example.toretatebox.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    // user_id で住所を取る
    Address findByUserId(Integer userId);

	Optional<Address> findTopByUserIdOrderByIdDesc(Integer userId);
}
