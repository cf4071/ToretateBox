package katachi.example.toretatebox.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import katachi.example.toretatebox.domain.dto.AdminOrderDetailDto;
import katachi.example.toretatebox.domain.dto.AdminOrderRow;

public interface AdminOrderService {

    Page<AdminOrderRow> findAdminOrderRows(Pageable pageable);

    AdminOrderDetailDto getOrderDetail(Integer orderId);
}