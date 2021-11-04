<jsp:useBean id="resume" scope="request" type="com.basejava.webapp.model.Resume"/>
<%@ page import="java.util.List" %>
<%@ page import="com.basejava.webapp.model.*" %>
<%@ page import="com.basejava.webapp.model.SectionType" %>
<%@ page import="com.basejava.webapp.util.HtmlUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<jsp:include page="fragments/header.jsp"/>
<%
    response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1.
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expire", 0);
%>

<style>
    h4 {
        font-family: "Arial Black", monospace;
        color: darkslateblue;
        font-size: small;
        text-align: left;
        border-radius: 20px;
    }
</style>

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
            <dd><input type="text" name="${type.name()}" size=45 value="${resume.getContact(type)}"></dd>
        </dl>
        </c:forEach>
        <hr>

        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resume.getSection(type)}"/>
            <jsp:useBean id="section" type="com.basejava.webapp.model.Section"/>
            <dt><h2><a>${type.title}</a></h2></dt>
            <c:choose>
                <c:when test="${type eq 'OBJECTIVE'}">
                    <input name="${type}" type="text" size=75 value="<%=section%>"/>
                    <br/>
                </c:when>
                <c:when test="${type eq 'PERSONAL'}">
                    <textarea name="${type}" cols=75 rows=5><%=section%></textarea>
                    <br/>
                </c:when>
                <c:when test="${type eq 'QUALIFICATIONS' or type eq 'ACHIEVEMENTS'}">
                    <textarea name="${type}" cols=75
                              rows=5><%=String.join("\n", ((ListSection) section).getData())%></textarea>
                    <br/>
                </c:when>
                <c:when test="${type eq 'EXPERIENCE' or type eq 'EDUCATION'}">
                    <c:forEach var="organisation" items="<%=((OrganizationSection)section).getData()%>"
                               varStatus="counter">
                        <dl>
                            <dt>Organization name:</dt>
                            <dd><input name="${type}" type="text" value="${organisation.link.name}" size=75/></dd>
                            <br/>
                        </dl>
                        <dl>
                            <dt>Webpage:</dt>
                            <dd><input name="${type.name()}url" type="text" value="${organisation.link.url}" size=75>
                            </dd>
                        </dl>
                        <div style="margin-left:30px">
                            <c:forEach var="position" items="${organisation.positions}">
                                <jsp:useBean id="position" type="com.basejava.webapp.model.Organization.Position"/>
                                <dl>
                                    <dt>From Date:</dt>
                                    <dd><input name="${type}${counter.index}startDate" type="text"
                                               value="<%=HtmlUtil.formatDates(position.getStartDate())%>"
                                               placeholder="MM.yyyy"></dd>
                                </dl>
                                <dl>
                                    <dt>Till Date:</dt>
                                    <dd><input name="${type}${counter.index}endDate"
                                               value="<%=HtmlUtil.formatDates(position.getEndDate())%>"
                                               placeholder="MM.yyyy"></dd>
                                </dl>
                                <dl>
                                    <dt>Title:</dt>
                                    <dd><input name="${type}${counter.index}title" type="text"
                                               value="${position.title.replaceAll("\"", "")}"
                                               size=75></dd>
                                </dl>
                                <dl>
                                    <dt>Description:</dt>
                                    <dd><textarea name="${type}${counter.index}description" cols=75
                                                  rows=5>${position.description}</textarea>
                                    </dd>
                                </dl>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Save</button>
        <input type="reset" onclick="window.history.back()" value="Cancel">
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>CV ${resume.fullName}</title>
</head>
</html>