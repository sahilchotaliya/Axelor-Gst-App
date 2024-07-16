package com.axelor.account.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.axelor.account.db.Account;
import com.axelor.account.db.Move;
import com.axelor.account.db.MoveLine;
import com.axelor.account.db.repo.MoveLineRepository;
import com.axelor.account.db.repo.MoveRepository;
import com.axelor.invoice.db.InvoiceLine;
import com.axelor.invoice.db.repo.InvoiceRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class InvoiceAccountServiceImpl implements InvoiceAccountService{

	private InvoiceRepository invoiceRepository;
	private MoveRepository moveRepository;
	private MoveLineRepository moveLineRepository;
	
	@Inject
	public InvoiceAccountServiceImpl(InvoiceRepository invoiceRepository,MoveRepository moveRepository,MoveLineRepository moveLineRepository) {
		this.invoiceRepository=invoiceRepository;
		this.moveRepository=moveRepository;
		this.moveLineRepository=moveLineRepository;
	}
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public Move generateMove(Long invoiceId, List<InvoiceLine> invoiceLineList,Account account,BigDecimal inTaxTotal) {
		Move move =new Move();
		move.setInvoice(invoiceRepository.find(invoiceId));
		move.setOperationdate(LocalDate.now());
		for(InvoiceLine list:invoiceLineList) {
			MoveLine moveLine=new MoveLine();
			moveLine.setAccount(list.getProduct().getAccount());
			moveLine.setCredit(list.getInTaxTotal());
			moveLine.setDebit(BigDecimal.ZERO);
			moveLine.setMove(move);
			moveLineRepository.save(moveLine);
		}
		MoveLine moveLine=new MoveLine();
		moveLine.setAccount(account);
		moveLine.setCredit(BigDecimal.ZERO);
		moveLine.setDebit(inTaxTotal);
		moveLine.setMove(move);
		moveLineRepository.save(moveLine);
		moveRepository.save(move);
		return move;
	}

}
