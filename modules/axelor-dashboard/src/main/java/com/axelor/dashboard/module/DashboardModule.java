package com.axelor.dashboard.module;

import com.axelor.app.AxelorModule;
import com.axelor.dashboard.services.DashboardService;
import com.axelor.dashboard.services.DashboardServiceImpl;

public class DashboardModule extends AxelorModule{
	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bind(DashboardService.class).to(DashboardServiceImpl.class);
		super.configure();
	}
}
