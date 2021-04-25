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
<div>
  <div>Вы ${placeQueue} в очереди</div>
  <div>${timeToService}</div><br>

  <table>
    <thead>
    <th>ID</th>
    <th>ReservationTime</th>
    <th>CarServices</th>
    <th>UserName</th>
    </thead>
    <tr>
      <td>${reservation.id}</td>
      <td>${reservation.reservation_time}</td>
      <td>${reservation.getCarServices()}</td>>
      <td>${reservation.getUsers().getUsername()}</td>
      <td>
        <form action="${pageContext.request.contextPath}/deleteReservation" method="post">
          <input type="hidden" name="reservationId" value="${reservation.id}"/>
          <input type="hidden" name="action" value="delete"/>
          <button type="submit">Delete</button>
        </form>
      </td>
    </tr>
  </table>
  <a href="/">Главная</a>
</div>
</body>
</html>