package katachi.example.toretatebox.service;

import java.util.List;

import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.CartItem;

public interface OrderService {

    Integer createOrder(List<CartItem> cart, Address address, Integer userId);
}