package com.axelor.dashboard.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.axelor.dashboard.services.DashboardService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class DashboardController{
	public void getInvoiceData(ActionRequest request, ActionResponse response) {
		if(request.getContext().get("customerId")==null) {
			return;
		}
		LinkedHashMap<String, Object> customerMap;
		customerMap = (LinkedHashMap<String, Object>) request.getContext().get("customerId");
		customerMap = (LinkedHashMap<String, Object>) request.getContext().get("customerId");
		Long customerId = ((Integer) customerMap.get("id")).longValue();
		String startDateStr = (String) request.getContext().get("calender");
		LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = startDate.plusMonths(3).atStartOfDay();
		List<Map<String,Object>> resultList=Beans.get(DashboardService.class).getDataList(customerId,startDateTime,endDateTime);
		response.setData(resultList);
	}

}
