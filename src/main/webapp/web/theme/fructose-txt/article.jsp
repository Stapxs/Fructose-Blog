<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="cn.stapxs.blog.model.Config" %>
<%@ page import="cn.stapxs.blog.model.SortInfo" %>
<%@ page import="cn.stapxs.blog.model.Article" %>
<%@ page import="cn.stapxs.blog.model.user.UserInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/05/24
  Time: 下午 03:02
  To change this template use File | Settings | File Templates.
--%>
<%
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Config configInfo = (Config) request.getAttribute("config");
    SortInfo[] sorts = (SortInfo[]) request.getAttribute("sort");
    Article article = (Article) request.getAttribute("article");
    UserInfo user = (UserInfo) request.getAttribute("user");
%>
<html>
<head>
    <title><%=configInfo.getFb_name()%></title>

    <link rel="stylesheet" href="/theme/<%=configInfo.getCfg_theme()%>/css/index.css">
    <link rel="stylesheet" href="/theme/<%=configInfo.getCfg_theme()%>/css/article.css">
</head>

<body>
<div class="main">
    <div>
        <div>
            <h1><%=configInfo.getFb_name()%></h1>
            <span>这是语录</span>
        </div>
        <label>
            <input placeholder="搜索">
        </label>
    </div>
    <div id="sort-body" class="sort-body">
        <a href="/">首页</a>
        <%
            for (SortInfo sort : sorts) {
                out.print(String.format("<a href=\"/category/%s\">%s</a>", sort.getSort_name().toLowerCase(), sort.getSort_title()));
            }
        %>
    </div>
    <div class="art_main">
        <div class="art_info">
            <div>
                <p><%=article.getArt_title()%></p>
                <span>作者：<%=user.getUser_nick()%></span>
            </div>
            <span><%=sdf.format(article.getArt_date())%></span>
        </div>
        <%=article.getArt_html()%>
    </div>
    <footer>
        <p><%=configInfo.getFb_name()%></p>
        <span>Powered by 果糖博客. Copyright © Stapx Steve [ 林槐 ].</span>
    </footer>
</div>
</body>
</html>