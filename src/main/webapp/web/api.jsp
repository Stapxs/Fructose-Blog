<%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/02/24
  Time: 下午 03:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String code = (String) request.getAttribute("code");
    String message = (String) request.getAttribute("str");
    response.setStatus(Integer.parseInt(code));
    out.print(message);
%>