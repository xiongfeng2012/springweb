package com.xdragon.jpa.service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.xdragon.jpa.entity.SysUser;
import com.xdragon.jpa.repository.BaseRepository;
import com.xdragon.jpa.repository.SysUserRepository;


@Transactional
@Service
public class SysUserService extends BaseService<SysUser, String> {
	
	

	@Override
	protected BaseRepository<SysUser, String> getRepository() {
		return this.sysUserRepository;
	}
	
	@Resource
	private SysUserRepository sysUserRepository;

}
