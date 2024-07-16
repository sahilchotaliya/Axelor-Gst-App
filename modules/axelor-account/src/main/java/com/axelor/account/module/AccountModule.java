package com.axelor.account.module;

import com.axelor.account.db.repo.AccountManagementRepository;
import com.axelor.account.db.repo.AccountRepository;
import com.axelor.account.service.InvoiceAccountService;
import com.axelor.account.service.InvoiceAccountServiceImpl;
import com.axelor.account.service.SaleOrderAccountService;
import com.axelor.account.service.SaleOrderAccountServiceImpl;
import com.axelor.app.AxelorModule;

public class AccountModule extends AxelorModule{
	@Override
	protected void configure() {
		bind(SaleOrderAccountService.class).to(SaleOrderAccountServiceImpl.class);
		bind(InvoiceAccountService.class).to(InvoiceAccountServiceImpl.class);
		bind(AccountRepository.class).to(AccountManagementRepository.class);
	}
}
