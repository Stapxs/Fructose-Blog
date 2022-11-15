<%@ page import="cn.stapxs.blog.model.Config" %>
<%@ page import="cn.stapxs.blog.model.SortInfo" %>
<%@ page import="cn.stapxs.blog.model.Article" %>
<%@ page import="java.util.List" %>
<%@ page import="cn.stapxs.blog.AppInfo" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/05/25
  Time: 下午 12:23
  To change this template use File | Settings | File Templates.
--%>
<%
    Config configInfo = (Config) request.getAttribute("config");
    SortInfo[] sorts = (SortInfo[]) request.getAttribute("sort");
    List<Article> articles = (List<Article>) request.getAttribute("articles");
    SortInfo nowSort = (SortInfo) request.getAttribute("now-sort");
%>
<!DOCTYPE html>
<html prefix="og: https://ogp.me/ns#">
<head>
    <title><%=configInfo.getFb_name()%></title>

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
    <link rel="stylesheet" href="/theme/<%=configInfo.getCfg_theme()%>/css/index.css">

    <style>
        .pin {
            flex-direction: column;
        }
        .pin > div {
            margin: 10px 0;
        }
    </style>

    <script>
        is_auto_dark = true
    </script>
</head>

<body>
<nav class="container-lg navbar navbar-expand-lg navbar-dark fixed-top nav-bar">
    <div class="container-lg" style="padding-left: 30px;">
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
</nav>

<div class="body">
    <div class="top-bar">
        <div></div>
        <div>
            <div class="top-bar-title">
                <p id="title"><%=nowSort.getSort_title()%></p>
                <span id="ana"><%=nowSort.getSort_mark()%></span>
                <span>本分类共有：<%=articles.size()%> 篇文章</span>
            </div>
        </div>
    </div>
    <div CLASS="container-lg">
        <div class="pin">
            <%
                for(Article art : articles) {
                    String appx = art.getArt_appendix();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // 去除 html 标签
                    String summary = art.getArt_html();
                    summary = summary.replaceAll("<[^>]+>", "");
                    summary = summary.replaceAll("\\s*", "");
                    summary = summary.substring(0, Math.min(summary.length(), 300));
                    String html = String.format("<div class=\"ss-card\">\n" +
                                    "                <div class=\"pin-img\" style=\"background: url(/api/article/img/get/%s/img)\"></div>\n" +
                                    "                <div>\n" +
                                    "                    <a href=\"/article/%s\">%s</a>\n" +
                                    "                    <div><span>%s</span></div>\n" +
                                    "                    <div><img alt=\"头像\" src=\"/api/account/avatar/%s/img\"><a>%s</a></div>\n" +
                                    "                </div>\n" +
                                    "            </div>",
                            art.getArt_id(), art.getArt_link(), art.getArt_title(), summary, art.getUser_id(), sdf.format(art.getArt_date()));
                    out.print(html);
                }
            %>
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

<script>
    // 尝试加载头像
    if(getCookie("id")) {
        document.getElementById("avatar-img").src = "/api/account/avatar/" + getCookie("id") + "/img";
    }

    // 获取 Cookie 值
    function getCookie(name) {
        let cookieValue = null;
        if (document.cookie && document.cookie !== '') {
            const cookies = document.cookie.split(';');
            for (let i = 0; i < cookies.length; i++) {
                const cookie = jQuery.trim(cookies[i]);
                if (cookie.substring(0, name.length + 1) === (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
</script>

<script src="https://api.stapxs.cn/bootstrap/popper.min.js"></script>
<script src="https://api.stapxs.cn/bootstrap/bootstrap.min.js"></script>
<script src="https://api.stapxs.cn/js/main.js"></script>
<script src="https://api.stapxs.cn/js/util/spacingjs.js"></script>
<script src="https://api.stapxs.cn/js/auto-theme.js"></script>
</html>