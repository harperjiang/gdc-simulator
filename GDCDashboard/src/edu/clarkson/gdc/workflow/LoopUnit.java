package edu.clarkson.gdc.workflow;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.Validate;

public class LoopUnit extends ExecutionUnit {

	static final String VAR = "var";

	static final String COUNT = "count";

	private List<ExecutionUnit> content;

	@Override
	public void execute(Object[] inputs) throws Exception {
		List<Object> paramList = getParameters(inputs);
		Validate.isTrue(paramList.size() <= 1);
		if (paramList.size() == 0 || paramList.get(0) == null)
			return;
		Object param = paramList.get(0);
		if (!(param instanceof List))
			throw new IllegalArgumentException(param.getClass().getSimpleName());
		@SuppressWarnings("unchecked")
		List<Object> pList = (List<Object>) param;
		String varKey = MessageFormat.format("{0}_{1}", getId(), VAR);
		String countKey = MessageFormat.format("{0}_{1}", getId(), COUNT);
		for (int i = 0; i < pList.size(); i++) {
			Object p = pList.get(i);
			WorkflowContext.get().getContext().put(varKey, p);
			WorkflowContext.get().getContext().put(countKey, i);
			for (ExecutionUnit unit : content) {
				unit.execute(null);
			}
		}
	}

	@Override
	protected List<Object> getParameters(Object[] inputs) {
		return super.getParameters(inputs);
	}

	public List<ExecutionUnit> getContent() {
		return content;
	}

	public void setContent(List<ExecutionUnit> content) {
		this.content = content;
	}

}
