package com.axelor.account.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.account.db.Account;
import com.axelor.account.db.Move;
import com.axelor.invoice.db.InvoiceLine;

public interface InvoiceAccountService {
	public Move generateMove(Long invoiceId,List<InvoiceLine> invoiceLineList,Account account,BigDecimal inTaxTotal);
}
