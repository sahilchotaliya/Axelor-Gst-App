package com.axelor.dashboard.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DashboardService {
	public List<Map<String,Object>> getDataList(Long customerId,LocalDateTime startDateTime,LocalDateTime endDateTime);
}
