package edu.clarkson.gdc.dashboard.portal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.gdc.common.ApplicationContextHolder;
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
				String[] data = line.split(",");
				// TODO Save each data
				//Iwind=34.02,Ibat=23.69,Iinv=31.88,Vbat=44.66,SOC=42.05,
			}
		} catch (Exception e) {
			logger.error("Exception occurred in Interface", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
