package cn.itcast.bos.web.action.transit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import cn.itcast.bos.domain.transit.DeliveryInfo;
import cn.itcast.bos.service.transit.DeliveryInfoService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class DeliveryInfoAction extends BaseAction<DeliveryInfo>{
	
	private String transitInfoId;
	
	public void setTransitInfoId(String transitInfoId) {
		this.transitInfoId = transitInfoId;
	}
	
	@Autowired
	private DeliveryInfoService deliveryInfoService;
	
	@Action(value="delivery_save",results={@Result(name="success",type="redirect",location="pages/transit/transitinfo.html")})
	public String save(){
		deliveryInfoService.save(model,transitInfoId);
		return SUCCESS;
	}
	
	
	
	
	
	
	
}
