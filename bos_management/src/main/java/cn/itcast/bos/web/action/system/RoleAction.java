package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("protope")
public class RoleAction extends BaseAction<Role>{
	@Autowired
	private RoleService roleService;
	@Action(value="role_list",results={@Result(name="success",type="json")})
	public String list(){
		List<Role> list =  roleService.list();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
}
