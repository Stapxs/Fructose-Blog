
<%@ page import="cn.stapxs.blog.model.Config" %>
<%@ page import="cn.stapxs.blog.AppInfo" %>
<%@ page import="cn.stapxs.blog.model.Article" %>
<%@ page import="cn.stapxs.blog.model.SortInfo" %>
<%@ page import="cn.stapxs.blog.model.user.UserInfo" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/05/14
  Time: 下午 03:18
  To change this template use File | Settings | File Templates.
--%>
<%
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Config configInfo = (Config) request.getAttribute("config");
    SortInfo[] sorts = (SortInfo[]) request.getAttribute("sort");
    Article article = (Article) request.getAttribute("article");
    UserInfo user = (UserInfo) request.getAttribute("user");
%>
<!DOCTYPE html>
<html prefix="og: https://ogp.me/ns#">
<head>
    <title><%=article.getArt_title()%> - <%=configInfo.getFb_name()%></title>

    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <meta name="theme-color" content="#313D43">

    <meta property="og:locale:alternate" content="zh_CN">
    <meta property="og:site_name" content="<%=configInfo.getFb_name()%>">
    <meta property="og:type" content="blog">

    <link rel="stylesheet" href="https://api.stapxs.cn/css/color-dark.css">

    <link rel="stylesheet" href="https://api.stapxs.cn/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/css/style.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/font/fira_code.css">

    <link rel="stylesheet" href="/editor/css/editormd.min.css"/>
    <link rel="stylesheet" href="/css/editer.css">

    <link rel="stylesheet" href="/theme/<%=configInfo.getCfg_theme()%>/css/prism-dark.css">
    <link rel="stylesheet" href="/theme/<%=configInfo.getCfg_theme()%>/css/article.css">

    <link rel="stylesheet" href="/theme/<%=configInfo.getCfg_theme()%>/css/katex.min.css">
    <script defer src="/theme/<%=configInfo.getCfg_theme()%>/js/katex.min.js"></script>
    <script defer src="/theme/<%=configInfo.getCfg_theme()%>/js/auto-render.min.js"
            onload="renderMathInElement(document.getElementById('article-main'),
            {delimiters:[{left: '$$', right: '$$', display: true}, {left: '$', right: '$', display: false}]});"></script>

    <script>
        is_auto_dark = true
    </script>
</head>

<body class="line-numbers language-none" id="body">
<nav id="nav" class="container-lg navbar navbar-expand-lg navbar-dark fixed-top nav-bar">
    <div class="container-lg" id="main-bar" style="transition: transform .3s; padding-left: 20px;">
        <a class="navbar-brand" href="/">
            <%=configInfo.getFb_name()%>
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
            <svg style="background-image: none;color: var(--color-font);" class="navbar-toggler-icon" xmlns='http://www.w3.org/2000/svg' width='30' height='30' viewBox='0 0 30 30'><path stroke='currentColor' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'></path></svg>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav mr-auto">
                <a class="nav-item nav-link active" href="/">主页</a>
                <%
                    for(SortInfo sort : sorts) {
                        out.print(String.format("<a class=\"nav-item nav-link active\" href=\"/category/%s\">%s</a>", sort.getSort_name().toLowerCase(), sort.getSort_title()));
                    }
                %>
            </div>
            <form class="form-inline my-2 my-lg-0">
                <input class="form-control mr-sm-2 bar-search" type="search" placeholder="搜索" aria-label="搜索">
                <div id="user-avatar" class="avatar" title="登录" onclick="window.location.href='/login'">
                    <img id="avatar-img" alt="头像">
                    <span id="user-name" class="user-name">登录</span>
                </div>
            </form>
        </div>
    </div>
    <div class="container-lg article-controller" id="article-bar">
        <a class="navbar-brand" href="#body">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M480 32H128C110.3 32 96 46.33 96 64v336C96 408.8 88.84 416 80 416S64 408.8 64 400V96H32C14.33 96 0 110.3 0 128v288c0 35.35 28.65 64 64 64h384c35.35 0 64-28.65 64-64V64C512 46.33 497.7 32 480 32zM272 416h-96C167.2 416 160 408.8 160 400C160 391.2 167.2 384 176 384h96c8.836 0 16 7.162 16 16C288 408.8 280.8 416 272 416zM272 320h-96C167.2 320 160 312.8 160 304C160 295.2 167.2 288 176 288h96C280.8 288 288 295.2 288 304C288 312.8 280.8 320 272 320zM432 416h-96c-8.836 0-16-7.164-16-16c0-8.838 7.164-16 16-16h96c8.836 0 16 7.162 16 16C448 408.8 440.8 416 432 416zM432 320h-96C327.2 320 320 312.8 320 304C320 295.2 327.2 288 336 288h96C440.8 288 448 295.2 448 304C448 312.8 440.8 320 432 320zM448 208C448 216.8 440.8 224 432 224h-256C167.2 224 160 216.8 160 208v-96C160 103.2 167.2 96 176 96h256C440.8 96 448 103.2 448 112V208z"/></svg>
            <%=article.getArt_title()%>
        </a>
        <div>
            <svg onclick="changeContent()" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M0 96C0 78.33 14.33 64 32 64H416C433.7 64 448 78.33 448 96C448 113.7 433.7 128 416 128H32C14.33 128 0 113.7 0 96zM64 256C64 238.3 78.33 224 96 224H480C497.7 224 512 238.3 512 256C512 273.7 497.7 288 480 288H96C78.33 288 64 273.7 64 256zM416 448H32C14.33 448 0 433.7 0 416C0 398.3 14.33 384 32 384H416C433.7 384 448 398.3 448 416C448 433.7 433.7 448 416 448z"/></svg>
            <svg onclick="document.documentElement.scrollTop = 0" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512"><path d="M54.63 246.6L192 109.3l137.4 137.4C335.6 252.9 343.8 256 352 256s16.38-3.125 22.62-9.375c12.5-12.5 12.5-32.75 0-45.25l-160-160c-12.5-12.5-32.75-12.5-45.25 0l-160 160c-12.5 12.5-12.5 32.75 0 45.25S42.13 259.1 54.63 246.6zM214.6 233.4c-12.5-12.5-32.75-12.5-45.25 0l-160 160c-12.5 12.5-12.5 32.75 0 45.25s32.75 12.5 45.25 0L192 301.3l137.4 137.4C335.6 444.9 343.8 448 352 448s16.38-3.125 22.62-9.375c12.5-12.5 12.5-32.75 0-45.25L214.6 233.4z"/></svg>
        </div>
    </div>
    <div class="nav-controller" onmouseover="barController()">
        <div id="nav-controller"></div>
    </div>
</nav>

<div class="body">
    <div class="top-bar">
        <div></div>
        <div>
            <div class="top-bar-title">
                <p id="title"></p>
                <span id="ana"></span>
            </div>
        </div>
    </div>
    <div class="container-lg" style="z-index: 1;position: relative;">
        <div class="main-card">
            <div class="article-info">
                <div>
                    <p>
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M480 32H128C110.3 32 96 46.33 96 64v336C96 408.8 88.84 416 80 416S64 408.8 64 400V96H32C14.33 96 0 110.3 0 128v288c0 35.35 28.65 64 64 64h384c35.35 0 64-28.65 64-64V64C512 46.33 497.7 32 480 32zM272 416h-96C167.2 416 160 408.8 160 400C160 391.2 167.2 384 176 384h96c8.836 0 16 7.162 16 16C288 408.8 280.8 416 272 416zM272 320h-96C167.2 320 160 312.8 160 304C160 295.2 167.2 288 176 288h96C280.8 288 288 295.2 288 304C288 312.8 280.8 320 272 320zM432 416h-96c-8.836 0-16-7.164-16-16c0-8.838 7.164-16 16-16h96c8.836 0 16 7.162 16 16C448 408.8 440.8 416 432 416zM432 320h-96C327.2 320 320 312.8 320 304C320 295.2 327.2 288 336 288h96C440.8 288 448 295.2 448 304C448 312.8 440.8 320 432 320zM448 208C448 216.8 440.8 224 432 224h-256C167.2 224 160 216.8 160 208v-96C160 103.2 167.2 96 176 96h256C440.8 96 448 103.2 448 112V208z"/></svg>
                        <%=article.getArt_title()%>
                    </p>
                    <div>
                        <div>
                            <a><%=user.getUser_nick()%></a>
                            <span><%=sdf.format(article.getArt_date())%></span>
                        </div>
                        <img alt="头像" src="/api/account/avatar/<%=user.getUser_id()%>/img">
                    </div>
                </div>
            </div>
            <div class="main">
                <div class="article-main" id="article-main">
                    <%=article.getArt_html()%>
                </div>
                <div id="main-right">
                    <div class="content" id="content">
                        <div>目录</div>
                    </div>
                </div>
            </div>
            <div class="end-info" id="end-info">
                <span>作者：<%=user.getUser_nick()%></span>
                <span id="link_address">链接：</span>
                <span>版权：本博客所有文章除特别声明外，均采用 CC BY-NC-SA 4.0 许可协议。转载请注明出处！</span>
            </div>
        </div>
        <div class="more-card">
            <div id="more-sort-card">
                <span><%
                    String nameSort = article.getArt_sort().split(",")[0];
                    if (nameSort.equals("")) {
                        out.print("推荐");
                    }
                    out.print(nameSort);
                %></span>
                <span>∞</span>
                <a id="add-1" data-sort="<%=article.getArt_sort().split(",")[0]%>"></a>
                <hr>
                <a id="add-2"></a>
                <hr>
                <a id="add-3"></a>
                <%
                    if (!nameSort.equals("")) {
                        out.print("<div></div><a href=\"/category/" + article.getArt_sort().split(",")[0].toLowerCase() + "\">查看更多文章 -></a>");
                    }
                %>
            </div>
        </div>
        <div class="comment-send-card">
            <div>
                <span>登录身份：</span>
                <a>Stapx_Steve</a>
                <span>，</span>
                <a>退出</a>
            </div>
            <div>
                <img src="/api/account/avatar/0/img">
                <div>
                    <label id="editor">
                        <textarea></textarea>
                    </label>
                    <div>
                        <button class="ss-button" id="comment-button">发表评论</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<footer class="container-lg">
    <div class="ss-card">
        <header><%=configInfo.getFb_name()%>
            <span>
            <%=AppInfo.APP_TYPE%> - <%=AppInfo.APP_VERSION%>
        </span>
        </header>
        <span>Copyright © 2020 - 2021 Stapx Steve [ 林槐 ]</span><br>
        <div style="float: right;margin-top: -44px;">
            <svg style="width: 50px;" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 274 259"><g id="圖層_2" data-name="圖層 2"><g id="本体"><rect class="cls-1" x="19" y="167" width="28" height="28"/><rect class="cls-1" x="62" y="181" width="14" height="14"/><rect class="cls-1" x="89" y="163" width="14" height="14"/><rect class="cls-1" x="62" y="63" width="14" height="14"/><rect class="cls-1" x="82" y="85" width="14" height="14"/><rect class="cls-1" x="114" y="66" width="28" height="28"/><polygon class="cls-1" points="112.54 153.5 33.5 153.5 33.5 106.5 155 106.5 155.5 106.5 159.37 106.5 159.46 207.5 159.5 254.36 159.5 254.46 144.5 254.47 144.5 254.37 144.5 238.5 116.5 238.5 116.5 254.4 116.5 254.5 112.63 254.5 112.63 254.4 3.54 254.5 3.5 207.64 112.59 207.54 112.54 153.5"/><polygon class="cls-1" points="183.51 114.5 198.5 114.5 198.5 130.5 198.5 131.49 269.5 131.42 269.48 103.5 219.5 103.55 219.5 85.47 269.5 85.42 269.5 84.5 269.5 36.42 269.5 20.5 269.48 20.5 251.5 20.5 251.5 5.52 251.5 5.5 232.5 5.5 232.5 5.53 183.5 5.58 183.53 36.5 232.5 36.45 232.5 54.53 219.5 54.55 219.5 54.5 182.5 54.5 182.5 114.5 183.51 114.5"/><rect class="cls-1" x="242" y="153" width="28" height="28"/><rect class="cls-1" x="183" y="240" width="14" height="14"/><rect class="cls-1" x="204" y="166" width="26" height="14"/><rect class="cls-1" x="183" y="190" width="14" height="41"/><polygon class="cls-1" points="269.5 189.5 269.5 230.62 245.62 254.5 203.5 254.5 203.5 189.5 269.5 189.5"/><rect class="cls-1" width="8" height="8"/><rect class="cls-1" x="266" y="251" width="8" height="8"/><polygon class="cls-1" points="3.5 33.16 30.77 5.5 50.5 5.5 145.5 5.5 145.5 19.5 159.5 19.5 159.5 52.5 50.5 52.5 50.5 153.5 3.5 153.5 3.5 52.5 3.5 33.16"/></g></g></svg>        </div>
    </div>
    <div>
    <span>
        <a rel="nofollow" href="http://beian.miit.gov.cn" target="_blank">苏ICP备 20015498号-2 | </a><a href="https://icp.gov.moe" target="_blank">萌ICP备 </a><a href="https://icp.gov.moe/?keyword=20202320" target="_blank"> 20202320号</a>
    </span>
    </div>
</footer>
</body>

<script src="https://api.stapxs.cn/js/util/jquery-1.12.4.js"></script>
<script src="https://api.stapxs.cn/bootstrap/popper.min.js"></script>
<script src="https://api.stapxs.cn/bootstrap/bootstrap.min.js"></script>
<script src="https://api.stapxs.cn/js/main.js"></script>
<script src="https://api.stapxs.cn/js/util/spacingjs.js"></script>
<script src="https://api.stapxs.cn/js/auto-theme.js"></script>

<script src="/editor/editormd.js"></script>

<script src="/theme/<%=configInfo.getCfg_theme()%>/js/article.js"></script>
<script src="/theme/<%=configInfo.getCfg_theme()%>/js/prism.js"></script>
</html>
