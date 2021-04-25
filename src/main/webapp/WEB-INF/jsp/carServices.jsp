<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Log in with your account</title>
  <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css">
</head>

<body>
<form action="${pageContext.request.contextPath}/createCarService" method="post">
  <p>

  <p>Введите время выполнения услуги:</p>
  <p><input type="number" size="3" name="execution_time" min="1" max="600" value="1"></p>
  <p>Введите цену услуги:</p>
  <p><input type="number" size="3" name="price" min="1" max="1000000" value="1"></p>
  <p>Введите название услуги:</p>
  <input type="text" name="title" size="255">
  </p>
  <input type="hidden" name="action" value="send"/>
  <button type="submit">Send</button>
</form>
<div>
  <table>
    <thead>
    <th>ID</th>
    <th>Title</th>
    <th>Execution time</th>
    <th>Price</th>
    </thead>
    <c:forEach items="${carServices}" var="carService">
      <tr>
        <td>${carService.id}</td>
        <td>${carService.title}</td>
        <td>${carService.execution_time}</td>
        <td>${carService.price}</td>>
        <td>
          <form action="${pageContext.request.contextPath}/deleteCarService" method="post">
            <input type="hidden" name="carServiceId" value="${carService.id}"/>
            <input type="hidden" name="action" value="delete"/>
            <button type="submit">Delete</button>
          </form>
        </td>
    </c:forEach>
      </tr>
  </table>
  <a href="/">Главная</a>
</div>
</body>
</html>