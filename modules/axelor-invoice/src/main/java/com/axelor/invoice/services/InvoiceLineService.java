package com.axelor.invoice.services;

import java.util.List;

import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;

public interface InvoiceLineService {
		public List<InvoiceLine> generateInvoiceLineList(List<InvoiceLine> invoiceLineList);
}
