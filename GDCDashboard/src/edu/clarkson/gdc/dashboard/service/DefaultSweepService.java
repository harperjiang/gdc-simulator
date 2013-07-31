package edu.clarkson.gdc.dashboard.service;

import java.util.Calendar;
import java.util.Date;

import edu.clarkson.gdc.dashboard.domain.dao.HistoryDao;

public class DefaultSweepService implements SweepService {

	private HistoryDao historyDao;

	public HistoryDao getHistoryDao() {
		return historyDao;
	}

	public void setHistoryDao(HistoryDao historyDao) {
		this.historyDao = historyDao;
	}

	@Override
	public void cleanHistory() {
		Date current = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		getHistoryDao().cleanHistory(cal.getTime());
	}

}
