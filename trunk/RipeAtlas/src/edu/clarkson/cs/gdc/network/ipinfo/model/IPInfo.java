package edu.clarkson.cs.gdc.network.ipinfo.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.clarkson.cs.gdc.network.common.deserializer.JsonAttribute;

@Entity
@Table(name = "ip_info")
public class IPInfo {

	@Id
	@Column(name = "ip_address")
	private String ip;

	@Column(name = "continent_code")
	private String continentCode;

	@Column(name = "country_code")
	@JsonAttribute("country_code_iso3166alpha2")
	private String countryCode;

	@Column(name = "country_code_3")
	@JsonAttribute("country_code_iso3166alpha3")
	private String countryCode3;

	@Column(name = "region_code")
	private String regionCode;

	@Column(name = "region_name")
	private String regionName;

	@Column(name = "city")
	private String city;

	@Column(name = "latitude")
	private BigDecimal latitude;

	@Column(name = "longitude")
	private BigDecimal longitude;

	@Column(name = "isp")
	private String isp;

	@Column(name = "organization")
	private String organization;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getContinentCode() {
		return continentCode;
	}

	public void setContinentCode(String continentCode) {
		this.continentCode = continentCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryCode3() {
		return countryCode3;
	}

	public void setCountryCode3(String countryCode3) {
		this.countryCode3 = countryCode3;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

}
