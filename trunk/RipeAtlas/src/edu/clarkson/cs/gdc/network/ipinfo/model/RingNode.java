package edu.clarkson.cs.gdc.network.ipinfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ring_node")
public class RingNode {

	@Id
	@Column(name = "ip_address")
	private String ip;

	@Column(name = "name")
	private String name;

	@Column(name = "country")
	private String country;

	@Column(name = "city")
	private String city;

	@Column(name = "ip_v6")
	private String ipV6;

	@Column(name = "asn")
	private int asn;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIpV6() {
		return ipV6;
	}

	public void setIpV6(String ipV6) {
		this.ipV6 = ipV6;
	}

	public int getAsn() {
		return asn;
	}

	public void setAsn(int asn) {
		this.asn = asn;
	}

}
