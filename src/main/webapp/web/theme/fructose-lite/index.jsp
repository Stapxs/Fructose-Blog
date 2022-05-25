<%@ page import="cn.stapxs.blog.model.Config" %>
<%@ page import="cn.stapxs.blog.AppInfo" %>
<%@ page import="cn.stapxs.blog.model.Article" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="cn.stapxs.blog.model.SortInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/05/12
  Time: 下午 12:25
  To change this template use File | Settings | File Templates.
--%>
<%
    Config configInfo = (Config) request.getAttribute("config");
    List<Article> allArticles = (List<Article>) request.getAttribute("articles");
    List<Article> articles = (List<Article>) request.getAttribute("now-page-articles");
    SortInfo[] sorts = (SortInfo[]) request.getAttribute("sort");
    int page_num = (int) request.getAttribute("page");
    int max_page = (int) request.getAttribute("max-page");
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
                <p id="title"><%=configInfo.getFb_name()%></p>
                <span id="ana">这是语录</span>
            </div>
        </div>
    </div>
    <div class="container-lg" style="padding: 0;">
        <div id="pin-pan" class="pin">
            <%
                int count = 0;
                for(Article art : allArticles) {
                    if(count > 2) {
                        break;
                    }
                    String appx = art.getArt_appendix();
                    if(appx != null && appx.contains("\"pin\":true") && !appx.contains("\"note\":true")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        // 去除 html 标签
                        String summary = art.getArt_html();
                        summary = summary.replaceAll("<[^>]+>", "");
                        summary = summary.replaceAll("\\s*", "");
                        summary = summary.substring(0, Math.min(summary.length(), 100));
                        String html = String.format("<div class=\"ss-card\">\n" +
                                "                <div class=\"pin-img\" style=\"background: url(/api/article/img/get/%s/img)\"></div>\n" +
                                "                <div>\n" +
                                "                    <a href=\"/article/%s\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 384 512\"><path d=\"M32 32C32 14.33 46.33 0 64 0H320C337.7 0 352 14.33 352 32C352 49.67 337.7 64 320 64H290.5L301.9 212.2C338.6 232.1 367.5 265.4 381.4 306.9L382.4 309.9C385.6 319.6 383.1 330.4 377.1 338.7C371.9 347.1 362.3 352 352 352H32C21.71 352 12.05 347.1 6.04 338.7C.0259 330.4-1.611 319.6 1.642 309.9L2.644 306.9C16.47 265.4 45.42 232.1 82.14 212.2L93.54 64H64C46.33 64 32 49.67 32 32zM224 384V480C224 497.7 209.7 512 192 512C174.3 512 160 497.7 160 480V384H224z\"/></svg>%s</a>\n" +
                                "                    <div><span>%s</span></div>\n" +
                                "                    <div><img alt=\"头像\" src=\"/api/account/avatar/%s/img\"><a>%s</a></div>\n" +
                                "                </div>\n" +
                                "            </div>",
                                art.getArt_id(), art.getArt_link(), art.getArt_title(), summary, art.getUser_id(), sdf.format(art.getArt_date()));
                        out.print(html);
                        count ++;
                    }
                }
            %>
        </div>
        <div id="main-list" class="art-list">
            <%
                for(Article art : articles){
                    if(art.getArt_statue() == 1) {
                        String appx = art.getArt_appendix();
                        if (appx != null && !appx.contains("\"note\":true")) {
                            // 常规文章
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            // 去除 html 标签
                            String summary = art.getArt_html();
                            summary = summary.replaceAll("<[^>]+>", "");
                            summary = summary.replaceAll("\\s*", "");
                            summary = summary.substring(0, Math.min(summary.length(), 300));
                            // 生成 HTML
                            String html = String.format("<div class=\"art-body\">\n" +
                                            "                <div>\n" +
                                            "                    <div style=\"background: url(/api/article/img/get/%s/img) center;\"></div>\n" +
                                            "                </div>\n" +
                                            "                <div>\n" +
                                            "                    <div><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 88.05 64\"><defs><style>.cls-2A{fill:var(--color-main);}</style></defs><g id=\"P_2\" data-name=\"P 2\"><g id=\"P_2-2\" data-name=\"P 2\"><polygon class=\"cls-2A\" points=\"0 0 88.05 0 42.19 33 88.05 64 0 64 0 0\"/></g></g></svg>\n" +
                                            "                    </div>\n" +
                                            "                    <a href=\"/article/%s\">%s</a>\n" +
                                            "                    <div><span>%s</span></div>\n" +
                                            "                    <div>\n" +
                                            "                        <img alt=\"头像\" src=\"/api/account/avatar/%s/img\">\n" +
                                            "                        <div>\n" +
                                            "                            <a>%s</a>\n" +
                                            "                            <span>%s</span>\n" +
                                            "                        </div>\n" +
                                            "                    </div>\n" +
                                            "                </div>\n" +
                                            "            </div>",
                                    art.getArt_id(), art.getArt_link(), art.getArt_title(), summary, art.getUser_id(), art.getUser_id(), sdf.format(art.getArt_date()));
                            out.print(html);
                        } else if (appx != null && appx.contains("\"note\":true")) {
                            // 短日记
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String html = String.format("<div class=\"say-body\">\n" +
                                            "                <div>\n" +
                                            "                    <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 512 512\"><path d=\"M256 31.1c-141.4 0-255.1 93.12-255.1 208c0 49.62 21.35 94.98 56.97 130.7c-12.5 50.37-54.27 95.27-54.77 95.77c-2.25 2.25-2.875 5.734-1.5 8.734c1.249 3 4.021 4.766 7.271 4.766c66.25 0 115.1-31.76 140.6-51.39c32.63 12.25 69.02 19.39 107.4 19.39c141.4 0 255.1-93.13 255.1-207.1S397.4 31.1 256 31.1zM127.1 271.1c-17.75 0-32-14.25-32-31.1s14.25-32 32-32s32 14.25 32 32S145.7 271.1 127.1 271.1zM256 271.1c-17.75 0-31.1-14.25-31.1-31.1s14.25-32 31.1-32s31.1 14.25 31.1 32S273.8 271.1 256 271.1zM383.1 271.1c-17.75 0-32-14.25-32-31.1s14.25-32 32-32s32 14.25 32 32S401.7 271.1 383.1 271.1z\"/></svg>\n" +
                                            "                </div>\n" +
                                            "                <div>\n" +
                                            "                    <span><span>碎碎念：</span>%s</span>\n" +
                                            "                    <span>%s</span>\n" +
                                            "                    <img alt=\"头像\" src=\"/api/account/avatar/%s/img\">\n" +
                                            "                </div>\n" +
                                            "            </div>",
                                    art.getArt_title(), sdf.format(art.getArt_date()), art.getUser_id());
                            out.print(html);
                        }
                    }
                }
            %>
        </div>
        <%
            if(max_page > 1) {
                String html = String.format("\n" +
                        "        <div class=\"controller\">\n" +
                        "            <button onclick=\"changePage(false)\" style=\"%s\" class=\"ss-button\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 256 512\"><path d=\"M137.4 406.6l-128-127.1C3.125 272.4 0 264.2 0 255.1s3.125-16.38 9.375-22.63l128-127.1c9.156-9.156 22.91-11.9 34.88-6.943S192 115.1 192 128v255.1c0 12.94-7.781 24.62-19.75 29.58S146.5 415.8 137.4 406.6z\"/></svg></button>\n" +
                        "            <div><span>第 %s 页</span></div>\n" +
                        "            <button onclick=\"changePage(true)\" style=\"%s\" class=\"ss-button\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 256 512\"><path d=\"M118.6 105.4l128 127.1C252.9 239.6 256 247.8 256 255.1s-3.125 16.38-9.375 22.63l-128 127.1c-9.156 9.156-22.91 11.9-34.88 6.943S64 396.9 64 383.1V128c0-12.94 7.781-24.62 19.75-29.58S109.5 96.23 118.6 105.4z\"/></svg></button>\n" +
                        "        </div>",
                        (page_num == 1) ? "display: none;" : "", page_num, (page_num == max_page) ? "display: none;" : "");
                out.print(html);
            }
        %>
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
    // 处理用户名
    const list = document.getElementById("main-list").children;
    for(let i = 0; i < list.length; i++) {
        if(list[i].className === "art-body") {
            const nameBody = list[i].children[1].children[list[i].children[1].children.length - 1].children[1].children[0];
            const name = nameBody.innerText;
            fetch("/api/account/base/" + name)
            .then(res => res.json())
            .then(res => {
                if(res.code === 200) {
                    if(res.data.user_nick !== undefined) {
                        nameBody.innerText = res.data.user_nick;
                    } else {
                        nameBody.innerText = res.data.user_name;
                    }
                }
            });
        }
    }
    // 尝试加载头像
    if(getCookie("id")) {
        document.getElementById("avatar-img").src = "/api/account/avatar/" + getCookie("id") + "/img";
    }

    // 加载设置
    const config = '<%=configInfo.getTheme_cfgs()%>';
    if(config !== 'null') {
        const address = JSON.parse(config).ana_address;
        fetch(address)
            .then(res => res.text())
            .then(res => {
                document.getElementById("ana").innerText = res;
            });
        if(JSON.parse(config).close_pin === true) {
            document.getElementById("pin-pan").style.display = "none";
        }
    }

    // 切换页面
    function changePage(next) {
        if(!next) {
            window.location.href = "/page/<%=page_num - 1%>";
        } else {
            window.location.href = "/page/<%=page_num + 1%>";
        }
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