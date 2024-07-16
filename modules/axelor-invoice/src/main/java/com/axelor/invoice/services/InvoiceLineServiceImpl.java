package com.axelor.invoice.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.invoice.db.repo.InvoiceLineRepository;
import com.axelor.invoice.db.repo.InvoiceRepository;
import com.axelor.sale.db.Product;
import com.google.inject.Inject;

public class InvoiceLineServiceImpl implements InvoiceLineService {
	@Inject
	InvoiceService invoiceService;
	@Inject
	InvoiceRepository invoiceRepository;
	@Inject
	InvoiceLineRepository invoiceLineRepository;

	@Override
	public List<InvoiceLine> generateInvoiceLineList(List<InvoiceLine> invoiceLineList) {

		Map<Product, List<InvoiceLine>> productInvoiceLineSet = invoiceLineList.stream()
				.collect(Collectors.groupingBy(InvoiceLine::getProduct));
		Set<Product> productSet = productInvoiceLineSet.keySet();
		Iterator<Product> productIterator = productSet.iterator();
		List<InvoiceLine> mergedLines = new ArrayList<>();
		while (productIterator.hasNext()) {
			List<InvoiceLine> lines = productInvoiceLineSet.get(productIterator.next());
			System.out.println(lines);
			boolean sameProduct = lines.stream()
					.allMatch(it -> it.getUnitPriceUntaxed().equals(lines.get(0).getUnitPriceUntaxed())
							&& it.getTaxRate().equals(lines.get(0).getTaxRate())
							&& it.getDiscription().equals(lines.get(0).getDiscription()));

			if (sameProduct) {
				InvoiceLine mergedLine = new InvoiceLine();
				mergedLine.setProduct(lines.get(0).getProduct());
				mergedLine.setQuantity(
						lines.stream().map(InvoiceLine::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add));
				mergedLine.setUnitPriceUntaxed(lines.get(0).getUnitPriceUntaxed());
				mergedLine.setTaxRate(lines.get(0).getTaxRate());
				mergedLine.setDiscription(lines.get(0).getDiscription());
				mergedLine.setExTaxTotal(
						lines.stream().map(InvoiceLine::getExTaxTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
				mergedLine.setInTaxTotal(
						lines.stream().map(InvoiceLine::getInTaxTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
				mergedLines.add(mergedLine);

			} else {
				for (InvoiceLine originalLine : lines) {
					InvoiceLine copiedLine = invoiceLineRepository.copy(originalLine, false);
					mergedLines.add(copiedLine);
				}
			}
		}
		return mergedLines;

	}

}
