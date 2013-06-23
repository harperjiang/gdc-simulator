package edu.clarkson.gdc.workflow;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicWorkflow implements Workflow {

	private List<ExecutionUnit> units;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void execute(Object... params) {
		try {
			for (ExecutionUnit unit : units) {
				unit.execute(params);
			}
		} catch (Exception e) {
			logger.error("Exception in workflow", e);
		}
	}

	public List<ExecutionUnit> getUnits() {
		return units;
	}

	public void setUnits(List<ExecutionUnit> units) {
		this.units = units;
	}

}
