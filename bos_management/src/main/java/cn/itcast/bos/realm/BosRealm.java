package cn.itcast.bos.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
//自定义Realm ，实现安全数据 连接
public class BosRealm extends AuthorizingRealm{
	@Autowired
	private UserService userService;
	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		System.out.println("shiro 认证管理... ");
		// 转换token
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		// 根据用户名 查询 用户信息
		User user = userService.findByUsername(usernamePasswordToken.getUsername());
		if (user == null) {
			// 用户名不存在
			// 参数一： 期望登录后，保存在Subject中信息
			// 参数二： 如果返回为null 说明用户不存在，报用户名
			// 参数三 ：realm名称
			return null;
		} else {
			// 用户名存在
			// 当返回用户密码时，securityManager安全管理器，自动比较返回密码和用户输入密码是否一致
			// 如果密码一致 登录成功， 如果密码不一致 报密码错误异常
			return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
			
		}
		
		
		
		
	}

}
