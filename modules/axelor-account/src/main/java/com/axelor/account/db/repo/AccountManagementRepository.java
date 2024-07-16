package com.axelor.account.db.repo;

import com.axelor.account.db.Account;

public class AccountManagementRepository extends AccountRepository{
	
	@Override
	public Account save(Account entity) {
		if(entity!=null) {
		int code=entity.getCode();
		String name=entity.getName();
		String fullName=code+"-"+name;
		entity.setFullName(fullName);
	}
	return super.save(entity);
	}
}
