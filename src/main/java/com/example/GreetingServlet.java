package com.example;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/GreetingServlet")
public class GreetingServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(GreetingServlet.class.getName());
    private final static String PAGE_HEADER = "<html><head><title>Greeting Servlet</title></head><body>";
    private final static String PAGE_FOOTER = "</body></html>";
    
    @Inject
    GreetingService greetingService;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var name = req.getParameter("name");
        resp.setContentType("text/html");
        var writer = resp.getWriter();
        writer.println(PAGE_HEADER);
        
        // write message to http response
        writer.println("<p>" + greetingService.buildGreetingMessage(name) + "</p>");
        
        writer.println(PAGE_FOOTER);
        writer.close();
    }
    
}
