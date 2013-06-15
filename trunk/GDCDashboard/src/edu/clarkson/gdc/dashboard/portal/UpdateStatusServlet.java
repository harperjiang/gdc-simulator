package edu.clarkson.gdc.dashboard.portal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.gdc.common.ApplicationContextHolder;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.service.InterfaceService;

/**
 * Servlet that receive status updates
 */
public class UpdateStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateStatusServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Support only POST method
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final static String node = "";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			InterfaceService interfaceService = ApplicationContextHolder
					.getInstance().getApplicationContext()
					.getBean("interfaceService", InterfaceService.class);
			// Parse input data
			BufferedReader br = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			String line = br.readLine();
			if (line != null) {
				String[] datas = line.split(",");
				// Save each data
				for (String data : datas) {
					if (StringUtils.isNotEmpty(data)) {
						String[] pair = data.split("=");
						if (NodeStatus.isStatus(pair[0])) {
							interfaceService.updateNodeStatus(node, pair[0],
									pair[1]);
						}
						if (NodeHistory.isHistory(pair[0])) {
							interfaceService.updateNodeHistory(node, pair[0],
									pair[1], new Date());
						}
					}
				}
				// Iwind=34.02,Ibat=23.69,Iinv=31.88,Vbat=44.66,SOC=42.05,
			}
		} catch (Exception e) {
			logger.error("Exception occurred in Interface", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
