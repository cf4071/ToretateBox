package katachi.example.toretatebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import katachi.example.toretatebox.domain.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findByUserId(Integer userId);

    Address findTopByUserIdOrderByIdDesc(Integer userId);
}
