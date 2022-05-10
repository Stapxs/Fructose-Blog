<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/05/09
  Time: 下午 07:08
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
    <link rel="stylesheet" href="/css/admin/sort.css">
    <link rel="stylesheet" href="/css/admin/main.css">

    <script>
        is_auto_dark = true
    </script>
</head>
<body style="margin-right: 6px;background: var(--color-card);">

<div class="opt-bar" style="margin-top: 0;">
    <div></div>
    <span>分类标签列表</span>
</div>
<div class="sort-list" id="sort-list"></div>
<div class="opt-bar" style="margin-top: 20px;">
    <div></div>
    <span>添加分类标签</span>
    <button class="ss-button" id="sort-send" onclick="addSort()">添加</button>
</div>
<div class="option-card opt-card-r" style="margin-top: 10px;">
    <div class="ss-card">
        <div></div>
        <div>
            <a>* 分类名</a>
            <span>分类的名字</span>
        </div>
        <div>
            <label>
                <input type="text" id="sort-title">
            </label>
        </div>
    </div>
    <div class="ss-card">
        <div></div>
        <div>
            <a>* 分类简称</a>
            <span>这个名字将用在分类页面的链接上，只能英文哦。</span>
        </div>
        <div>
            <label>
                <input type="text" id="sort-name">
            </label>
        </div>
    </div>
    <div class="ss-card">
        <div></div>
        <div>
            <a>简介</a>
            <span>主题可以选择把简介显示出来哦</span>
        </div>
        <div>
            <label>
                <textarea id="sort-mark"></textarea>
            </label>
        </div>
    </div>
</div>
</body>
<script src="https://api.stapxs.cn/js/util/jquery-1.12.4.js"></script>
<script src="https://api.stapxs.cn/bootstrap/popper.min.js"></script>
<script src="https://api.stapxs.cn/bootstrap/bootstrap.min.js"></script>
<script src="https://api.stapxs.cn/js/main.js"></script>
<script src="https://api.stapxs.cn/js/auto-theme.js"></script>
<script src="/js/admin/sort.js"></script>
</html>