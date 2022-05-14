<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/05/07
  Time: 下午 04:38
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html style="overflow-x: hidden;">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">

    <link rel="stylesheet" href="https://api.stapxs.cn/css/color-dark.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/css/style.css">
    <link rel="stylesheet" href="/css/admin/theme.css">
    <link rel="stylesheet" href="/css/admin/main.css">

    <script>
        is_auto_dark = true
    </script>
</head>
<body style="margin-right: 6px;background: var(--color-card);">
<div class="opt-bar" style="margin-top: 0;">
    <div></div>
    <span>主题列表</span>
    <button class="ss-button">修改当前主题设置</button>
</div>
<div id="theme-list"></div>
</body>
<script src="https://api.stapxs.cn/js/util/jquery-1.12.4.js"></script>
<script src="https://api.stapxs.cn/bootstrap/popper.min.js"></script>
<script src="https://api.stapxs.cn/bootstrap/bootstrap.min.js"></script>
<script src="https://api.stapxs.cn/js/main.js"></script>
<script src="https://api.stapxs.cn/js/auto-theme.js"></script>
<script src="/js/admin/theme.js"></script>
</html>

