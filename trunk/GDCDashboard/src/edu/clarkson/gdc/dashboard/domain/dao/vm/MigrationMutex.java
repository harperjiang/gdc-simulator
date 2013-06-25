package edu.clarkson.gdc.dashboard.domain.dao.vm;

public class MigrationMutex {
	String sourceId;
	String destId;
	String vmName;

	public MigrationMutex(String s, String d, String v) {
		this.sourceId = s;
		this.destId = d;
		this.vmName = v;
	}

	@Override
	public int hashCode() {
		return (sourceId + ":" + vmName).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MigrationMutex) {
			MigrationMutex mm = (MigrationMutex) obj;
			return mm.getSourceId().equals(sourceId)
					&& mm.getVmName().equals(getVmName());
		}
		return super.equals(obj);
	}

	public String getSourceId() {
		return sourceId;
	}

	public String getDestId() {
		return destId;
	}

	public String getVmName() {
		return vmName;
	}

}
