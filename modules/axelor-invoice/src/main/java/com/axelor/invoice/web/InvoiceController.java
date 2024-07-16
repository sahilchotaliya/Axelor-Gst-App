package com.axelor.invoice.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.axelor.inject.Beans;
import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.invoice.db.repo.InvoiceRepository;
import com.axelor.invoice.services.InvoiceService;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceController {
	public void setinvoiceDateT(ActionRequest request, ActionResponse response) {
		response.setValue("invoiceDateT", LocalDateTime.now());
	}

	public void setTaxTotal(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		BigDecimal inTaxTotal = new BigDecimal(0);
		BigDecimal exTaxTotal = new BigDecimal(0);
		BigDecimal unitpriceuntaxed = new BigDecimal(0);
		for (int i = 0; i < invoice.getInvoiceLineList().size(); i++) {
			unitpriceuntaxed = invoice.getInvoiceLineList().get(i).getUnitPriceUntaxed();
			exTaxTotal = exTaxTotal.add(unitpriceuntaxed.multiply(invoice.getInvoiceLineList().get(i).getQuantity()));
			inTaxTotal = inTaxTotal.add(invoice.getInvoiceLineList().get(i).getInTaxTotal());
		}
		response.setValue("exTaxTotal", exTaxTotal);
		response.setValue("inTaxTotal", inTaxTotal);
	}

	public void onValidate(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		if (CollectionUtils.isEmpty(invoice.getInvoiceLineList())) {
			response.setError("Empty List!!", "At least one invoice line is required");
		} else {
			boolean flag = true;
			for (InvoiceLine invoiceLine : invoice.getInvoiceLineList()) {
				if (invoiceLine.getExTaxTotal().compareTo(BigDecimal.ZERO) <= 0) {
					response.setError("One invoice line has a null or negative total");
					flag = false;
					break;
				}
			}
			if (flag) {
				setTaxTotal(request, response);
				response.setValue("statusSelect", 1);
			}
		}
	}

	public void onVentilate(ActionRequest request, ActionResponse response) {
		onValidate(request, response);
		response.setValue("statusSelect", 2);
	}

	public void onCancelAlert(ActionRequest request, ActionResponse response) {
		response.setAlert("This action will cancel this invoice.Do you want to proceed?");
	}

	public void onCancel(ActionRequest request, ActionResponse response) {
		response.setValue("statusSelect", 3);
		setTaxTotal(request, response);
	}

	public void cancelInvoiceList(ActionRequest request, ActionResponse response) {
		List<Integer> list = (List<Integer>) request.getContext().get("_ids");
		List<Invoice> invoices = new ArrayList<>();
		List<Long> longInvoiceIds = list.stream().map(Integer::longValue).collect(Collectors.toList());
		for (Long id : longInvoiceIds) {
			Invoice invoice = Beans.get(InvoiceRepository.class).find(id);
			if (invoice != null) {
				invoices.add(invoice);
			}
		}
		Beans.get(InvoiceService.class).cancelInvoiceList(invoices);
		response.setReload(true);
	}

	public void mergeInvoiceController(ActionRequest request, ActionResponse response) {

		List<Map<String, Object>> idofinvoice = (List<Map<String, Object>>) request.getContext()
				.get("invoiceToMergeList");
		if (CollectionUtils.isEmpty(idofinvoice) || idofinvoice.size() < 2) {
			return;
		} else {
			List<Long> idList = idofinvoice.stream().map(it -> Long.parseLong(it.get("id").toString()))
					.collect(Collectors.toList());
			List<Invoice> invoiceList = new ArrayList<Invoice>();
			for (Long id : idList) {
				Invoice invoice = Beans.get(InvoiceRepository.class).find(id);
				if (invoice != null) {
					invoiceList.add(invoice);
				}
			}
			Invoice generatedInvoice = Beans.get(InvoiceService.class).generateInvoice(invoiceList);
			if (generatedInvoice == null) {
				response.setError("You have to choose the same customer");
			} else {
				response.setView(ActionView.define("Invoice").model(Invoice.class.getName()).add("grid", "invoice-grid")
						.add("form", "invoice-form").context("_showRecord", generatedInvoice.getId()).map());
				response.setCanClose(true);
			}
		}
	}
}
