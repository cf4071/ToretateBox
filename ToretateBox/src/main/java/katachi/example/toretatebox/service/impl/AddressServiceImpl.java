package katachi.example.toretatebox.service.impl;

import org.springframework.stereotype.Service;

import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.repository.AddressRepository;
import katachi.example.toretatebox.service.AddressService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address findById(Integer id) {
        return addressRepository.findById(id).orElse(null);
    }

    @Override
    public Address findLatestByUserId(Integer userId) {
        return addressRepository.findTopByUserIdOrderByIdDesc(userId);
    }
}