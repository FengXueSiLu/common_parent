package cn.itcast.bos.service.take_delivery.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.take_delivery.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;
	@Autowired
	private WayBillRepository wayBillRepository;
	@Override
	public void save(WayBill model) {
		//判断运单号是否存在
		WayBill wayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());
		if (wayBill == null || wayBill.getId() == null) {
			//运单不存在
			//保存运单
			wayBill.setSignStatus(1);
			wayBillRepository.save( model);
			//保存索引
			wayBillIndexRepository.save(model);
		} else {
			//运单存在
			if(wayBill.getSignStatus() == 1) {
				Integer id = wayBill.getId();
				BeanUtils.copyProperties(wayBill, model);
				wayBill.setId(id);
				wayBill.setSignStatus(1);
				//保存索引
				wayBillIndexRepository.save(wayBill);
			}else{
				// 运单状态 已经运输中，不能修改
				throw new RuntimeException("运单已经发出，无法修改保存！！");
				
			}
			
		}
		
	}
	@Override
	public Page<WayBill> findPageData(WayBill wayBill,Pageable pageable) {
		// 判断WayBill 中条件是否存在
		if (StringUtils.isBlank(wayBill.getWayBillNum())
				&&StringUtils.isBlank(wayBill.getSendAddress())
				&&StringUtils.isBlank(wayBill.getRecAddress())
				&&StringUtils.isBlank(wayBill.getSendProNum())
				&&(wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
			// 无条件查询 、查询数据库
			return wayBillRepository.findAll(pageable);
		} else {
			// 查询条件
			// must 条件必须成立 and
			// must not 条件必须不成立 not
			// should 条件可以成立 or
			BoolQueryBuilder query = new BoolQueryBuilder();// 布尔查询 ，多条件组合查询
			// 向组合查询对象添加条件
			if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
				// 运单号查询
				QueryBuilder termQuery = new TermQueryBuilder("wayBillNum",
						wayBill.getWayBillNum());
				query.must(termQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
				// 发货地 模糊查询
				QueryBuilder wildcardQuery = new WildcardQueryBuilder("sendAddress",
						"*"+wayBill.getSendAddress()+"*");
				query.must(wildcardQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
				//收货地 模糊查询
				QueryBuilder wildcardQuery = new WildcardQueryBuilder("recAddress","*"+wayBill.getRecAddress()+"*");
				query.must(wildcardQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				// 速运类型 等值查询
				QueryBuilder termQuery = new TermQueryBuilder("sendProNum",
						wayBill.getSendProNum());
				query.must(termQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				// 速运类型 等值查询
				QueryBuilder termQuery = new TermQueryBuilder("sendProNum",
						wayBill.getSendProNum());
				query.must(termQuery);
			}
			if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
				// 签收状态查询
				QueryBuilder termQuery = new TermQueryBuilder("signStatus",
						wayBill.getSignStatus());
				query.must(termQuery);
			}
			SearchQuery searchQuery = new NativeSearchQuery(query);
			searchQuery.setPageable(pageable);// 分页效果
			//有条件查询 、查询索引库
			return wayBillIndexRepository.search(searchQuery);
		}
		
	}
	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}
	@Override
	public void syncIndex(){
		//查询数据库
		List<WayBill> wayBill = wayBillRepository.findAll();
		//同步索引库
		wayBillIndexRepository.save(wayBill);
	}
	@Override
	public List<WayBill> findWayBills(WayBill model) {
		// 判断WayBill 中条件是否存在
		if (StringUtils.isBlank(model.getWayBillNum())
				&&StringUtils.isBlank(model.getSendAddress())
				&&StringUtils.isBlank(model.getRecAddress())
				&&StringUtils.isBlank(model.getSendProNum())
				&&model.getSignStatus()==null || model.getSignStatus()==0) {
			// 无条件查询 、查询数据库
			return wayBillRepository.findAll();
		} else {
			// 查询条件
			// must 条件必须成立 and
			// must not 条件必须不成立 not
			// should 条件可以成立 or
			BoolQueryBuilder query = new BoolQueryBuilder(); //布尔查询，多条件查询
			// 向组合查询对象添加条件
			if (StringUtils.isNoneBlank(model.getWayBillNum())) {
				//运单号
				QueryBuilder tempQuery = new TermQueryBuilder("wayBillNum", model.getWayBillNum());
				query.must(tempQuery);
			}
			if (StringUtils.isNoneBlank(model.getSendAddress())) {
				// 发货地 模糊查询
				// 情况一： 输入"北" 是查询词条一部分， 使用模糊匹配词条查询
				QueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("sendAddress", "*"+model.getSendAddress()+"*");
				// 情况二： 输入"北京市海淀区" 是多个词条组合，进行分词后 每个词条匹配查询
				QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(
						model.getSendAddress()).field("sendAddress").defaultOperator(Operator.AND);
				// 两种情况取or关系
				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				boolQueryBuilder.should(wildcardQueryBuilder);
				boolQueryBuilder.should(queryStringQueryBuilder);
				query.must(boolQueryBuilder);
			}
			if (StringUtils.isNoneBlank(model.getRecAddress())) {
				//收货地
				
				
			}
			if (StringUtils.isNoneBlank(model.getSendProNum())) {
				//快递产品类型
				
				
			}
			if (model.getSignStatus()!=null || model.getSignStatus()!=0) {
				//运单状态
				
				
			}
			
			
		}
		return null;
		
	}
	
	
}
