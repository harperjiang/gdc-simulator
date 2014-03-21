package gdc.network.ripeatlas.model;

public class MeasurementCreate {

	private String description;

	private int af = 4;

	private String type;

	private boolean resolveOnProbe;

	private boolean isOneoff;

	private boolean isPublic;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAf() {
		return af;
	}

	public void setAf(int af) {
		this.af = af;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isResolveOnProbe() {
		return resolveOnProbe;
	}

	public void setResolveOnProbe(boolean resolveOnProbe) {
		this.resolveOnProbe = resolveOnProbe;
	}

	public boolean isOneoff() {
		return isOneoff;
	}

	public void setOneoff(boolean isOneoff) {
		this.isOneoff = isOneoff;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

}
