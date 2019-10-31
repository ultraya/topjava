<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<jsp:useBean id="meals" scope="request" type="java.util.List"/>
<html>
<head>
</head>
<body>
<table>

    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <c:out value="${meal.excess ? '<tr style=\"color: red\">' : '<tr style=\"color: green\">'}" escapeXml="false"/>

        <td><%= TimeUtil.format(meal.getDateTime())%>
        </td>

        <td>${meal.description}</td>

        <td>${meal.calories}</td>
        <td>
            <a href="meals?id=${meal.id}&action=edit">Edit</a>
        </td>
        <td>
            <a href="meals?id=${meal.id}&action=delete">Delete</a>
        </td>
        <c:out value="${'</tr>'}" escapeXml="false"/>
    </c:forEach>
</table>

<br>
<br>

<form action="meals" method="post">
    <fieldset>
        <legend>Добавить прием пищи:</legend>

        Дата/Время:<br>
        <input type="datetime-local" name="dateTime" required>
        <br>
        Описание:<br>
        <input type="text" name="description" required><br>
        Калории:<br>
        <input type="text" name="calories" required><br>
        <br><br>
        <input type="submit" value="Добавить">
    </fieldset>
</form>
</body>
</html>
