package edu.clarkson.gdc.workflow;

import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicWorkflow implements Workflow {

	private List<ExecutionUnit> units;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void execute(Object... params) {
		for (ExecutionUnit unit : units) {
			try {
				unit.execute(params);
			} catch (Exception e) {
				logger.error("Exception in workflow", e);
				logger.error(MessageFormat.format(
						"ExecutionUnitInfo:Id:{0},Params:{1}", unit.getId(),
						unit.getParams()));
				if (e instanceof RuntimeException)
					throw (RuntimeException) e;
				throw new RuntimeException(e);
			}
		}
	}

	public List<ExecutionUnit> getUnits() {
		return units;
	}

	public void setUnits(List<ExecutionUnit> units) {
		this.units = units;
	}

}
