package katachi.example.toretatebox.service;

import org.springframework.stereotype.Service;

import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.repository.AddressRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    // 住所を保存
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    // idで住所を取得（register画面表示で使う）
    public Address findById(Integer id) {
        return addressRepository.findById(id).orElse(null);
    }
    
    public Address findLatestByUserId(Integer userId) {
        return addressRepository.findTopByUserIdOrderByIdDesc(userId).orElse(null);
    }
}
