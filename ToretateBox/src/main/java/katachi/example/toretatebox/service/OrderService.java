package katachi.example.toretatebox.service;

import java.util.List;

import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.Cart;

public interface OrderService {

	Integer createOrder(List<Cart> cart, Address address, Integer userId);
}