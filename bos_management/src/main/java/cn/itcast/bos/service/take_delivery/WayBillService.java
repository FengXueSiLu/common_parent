package cn.itcast.bos.service.take_delivery;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillService {
	// 保存运单
	void save(WayBill model);
	// 无条件 分页查询
	Page<WayBill> findPageData(WayBill model, Pageable pageable);
	// 根据运单号查询
	WayBill findByWayBillNum(String wayBillNum);
	//同步数据库和索引库
	void syncIndex();
	
	List<WayBill> findWayBills(WayBill model);
	
}
