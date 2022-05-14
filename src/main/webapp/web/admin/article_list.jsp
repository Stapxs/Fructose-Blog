<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/05/10
  Time: 下午 03:47
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
    <link rel="stylesheet" href="/css/admin/main.css">

    <script>
        is_auto_dark = true
    </script>
</head>
<style>
    .list-main {
        margin-top: 10px;
    }
    .list-main > div:first-child {
        border-radius: 7px 7px 0 0;
    }
    .list-main > div:last-child {
        border-radius: 0 0 7px 7px;
    }
    .list-main > div:not(:first-child):hover {
        background: var(--color-card-2);
        color: var(--color-font);
    }
    .list-main > div:not(:first-child):hover button {
        background: #ffffff69;
    }

    .list-body {
        background: var(--color-card-1);
        display: flex;
        padding: 10px 15px;
        flex-direction: row;
        align-items: center;
        transition: background .3s;
        color: var(--color-font);
    }
    .list-body > div {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
    .list-body > div:first-child {
        width: 40%;
        padding-right: 20px;
        cursor: pointer;
    }
    .list-body > div:last-child {
        width: 20%;
        display: flex;
        flex-direction: row-reverse;
        justify-content: space-between;
    }
    .list-body > div:not(:first-child):not(:last-child) {
        width: 20%;
    }
    .list-body button {
        background: var(--color-card-2);
        color: var(--color-font-1);
        width: 100%;
        margin-left: 10px;
        transition: background .3s;
    }

    .list-empty {
        color: var(--color-font-2);
        background: var(--color-card-1);
        border-radius: 7px;
        padding: 15px;
        margin-top: 10px;
        text-align: center;
    }
</style>
<body style="margin-right: 6px;background: var(--color-card);" id="body">
<div class="opt-bar" style="margin-top: 0;">
    <div></div>
    <span>公开</span>
</div>
<div id="public" class="list-main">
</div>
<div class="opt-bar">
    <div></div>
    <span>私密</span>
</div>
<div id="private" class="list-main">

</div>
<div class="opt-bar">
    <div></div>
    <span>待审核</span>
</div>
<div id="wait" class="list-main">

</div>
<div class="opt-bar">
    <div></div>
    <span>草稿</span>
</div>
<div id="draft" class="list-main">

</div>

</body>
<script src="https://api.stapxs.cn/js/util/jquery-1.12.4.js"></script>
<script src="https://api.stapxs.cn/bootstrap/popper.min.js"></script>
<script src="https://api.stapxs.cn/bootstrap/bootstrap.min.js"></script>
<script src="https://api.stapxs.cn/js/main.js"></script>
<script src="https://api.stapxs.cn/js/auto-theme.js"></script>
<script src="/js/admin/article_list.js"></script>
</html>

