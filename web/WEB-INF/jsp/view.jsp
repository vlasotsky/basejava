<%@ page import="java.util.List" %>
<%@ page import="com.basejava.webapp.model.*" %>
<%@ page import="com.basejava.webapp.util.HtmlUtil" %>
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
    <p>
    <hr>

    <table>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.basejava.webapp.model.SectionType, com.basejava.webapp.model.Section>"/>
            <c:set var="type" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <jsp:useBean id="section" type="com.basejava.webapp.model.Section"/>
            <tr>
                <c:if test="${type.name() == 'OBJECTIVE'}">
                    <td><h3><c:out value="${type.title}"/></h3></td>
                    <td>
                        <h3><%=((TextSection) section).getData()%>
                        </h3>
                    </td>
                </c:if>
            </tr>
            <c:if test="${type.name() != 'OBJECTIVE'}">
                <c:choose>
                    <c:when test="${type.name() == 'PERSONAL'}">
                        <tr>
                            <td colspan="2">
                                <h3>${type.title}</h3>
                            </td>
                            <td>
                                <%=((TextSection) section).getData()%>
                            </td>
                        </tr>
                    </c:when>
                    <c:when test="${type.name() eq 'ACHIEVEMENTS' or type.name() eq 'QUALIFICATIONS'}">
                        <h3>${type.title}</h3>
                        <tr>
                            <td colspan="2">
                                <ul>
                                    <c:forEach var="element" items="<%=((ListSection) section).getData()%>">
                                        <li>${element}</li>
                                    </c:forEach>
                                </ul>
                            </td>
                        </tr>
                    </c:when>
                    <c:when test="${type.name() eq 'EXPERIENCE' or type.name() eq 'EDUCATION'}">
                        <tr>
                            <td><h3>${type.title}</h3></td>
                        </tr>
                        <c:forEach var="organisation" items="<%=((OrganizationSection) section).getData()%>">
                            <jsp:useBean id="organisation" type="com.basejava.webapp.model.Organization"/>
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty organisation.link.url}">
                                            <h3>${organisation.link.name}</h3>
                                        </c:when>
                                        <c:otherwise>
                                            <h3><a href="${organisation.link.url}">${organisation.link.name}</a></h3>
                                        </c:otherwise>

                                    </c:choose>
                                </td>
                            </tr>
                            <c:forEach var="position" items="${organisation.positions}">
                                <jsp:useBean id="position" type="com.basejava.webapp.model.Organization.Position"/>
                                <tr>
                                    <td><%=HtmlUtil.formatDates(position.getStartDate()) + " - " + HtmlUtil.formatDates(position.getEndDate())%>
                                    </td>
                                    <td><h4>${position.title}</h4>
                                        <br/>
                                        <c:if test="${not empty position.description}">
                                            <c:out value="${position.description}"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </c:if>
        </c:forEach>
    </table>
    <button onclick="window.history.back()">OK</button>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
