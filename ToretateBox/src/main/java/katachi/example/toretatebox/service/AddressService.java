package katachi.example.toretatebox.service;

import katachi.example.toretatebox.domain.model.Address;

public interface AddressService {

    Address save(Address address);

    Address findById(Integer id);

    Address findLatestByUserId(Integer userId);
}