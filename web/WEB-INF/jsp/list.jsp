<%@ page import="com.basejava.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="com.basejava.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.UUID" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>List of all CVs</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <a href="resume?action=add"><img src="img/add.png" width=70 height=50></a>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Name</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>
        <tr>
        </tr>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.basejava.webapp.model.Resume"/>
            <tr>
                <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                <td><%=ContactType.EMAIL_ADDRESS.toHtml(resume.getContact(ContactType.EMAIL_ADDRESS))%>
                </td>
                <td><a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png" width=30 height=30></a>
                </td>
                <td><a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png" width=30 height=30></a>
                </td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
