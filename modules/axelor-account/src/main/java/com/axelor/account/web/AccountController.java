package com.axelor.account.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.axelor.account.db.Account;
import com.axelor.account.db.Move;
import com.axelor.account.service.SaleOrderAccountService;
import com.axelor.account.service.InvoiceAccountService;
import com.axelor.contact.db.Contact;
import com.axelor.inject.Beans;
import com.axelor.invoice.db.Invoice;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.sale.db.SaleOrderLine;

public class AccountController {
	public void generateInvoiceFromSaleOrder(ActionRequest request, ActionResponse response) {
		Contact contact = (Contact) request.getContext().get("customer");
		List<SaleOrderLine> saleOrderLineList = (List<SaleOrderLine>) request.getContext().get("saleOrderLineList");
		BigDecimal inTaxTotal = (BigDecimal) request.getContext().get("inTaxTotal");
		Long saleOrderid = (Long) request.getContext().get("id");
		LocalDateTime invoiceDateT;
		if (request.getContext().get("estimatedInvoiceDate") != null) {
			LocalDate date = (LocalDate) request.getContext().get("estimatedInvoiceDate");
			invoiceDateT = date.atStartOfDay();
		} else {
			invoiceDateT = null;
		}
		Invoice genInvoice = Beans.get(SaleOrderAccountService.class).generateInvoice(contact, saleOrderid, inTaxTotal,
				invoiceDateT, saleOrderLineList);
		if (genInvoice == null) {
			response.setError("There is an error with invoice generation");
		} else {
			response.setView(ActionView.define("Invoice").model(Invoice.class.getName()).add("grid", "invoice-grid")
					.add("form", "invoice-form").context("_showRecord", genInvoice.getId()).map());
		}
	}

	public void generateMoveFromInvoice(ActionRequest request, ActionResponse response) {
		Long invoiceId = (Long) request.getContext().get("id");
		List<InvoiceLine> invoiceLineList = (List<InvoiceLine>) request.getContext().get("invoiceLineList");
		Contact contact = (Contact) request.getContext().get("customer");
		Account account = contact.getAccount();
		BigDecimal inTaxTotal = (BigDecimal) request.getContext().get("inTaxTotal");
		Move genmove = Beans.get(InvoiceAccountService.class).generateMove(invoiceId, invoiceLineList, account, inTaxTotal);
		if (genmove == null) {
			response.setError("There is an error with the mve generation");
		} else {
			response.setView(ActionView.define("Move").model(Move.class.getName()).add("grid", "move-grid").add("form", "move-form")
					.context("_showRecord", genmove.getId()).map());
		}
	}

}
