package com.axelor.account.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.axelor.contact.db.Contact;
import com.axelor.invoice.db.Invoice;
import com.axelor.sale.db.SaleOrderLine;

public interface SaleOrderAccountService {
	public Invoice generateInvoice(Contact contact,Long saleOrderId,BigDecimal inTaxTotal,LocalDateTime invoiceDateT,List<SaleOrderLine> saleOrderLineList);
}
