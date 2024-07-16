package com.axelor.account.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.axelor.contact.db.Contact;
import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.invoice.db.repo.InvoiceLineRepository;
import com.axelor.invoice.db.repo.InvoiceRepository;
import com.axelor.sale.db.SaleOrderLine;
import com.axelor.sale.db.repo.SaleOrderRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class SaleOrderAccountServiceImpl implements SaleOrderAccountService {
	private InvoiceRepository invoiceRepository;
	private InvoiceLineRepository invoiceLineRepository;
	private SaleOrderRepository saleOrderRepository;

	@Inject
	public SaleOrderAccountServiceImpl(InvoiceRepository invoiceRepository, InvoiceLineRepository invoiceLineRepository,
			SaleOrderRepository saleOrderRepository) {
		this.invoiceRepository = invoiceRepository;
		this.invoiceLineRepository = invoiceLineRepository;
		this.saleOrderRepository = saleOrderRepository;
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public Invoice generateInvoice(Contact customer, Long saleOrderId, BigDecimal inTaxTotal,
			LocalDateTime invoiceDateT, List<SaleOrderLine> saleOrderLineList) {
		Invoice invoice = new Invoice();
		invoice.setStatusSelect(0);
		invoice.setSaleOrder(saleOrderRepository.find(saleOrderId));
		invoice.setCustomer(customer);
		invoice.setInvoiceDateT(invoiceDateT);
		invoiceRepository.save(invoice);

		for (SaleOrderLine line : saleOrderLineList) {
			InvoiceLine invoiceLine = new InvoiceLine();
			invoiceLine.setProduct(line.getProduct());
			invoiceLine.setDiscription(line.getDescription());
			invoiceLine.setUnitPriceUntaxed(line.getUnitPriceUntaxed());
			invoiceLine.setQuantity(line.getQuantity());
			invoiceLine.setExTaxTotal(line.getExTaxTotal());
			invoiceLine.setTaxRate(line.getTaxRate());
			invoiceLine.setInTaxTotal(line.getExTaxTotal().multiply((line.getTaxRate().add(BigDecimal.ONE))));
			invoiceLine.setInvoice(invoice);
			invoiceLineRepository.save(invoiceLine);
		}
		invoice.setExTaxTotal(
				saleOrderLineList.stream().map(it -> it.getExTaxTotal()).reduce(BigDecimal.ZERO, BigDecimal::add));
		invoice.setInTaxTotal(
				saleOrderLineList.stream().map(it -> it.getExTaxTotal().multiply((it.getTaxRate().add(BigDecimal.ONE))))
						.reduce(BigDecimal.ZERO, BigDecimal::add));
		invoiceRepository.save(invoice);
		return invoice;
	}

}
