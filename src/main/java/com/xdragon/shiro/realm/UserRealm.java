package com.xdragon.shiro.realm;

import java.util.Map;

import javax.inject.Inject;  

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.xdragon.jpa.entity.SysUser;
import com.xdragon.jpa.persistence.DynamicSpecifications;
import com.xdragon.jpa.service.SysUserService;
import com.xdragon.shiro.token.UsernamePasswordCaptchaToken;

@Service  
@Transactional  
public class UserRealm extends AuthorizingRealm{

	/**
	 * 权限认证 
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {

		return null;
	}

	/** 
     * 登录认证; 
     */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		//UsernamePasswordToken对象用来存放提交的登录信息  
		UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authenticationToken; 
		//查出是否有此用户  
		Map<String, Object> searchParams = Maps.newHashMap();
		searchParams.put("userName", token.getUsername());
		Specification<SysUser> _t = DynamicSpecifications.buildSpecification(searchParams, SysUser.class);
		SysUser user = sysUserService.findOne(_t); 
		if(user!=null){  
            //若存在，将此用户存放到登录认证info中  
            return new SimpleAuthenticationInfo(user.getUserName(), user.getUserPwd(), getName());  
        }  
        return null;  
	}
	
	@Inject  
    private SysUserService sysUserService;  

}
