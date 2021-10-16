<%@ page import="java.util.List" %>
<%@ page import="com.basejava.webapp.model.*" %>
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
            <%=sectionEntry.getKey()%>
            <br/>
            <c:if test="${sectionEntry.key == SectionType.PERSONAL || sectionEntry.key == SectionType.OBJECTIVE}">
                <c:out value="${sectionEntry.value}"/><br/>
            </c:if>
            <c:if test="${sectionEntry.key == SectionType.ACHIEVEMENTS || sectionEntry.key == SectionType.QUALIFICATIONS}">
                <%
                    ListSection entryValue = (ListSection) sectionEntry.getValue();
                    pageContext.setAttribute("entryValue", entryValue);
                %>
                <c:forEach var="element" items="${entryValue.data}">
                    <c:out value="${element}"/><br/>
                </c:forEach>
            </c:if>
            <c:if test="${sectionEntry.key == SectionType.EDUCATION || sectionEntry.key == SectionType.EXPERIENCE}">
                <%
                    OrganizationSection organizationSection = (OrganizationSection) sectionEntry.getValue();
                    pageContext.setAttribute("organisationSection", organizationSection);
                %>
                <c:forEach var="organisation" items="${organisationSection.data}">
                    <c:out value="${organisation.link.name}"/><br/>
                    <c:if test="${organisation.link.url != null}">
                        <c:out value="${organisation.link.url}"/><br/>
                    </c:if>
                    <c:forEach var="position" items="${organisation.positions}">
                        <c:out value="${position.title}"/><br/>
                        <c:if test="${position.description != null}">
                            <c:out value="${position.description}"/><br/>
                        </c:if>
                        <c:out value="FROM: ${position.startDate} TO: ${position.endDate}"/><br/>
                        <br/>
                    </c:forEach>
                </c:forEach>
            </c:if>
        </c:forEach>
    </p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
