package com.example;

import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/notes")
public class NoteServlet extends HttpServlet {

    @EJB
    private NoteService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String add = req.getParameter("add");
        if (add != null && !add.isBlank()) {
            Note n = service.add(add);
            out.println("Saved: id=" + n.getId() + ", text=" + n.getText());
            out.println();
        }

        out.println("All notes:");
        for (Note n : service.findAll()) {
            out.println(n.getId() + ": " + n.getText());
        }
    }
}