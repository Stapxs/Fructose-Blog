<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/05/08
  Time: 下午 07:17
  To change this template use File | Settings | File Templates.
--%>
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
    <link rel="stylesheet" href="/editor/css/editormd.min.css"/>
    <link rel="stylesheet" href="/css/admin/article.css">
    <link rel="stylesheet" href="/css/admin/main.css">


    <script>
        is_auto_dark = true
    </script>
</head>
<body style="margin-right: 6px;background: var(--color-card);">
<div class="main">
    <div class="opt-bar" style="margin-top: 0;">
        <div></div>
        <span style="flex: none">标题：</span>
        <label>
            <input type="text" id="title" placeholder="请输入标题">
        </label>
    </div>
    <div class="main-body">
        <label id="editor">
            <textarea style="display:none;margin-bottom: 0;"></textarea>
        </label>
    </div>
    <div class="option">
        <div>
            <div class="opt-bar" style="margin-top: 0;">
                <div></div>
                <span>附加字段</span>
            </div>
            <div class="add-opt">

            </div>
            <div class="opt-bar">
                <div></div>
                <span>操作</span>
            </div>
            <div class="end-action">
                <button class="ss-button" data-aid="" onclick="uploadArticle(this)" style="background: var(--color-main);color: var(--color-font-r)">发布文章</button>
                <button class="ss-button">保存草稿</button>
                <button class="ss-button">预览文章</button>
            </div>
        </div>
        <div>
            <div>
                <div class="opt-bar-small" style="margin-top: 0;">
                    <div>
                        <div></div>
                        <span>发送日期</span>
                    </div>
                    <label style="width: 100%;padding-left: 10px;">
                        <input type="text" id="send_date" placeholder="请输入发送日期" value="<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())%>">
                    </label>
                </div>
                <div class="opt-bar-small">
                    <div>
                        <div></div>
                        <span>文章链接名</span>
                    </div>
                    <label style="width: 100%;padding-left: 10px;">
                        <input type="text" placeholder="留空自动生成" id="link">
                    </label>
                </div>
                <div class="opt-bar-small" id="sort-body">
                    <div>
                        <div></div>
                        <span>分类</span>
                    </div>
                    <div style="width: 100%;padding-left: 10px;" id="sort-list"></div>
                </div>
                <div class="opt-bar-small">
                    <div>
                        <div></div>
                        <span>标签</span>
                    </div>
                    <div style="width: 100%;padding-left: 10px;">
                        <div id="tag-list"></div>
                        <form onsubmit="return addTag(this)">
                            <label id="tag-input" class="tag-input">
                                <input type="text" placeholder="回车添加">
                            </label>
                        </form>
                    </div>
                </div>
                <div class="opt-bar-small">
                    <div>
                        <div></div>
                        <span>公开度</span>
                    </div>
                    <label style="width: 100%;padding-left: 10px;">
                        <select id="public" style="color: var(--color-font-1);width: 100%;background: var(--color-card-2);border: 0;min-height: 35px;border-radius: 7px;padding: 0 10px;">
                            <option value="1">公开</option>
                            <option value="0">私密</option>
                            <option value="2">密码保护</option>
                            <option value="-1">待审核</option>
                        </select>
                    </label>
                </div>
                <div class="opt-bar-small">
                    <div>
                        <div></div>
                        <span>引用通告（逗号隔开）</span>
                    </div>
                    <label style="width: 100%;padding-left: 10px;">
                        <textarea id="use"></textarea>
                    </label>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script src="https://api.stapxs.cn/js/util/jquery-1.12.4.js"></script>
<script src="https://api.stapxs.cn/bootstrap/popper.min.js"></script>
<script src="https://api.stapxs.cn/bootstrap/bootstrap.min.js"></script>
<script src="https://api.stapxs.cn/js/main.js"></script>
<script src="https://api.stapxs.cn/js/auto-theme.js"></script>
<script src="/editor/editormd.min.js"></script>
<script src="/js/admin/article.js"></script>
</html>
