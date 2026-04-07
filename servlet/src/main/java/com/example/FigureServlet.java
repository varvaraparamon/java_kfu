package com.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/figure")
public class FigureServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        
        String param = req.getParameter("param");
        String cookieValue = null;
        
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("fig")) cookieValue = c.getValue();
            }
        }

        String fig;
        if (param != null && !param.isEmpty()) {
            fig = param;
            resp.addCookie(new Cookie("fig", param));
        } else if (cookieValue != null) {
            fig = cookieValue;
        } else {
            fig = "3";
        }
        
        PrintWriter out = resp.getWriter();
        out.println("<svg width='200' height='200'>");
        
        if (fig.equals("1")) {
            out.println("<circle cx='100' cy='100' r='80' fill='red'/>");
        } else if (fig.equals("2")) {
            out.println("<rect x='20' y='20' width='160' height='160' fill='blue'/>");
        } else {
            out.println("<polygon points='100,10 190,190 10,190' fill='green'/>");
        }
        
        out.println("</svg>");
    }
}