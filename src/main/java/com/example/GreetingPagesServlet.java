package com.example;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/GreetingPagesServlet")
public class GreetingPagesServlet extends HttpServlet {

    @Inject
    private GreetingService greetingService;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        var name = req.getParameter("name");
        var message = greetingService.buildGreetingMessage(name);
        req.setAttribute("hello", message);
        req.getRequestDispatcher("/hello-pages.jspx").forward(req, resp);
    }

}
