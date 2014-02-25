import org.abs.consumer.entities.Experiment;
import org.abs.consumer.entities.Persona;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Extend HttpServlet class
public class HelloWorld extends HttpServlet {

	private String message;

	public void init() throws ServletException {
		// Do required initialization
		message = "A/B Test";
	}

	public void doGet(HttpServletRequest request,
					  HttpServletResponse response)
			throws ServletException, IOException {
		// Set response content type
		response.setContentType("text/html");

		// Actual logic goes here.
		PrintWriter out = response.getWriter();
		out.println("<h1>" + message + "</h1>");

		Object po = request.getAttribute("abtest");
		Persona persona = null;
		if (null != po) {
			persona = (Persona) po;
			if (null != persona || null != persona.getVariants()) {
				out.println("<body style='background-color: " + persona.getVariants().get("variant2").getActiveGroup().getValue() + "' />");
				for (String key : persona.getVariants().keySet()) {
					out.println("<h2>" + key + "</h2>");
					out.println("<pre>");
					out.println(persona.getVariant(key).getActiveGroup().toJson());
					out.println("</pre>");
				}
				out.println("<img src='" + persona.getVariants().get("variant1").getActiveGroup().getValue() + "' />");
			}

			out.println("<br /><h2><a href=\"index.jsp\">Proceed</a></h2>");
		}
	}

	public void destroy() {
		// do nothing.
	}
}
