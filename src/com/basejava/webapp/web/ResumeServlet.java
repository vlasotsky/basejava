package com.basejava.webapp.web;

import com.basejava.webapp.Config;
import com.basejava.webapp.ResumeTestData;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.SqlStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ResumeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        SqlStorage sqlStorage = Config.getSqlStorage();
        sqlStorage.save(ResumeTestData.makeTestResume(UUID.randomUUID().toString(), "Judy"));
        String name = request.getParameter("name");
        LinkedHashMap<String, Resume> map = new LinkedHashMap<>();
        for (Resume element : sqlStorage.getAllSorted()) {
            map.put(element.getFullName(), element);
        }

        if (name == null) {
            response.getWriter().write("<html>" +
                    "<style>" +
                        "table, th, td {" +
                        "border:1px solid black;" +
                    "}" +
                    "</style>" +
                    "<table border>" +
                        "<tr> " +
                            "<th>uuid</th> " +
                            "<th>full_name</th>" +
                        "</tr>");
            for (Map.Entry<String, Resume> entry : map.entrySet()) {
                Resume resume = entry.getValue();
                response.getWriter().write("<tr>" +
                        "<td>" + resume.getUuid() + "</td>" +
                        "<td>" + resume.getFullName() + "</td>" +
                        "</tr>");
            }
            response.getWriter().write("</table>" +
                    "</html>");
        } else {
            Resume resume = map.get(name);

            response.getWriter().write("<html>" +
                    "<style>" +
                    "table, th, td {" +
                    "  border:1px solid black;" +
                    "}" +
                    "</style>" +
                    "<table border> " +
                        "<tr> " +
                            "<th>uuid</th> " +
                            "<th>full_name</th>" +
                        "</tr>" +
                        "<tr>" +
                            "<td>" + resume.getUuid() + "</td>" +
                            "<td>" + resume.getFullName() + "</td>" +
                        "</tr>" +
                    "</table>" +
                    "</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
