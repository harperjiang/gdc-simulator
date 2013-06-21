package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.clarkson.gdc.dashboard.domain.entity.Battery;
import edu.clarkson.gdc.dashboard.domain.entity.DataCenter;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.PowerSource;

public class XMLNodeDao implements NodeDao {

	private Map<String, Node> nodes;

	public XMLNodeDao() {
		super();
		nodes = new HashMap<String, Node>();
		init();
	}

	protected void init() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("node_structure.xml"));
			Element dataCenters = doc.getDocumentElement();

			NodeList dcList = dataCenters.getElementsByTagName("data-center");

			for (int i = 0; i < dcList.getLength(); i++) {
				Element dcElem = (Element) dcList.item(i);
				DataCenter dc = new DataCenter();
				dc.setId(dcElem.getAttribute("id"));
				dc.setName(dcElem.getAttribute("name"));
				dc.setDescription(dcElem.getElementsByTagName("description")
						.item(0).getTextContent());
				Element attrs = (Element) dcElem.getElementsByTagName(
						"attributes").item(0);
				if (attrs != null && attrs.getParentNode() == dcElem) {
					NodeList attrList = attrs.getElementsByTagName("attribute");
					for (int ii = 0; ii < attrList.getLength(); ii++) {
						Element attr = (Element) attrList.item(ii);
						dc.getAttributes().put(attr.getAttribute("key"),
								attr.getAttribute("value"));
					}
				}
				nodes.put(dc.getId(), dc);

				// Power Source
				NodeList powerNodes = dcElem.getElementsByTagName("power");
				if (powerNodes.getLength() > 0) {
					Element powerElem = (Element) powerNodes.item(0);

					PowerSource powerSource = new PowerSource();
					powerSource.setId(powerElem.getAttribute("id"));
					powerSource.setName(powerElem.getAttribute("name"));
					powerSource.setDescription(powerElem
							.getAttribute("description"));

					Element powerAttrs = (Element) powerElem
							.getElementsByTagName("attributes").item(0);
					if (powerAttrs != null
							&& powerAttrs.getParentNode() == powerElem) {
						NodeList attrList = powerAttrs
								.getElementsByTagName("attribute");
						for (int ii = 0; ii < attrList.getLength(); ii++) {
							Element attr = (Element) attrList.item(ii);
							powerSource.getAttributes().put(
									attr.getAttribute("key"),
									attr.getAttribute("value"));
						}
					}
					nodes.put(powerSource.getId(), powerSource);
					dc.setPowerSource(powerSource);
				}

				// Battery / UPS
				NodeList batteryList = dcElem.getElementsByTagName("battery");
				for (int j = 0; j < batteryList.getLength(); j++) {
					Element batteryElem = (Element) batteryList.item(j);
					Battery battery = new Battery();
					battery.setId(batteryElem.getAttribute("id"));
					battery.setName(batteryElem.getAttribute("name"));
					dc.getBatteries().add(battery);

					Element battrs = (Element) batteryElem
							.getElementsByTagName("attributes").item(0);
					if (battrs != null && battrs.getParentNode() == batteryElem) {
						NodeList attrList = battrs
								.getElementsByTagName("attribute");
						for (int ii = 0; ii < attrList.getLength(); ii++) {
							Element attr = (Element) attrList.item(ii);
							battery.getAttributes().put(
									attr.getAttribute("key"),
									attr.getAttribute("value"));
						}
					}
					nodes.put(battery.getId(), battery);
					// Machine
					NodeList machineList = batteryElem
							.getElementsByTagName("machine");
					for (int k = 0; k < machineList.getLength(); k++) {
						Element machineElem = (Element) machineList.item(k);
						Machine machine = new Machine();
						machine.setId(machineElem.getAttribute("id"));
						machine.setName(machineElem.getAttribute("name"));
						battery.getMachines().add(machine);
						Element mattrs = (Element) machineElem
								.getElementsByTagName("attributes").item(0);
						if (mattrs != null
								&& mattrs.getParentNode() == machineElem) {
							NodeList attrList = mattrs
									.getElementsByTagName("attribute");
							for (int ii = 0; ii < attrList.getLength(); ii++) {
								Element attr = (Element) attrList.item(ii);
								machine.getAttributes().put(
										attr.getAttribute("key"),
										attr.getAttribute("value"));
							}
						}
						nodes.put(machine.getId(), machine);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T extends Node> T getNode(String id) {
		return (T) nodes.get(id);
	}

	@Override
	public <T extends Node> List<T> getNodesByType(Class<T> clazz) {
		PriorityQueue<T> pq = new PriorityQueue<T>(10, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		for (Entry<String, Node> entry : nodes.entrySet()) {
			if (entry.getValue().getClass() == clazz)
				pq.add((T) entry.getValue());
		}
		List<T> result = new ArrayList<T>();
		result.addAll(pq);
		return result;
	}
}
