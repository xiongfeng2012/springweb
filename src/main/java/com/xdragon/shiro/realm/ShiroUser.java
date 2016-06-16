package com.xdragon.shiro.realm;

import java.io.Serializable;

/**
 * 
 * @author xdragon
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 *
 */
public class ShiroUser  implements Serializable {

	private static final long serialVersionUID = -4933907914109120581L;
	
	private String loginName;
	
	private String name;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ShiroUser(String loginName, String name) {
		super();
		this.loginName = loginName;
		this.name = name;
	}

}
