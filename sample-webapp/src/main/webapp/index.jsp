<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>Sample Application JSP Page</title>
</head>
<c:set var="persona" value="${requestScope.abtest}" />
<body bgcolor="${persona.variants.variant2.activeGroup.value}">
<h1>${persona.variants.variant3.activeGroup.value}</h1>

</body>
</html>
