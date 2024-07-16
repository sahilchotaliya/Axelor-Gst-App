package com.axelor.invoice.module;

import com.axelor.app.AxelorModule;
import com.axelor.invoice.services.InvoiceLineService;
import com.axelor.invoice.services.InvoiceLineServiceImpl;
import com.axelor.invoice.services.InvoiceService;
import com.axelor.invoice.services.InvoiceServiceImpl;

public class InvoiceModule extends AxelorModule{
	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bind(InvoiceService.class).to(InvoiceServiceImpl.class);
		bind(InvoiceLineService.class).to(InvoiceLineServiceImpl.class);
		super.configure();
	}
}
