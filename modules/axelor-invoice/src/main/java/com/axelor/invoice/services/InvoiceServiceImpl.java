package com.axelor.invoice.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.axelor.contact.db.Contact;
import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.invoice.db.repo.InvoiceLineRepository;
import com.axelor.invoice.db.repo.InvoiceRepository;
import com.axelor.sale.db.Product;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class InvoiceServiceImpl implements InvoiceService {
	@Inject
	public InvoiceLineServiceImpl invoiceLineService;
	@Inject
	public InvoiceLineRepository invoiceLineRepository;
	@Inject
	public InvoiceRepository invoiceRepository;

	@Transactional(rollbackOn = Exception.class)
	@Override
	public void cancelInvoiceList(List<Invoice> invoices) {
		for (Invoice invoice : invoices) {
			cancelInvoice(invoice);
		}

	}

	public void cancelInvoice(Invoice invoice) {
		if (invoice.getStatusSelect() != 2) {
			invoice.setStatusSelect(3);
			invoiceRepository.save(invoice);
		}
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public Invoice generateInvoice(List<Invoice> invoices) {
		BigDecimal exTaxTotal = BigDecimal.ZERO;
		BigDecimal inTaxTotal = BigDecimal.ZERO;
		Invoice newInvoice = new Invoice();
		boolean isCustomerDifferent = invoices.stream()
				.allMatch(it -> it.getCustomer() == invoices.get(0).getCustomer());
		if (!isCustomerDifferent) {
			return null;
		}
		Contact customer = invoices.get(0).getCustomer();
		List<InvoiceLine> allInvoiceLineList = new ArrayList<InvoiceLine>();
		for (Invoice invoice : invoices) {
			allInvoiceLineList.addAll(invoice.getInvoiceLineList());
			exTaxTotal = exTaxTotal.add(invoice.getExTaxTotal());
			inTaxTotal = inTaxTotal.add(invoice.getInTaxTotal());
			invoice.setGeneratedInvoice(newInvoice);
			invoice.setArchived(true);
		}
		List<InvoiceLine> mergedAndGroupedInvoiceLines = invoiceLineService.generateInvoiceLineList(allInvoiceLineList);
		newInvoice.setCustomer(customer);
		newInvoice.setInvoiceDateT(LocalDateTime.now());
		newInvoice.setStatusSelect(0);
		newInvoice.setExTaxTotal(exTaxTotal);
		newInvoice.setInTaxTotal(inTaxTotal);
		newInvoice.setInvoiceLineList(mergedAndGroupedInvoiceLines);

		newInvoice = invoiceRepository.save(newInvoice);

		for (InvoiceLine invoiceLine : mergedAndGroupedInvoiceLines) {
			invoiceLine.setInvoice(newInvoice);
		}
		return newInvoice;
	}

}
