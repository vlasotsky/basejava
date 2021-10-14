<%@ page import="com.basejava.webapp.model.ContactType" %>
<%@ page import="com.basejava.webapp.model.SectionType" %>
<%@ page import="com.basejava.webapp.model.ListSection" %>
<%@ page import="java.util.List" %>
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
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Name:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}" required minlength=1></dd>
        </dl>
        <h3>Contacts:</h3>
        <p>
            <c:forEach var="type" items="<%=ContactType.values()%>">
        <dl>
            <dt>${type.title}</dt>
            <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
        </dl>
        </c:forEach>
        <h3>Sections:</h3>
        <p>
            <c:forEach var="sectionType" items="<%=SectionType.values()%>">
        <dl>
            <dt>${sectionType.title}</dt>
            <textarea name="${sectionType.name()}" cols=100 rows=10><c:out value="${resume.getSection(sectionType)}"/></textarea>
        </dl>
        </c:forEach>
        <hr>
        <button type="submit">Save</button>
        <input type="reset" onclick="window.history.back()" value="Cancel">
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>