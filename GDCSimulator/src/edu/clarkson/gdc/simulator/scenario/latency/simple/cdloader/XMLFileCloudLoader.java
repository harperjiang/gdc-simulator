package edu.clarkson.gdc.simulator.scenario.latency.simple.cdloader;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.ProcessTimeModel.ConstantTimeModel;
import edu.clarkson.gdc.simulator.scenario.latency.simple.CloudDataLoader;
import edu.clarkson.gdc.simulator.scenario.latency.simple.DefaultDataCenter;
import edu.clarkson.gdc.simulator.scenario.latency.simple.client.DefaultWorkloadProvider;
import edu.clarkson.gdc.simulator.scenario.latency.simple.client.RequestIndexClient;
import edu.clarkson.gdc.simulator.scenario.latency.simple.exstr.RangeExceptionStrategy;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class XMLFileCloudLoader implements CloudDataLoader {

	private boolean init = false;

	private int unit;

	private List<DefaultDataCenter> dataCenters;

	private List<Client> clients;

	protected void init() {
		if (!init) {

			dataCenters = new ArrayList<DefaultDataCenter>();
			clients = new ArrayList<Client>();
			// Load XML files

			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = dbf.newDocumentBuilder();
				Document doc = builder.parse(Thread.currentThread()
						.getContextClassLoader()
						.getResourceAsStream("cloud.xml"));
				Element cloud = doc.getDocumentElement();
				NodeList nodeList = cloud.getChildNodes();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if ("config".equals(node.getNodeName())) {
						Element configElement = (Element) node;
						unit = Integer.valueOf(configElement
								.getAttribute("unit"));
					}
					if ("datacenters".equals(node.getNodeName())) {
						NodeList dcs = ((Element) node)
								.getElementsByTagName("datacenter");
						for (int j = 0; j < dcs.getLength(); j++) {
							Element dcNode = (Element) dcs.item(j);

							DefaultDataCenter dc = new DefaultDataCenter();
							dc.setId(dcNode.getAttribute("id"));
							Element dcLocNode = (Element) ((Element) dcNode)
									.getElementsByTagName("location").item(0);
							dc.setLocation(new Point2D.Double(Double
									.valueOf(dcLocNode.getAttribute("x")),
									Double.valueOf(dcLocNode.getAttribute("y"))));
							dc.setTimeModel(new ConstantTimeModel(Long
									.valueOf(dcNode.getAttribute("latency"))));
							RangeExceptionStrategy rfs = new RangeExceptionStrategy();
							rfs.load(
									dcNode.getAttribute("datafile").toString(),
									unit);
							dc.setExceptionStrategy(rfs);

							dataCenters.add(dc);
						}
					}
					if ("clients".equals(node.getNodeName())) {
						NodeList clientNodeList = ((Element) node)
								.getElementsByTagName("client");
						for (int j = 0; j < clientNodeList.getLength(); j++) {
							Element clientNode = (Element) clientNodeList
									.item(j);

							RequestIndexClient client = new RequestIndexClient();
							client.setId(clientNode.getAttribute("id"));
							Element clientLocNode = (Element) ((Element) clientNode)
									.getElementsByTagName("location").item(0);
							client.setLocation(new Point2D.Double(Double
									.valueOf(clientLocNode.getAttribute("x")),
									Double.valueOf(clientLocNode
											.getAttribute("y"))));
							DefaultWorkloadProvider workloadProvider = new DefaultWorkloadProvider();
							workloadProvider.load(
									clientNode.getAttribute("workload_file"),
									unit);
							client.setProvider(workloadProvider);

							clients.add(client);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			init = true;
		}
	}

	public int loadUnit() {
		init();
		return unit;
	}

	@Override
	public List<DefaultDataCenter> loadDataCenters() {
		init();
		return dataCenters;
	}

	@Override
	public List<Client> loadClients() {
		init();
		return clients;
	}

}
