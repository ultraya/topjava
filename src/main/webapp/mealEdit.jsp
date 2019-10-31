<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>

<h3>Редактирование приема пищи</h3>

<form action="meals" method="post">


    <input type="text" name="id" hidden="true" value="${meal.id}">
    Дата/Время:<br>
    <input type="datetime-local" name="dateTime" required value="${meal.dateTime}">
    <br>
    Описание:<br>
    <input type="text" name="description" required value="${meal.description}"><br>
    Калории:<br>
    <input type="text" name="calories" required value="${meal.calories}"><br>
    <br><br>
    <input type="submit" value="Обновить">

</form>

</body>
</html>