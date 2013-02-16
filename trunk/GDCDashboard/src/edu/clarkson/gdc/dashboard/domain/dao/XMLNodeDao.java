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

public class XMLNodeDao implements NodeDao {

	private boolean init = false;

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
				nodes.put(dc.getId(), dc);

				NodeList batteryList = dcElem.getElementsByTagName("battery");
				for (int j = 0; j < batteryList.getLength(); j++) {
					Element batteryElem = (Element) batteryList.item(j);
					Battery battery = new Battery();
					battery.setId(batteryElem.getAttribute("id"));
					battery.setName(batteryElem.getAttribute("name"));
					dc.getBatteries().add(battery);
					nodes.put(battery.getId(), battery);

					NodeList machineList = batteryElem
							.getElementsByTagName("machine");
					for (int k = 0; k < machineList.getLength(); k++) {
						Element machineElem = (Element) machineList.item(k);
						Machine machine = new Machine();
						machine.setId(machineElem.getAttribute("id"));
						machine.setName(machineElem.getAttribute("name"));
						battery.getMachines().add(machine);
						nodes.put(machine.getId(), machine);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Node getNode(String id) {
		return nodes.get(id);
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
