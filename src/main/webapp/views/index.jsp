<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">

    
    <link rel="stylesheet" href="/CSS/scrollbarStyle.css">
<meta charset="ISO-8859-1">
<title>Medicare</title>
</head>
<body>
<div class="container">
<h3 style="font-weight: lighter;padding-top: 0.5em;">Medicare&nbsp;&nbsp;&nbsp;&nbsp;About&nbsp;&nbsp;&nbsp;&nbsp;Contact&nbsp;&nbsp;&nbsp;&nbsp;ViewProducts
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Sign Up&nbsp;&nbsp;&nbsp;&nbsp;Login&nbsp;&nbsp;</h3>

</div>
<hr>

<h1 style="text-align: center;">THIS IS SERVER BACKEND API RUNNING ON 8084 PORT</h1>

<a href="/CSS/scrollbarStyle.css">test scrollbarStyle.css link</a>

<h5>Testing Model Attribute in JSP <%=request.getAttribute("testModelAttribureName")%></h5>
	<% if(request.getAttribute("testModelAttribureName").equals("testModelAttribureValue")) { %>
	<h5>This is printed for if block where condition is request.getAttribute("testModelAttribureName").equals("testModelAttribureValue")</h5>
	<% } else {%>
	<h5>This is printed for else block</h5>
	<% } %>
	
	<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj" crossorigin="anonymous"></script>
</body>
</html>