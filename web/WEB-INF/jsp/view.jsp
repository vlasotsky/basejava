<%@ page import="com.basejava.webapp.model.ListSection" %>
<%@ page import="com.basejava.webapp.model.SectionType" %>
<%@ page import="com.basejava.webapp.model.Section" %>
<%@ page import="com.basejava.webapp.model.TextSection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.basejava.webapp.model.Resume" scope="request"/>
    <title>CV ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>

<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png" height=40
                                                                                      width=40></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.basejava.webapp.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.basejava.webapp.model.SectionType, com.basejava.webapp.model.Section>"/>
            <%=sectionEntry.getKey()%><a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"
                                                                                            height=20 width=20></a>
            <br/>
            <%
                switch (sectionEntry.getKey()) {
                    case OBJECTIVE:
                    case PERSONAL:
                        TextSection textSection = (TextSection) sectionEntry.getValue();
                        out.println(textSection.getData() + "<br>");
                        break;
                    case QUALIFICATIONS:
                    case ACHIEVEMENTS:
                        ListSection listSection = (ListSection) sectionEntry.getValue();
                        for (String element : listSection.getData()) {
                            out.println(element + "<br>");
                        }
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        break;
                }
            %>
        </c:forEach>
    </p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
