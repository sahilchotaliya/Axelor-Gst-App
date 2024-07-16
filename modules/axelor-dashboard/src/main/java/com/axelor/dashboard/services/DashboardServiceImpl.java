package com.axelor.dashboard.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;

import com.axelor.db.JPA;
import com.google.common.collect.Maps;

public class DashboardServiceImpl implements DashboardService{

	@Override
	public List<Map<String, Object>> getDataList(Long customerId,LocalDateTime startDateTime,LocalDateTime endDateTime) {
		
		Query q1 = JPA.em().createNativeQuery(
				"SELECT to_char(ii.created_on, 'MM-YYYY') AS _date,\n"+
			    "sum(ii.ex_tax_total) as amount\n"+
			"FROM invoice_invoice AS ii\n"+
			"WHERE ii.customer =:customerId\n"+
			    "AND ii.status_select != 0\n"+
			    "AND ii.invoice_datet BETWEEN :startDate AND :endDate\n"+
			"GROUP BY _date");

		q1.setParameter("customerId", customerId);
		q1.setParameter("startDate", startDateTime);
		q1.setParameter("endDate", endDateTime);
		List<Object[]> resultList = q1.getResultList();
		List<Map<String, Object>> result = new ArrayList<>();

		resultList.forEach(element -> {
			Map<String, Object> dataMap = Maps.newHashMap();
			dataMap.put("_amount", element[1]);
			dataMap.put("_month", element[0]);
			result.add(dataMap);
		});
		
		return result;
	}

}
