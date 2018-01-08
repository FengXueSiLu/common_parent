package cn.itcast.bos.web.action.transit;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.TransitInfoService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TransitInfoAction extends BaseAction<TransitInfo>{
	//属性驱动
	private String waybillIds;

	public void setWaybillIds(String waybillIds) {
		this.waybillIds = waybillIds;
	}
	@Autowired
	private TransitInfoService transitInfoService;
	
	@Action(value="transitInfo_create",results={@Result(name="success",type="json")})
	public String create(){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			transitInfoService.createTransits(waybillIds);
			//成功
			result.put("success", true);
			result.put("msg","开启中转配送成功！");
		} catch (Exception e) {
			System.out.println("qqqqqqq");
			e.printStackTrace();
			result.put("success", false);
			result.put("msg","开启中转配送失败，异常："+e.getMessage());
		}
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
	@Action(value="transitInfo_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		// 分页查询
		Pageable pageable = new PageRequest(page-1, rows);
		// 调用业务层 查询分页数据
		Page<TransitInfo> pageData = transitInfoService.findPageData( pageable);
		//压入值栈
		ActionContext.getContext().getValueStack().push(pageData);
		return SUCCESS;
		
	}
	
	
}
