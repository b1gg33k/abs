import org.abs.consumer.entities.Persona;
import org.abs.consumer.entities.Variant;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Extend HttpServlet class
public class HelloWorld extends HttpServlet {
 
  private String message;

  public void init() throws ServletException
  {
      // Do required initialization
      message = "A/B Test";
  }

  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type
      response.setContentType("text/html");

      // Actual logic goes here.
      PrintWriter out = response.getWriter();
      out.println("<h1>" + message + "</h1>");

	  Object po = request.getAttribute("abtest");
	  Persona persona = null;
	  if (null != po){
		  persona = (Persona) po;
		  for (String key : persona.getVariants().keySet()){
			  out.println("<h2>"+key+"</h2>");
			  out.println("<pre>");
			  out.println(persona.getVariant(key).getActiveGroup().toJson());
			  out.println("</pre>");
		  }
	  }
  }
  
  public void destroy()
  {
      // do nothing.
  }
}
