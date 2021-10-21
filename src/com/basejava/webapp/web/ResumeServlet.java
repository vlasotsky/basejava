package com.basejava.webapp.web;

import com.basejava.webapp.Config;
import com.basejava.webapp.exception.NotExistingStorageException;
import com.basejava.webapp.model.*;
import com.basejava.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.getSqlStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0);
        request.setCharacterEncoding("UTF-8");

        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume resume;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "add":
                resume = new Resume(UUID.randomUUID().toString(), "");
                break;
            case "view":
            case "edit":
                resume = storage.get(uuid);
                break;
            default:
                throw new IllegalArgumentException("Action" + action + " is illegal.");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume resume;
        try {
            resume = storage.get(uuid);
        } catch (NotExistingStorageException e) {
            resume = new Resume(uuid, fullName);
            storage.save(resume);
        }
        resume.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }
        for (SectionType sectionType : SectionType.values()) {
            String value = request.getParameter(sectionType.name());
            switch (SectionType.valueOf(sectionType.name())) {
                case PERSONAL, OBJECTIVE:
                    if (value != null && value.trim().length() != 0) {
                        resume.addSection(sectionType, new TextSection(value));
                    } else {
                        resume.getSections().remove(sectionType);
                    }
                    break;
                case ACHIEVEMENTS, QUALIFICATIONS:
                    if (value != null && value.trim().length() != 0) {
                        List<String> list = new ArrayList<>(Arrays.asList(value.split("\n")));
                        list.removeIf(item -> item == null || item.equals("\r"));
                        resume.addSection(sectionType, new ListSection(list));
                    } else {
                        resume.getSections().remove(sectionType);
                    }
            }
        }
        if (resume.getFullName() == null || resume.getFullName().trim().equals("")) {
            storage.delete(resume.getUuid());
        }
        storage.update(resume);
        response.sendRedirect("resume");
    }
}
