<%@ page import="cn.stapxs.blog.AppInfo" %>
<%@ page import="java.util.Calendar" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/05/06
  Time: 下午 02:14
  To change this template use File | Settings | File Templates.
--%>
<%
    int userType = (int) request.getAttribute("userType");
    String siteName = (String) request.getAttribute("siteName");
%>
<!DOCTYPE html>
<html>
<head>
    <title>设置 - <%=siteName%></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <meta name="theme-color" content="#313D43">

    <meta property="og:locale:alternate" content="zh_CN">
    <meta property="og:site_name" content="<%=siteName%>">
    <meta property="og:type" content="blog">

    <link rel="stylesheet" href="https://api.stapxs.cn/css/color-dark.css">

    <link rel="stylesheet" href="https://api.stapxs.cn/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/css/style.css">
    <link rel="stylesheet" href="css/admin/index.css">

    <script>
        is_auto_dark = true
    </script>
</head>

<body style="background: var(--color-bg);">
<div class="container-lg" style="display: flex;flex-direction: column;height: calc(100vh - 150px);margin-bottom: 20px;margin-top: 20px;">
    <div style="flex: 1;display: flex;">
        <%-- Code Here …… --%>
        <div class="main-board">
            <div class="left-list ss-card" id="left-list">
                <div>
                    <span><%=siteName%></span>
                    <svg onclick="changeLeftList()" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M0 96C0 78.33 14.33 64 32 64H416C433.7 64 448 78.33 448 96C448 113.7 433.7 128 416 128H32C14.33 128 0 113.7 0 96zM64 256C64 238.3 78.33 224 96 224H480C497.7 224 512 238.3 512 256C512 273.7 497.7 288 480 288H96C78.33 288 64 273.7 64 256zM416 448H32C14.33 448 0 433.7 0 416C0 398.3 14.33 384 32 384H416C433.7 384 448 398.3 448 416C448 433.7 433.7 448 416 448z"/></svg>
                </div>
                <div>
                    <button onclick="foldChange(this)" class="btn btn-primary lis-button" type="button" data-toggle="collapse" data-target="#collapse-1" aria-controls="collapse-1">
                        <header><div style="transform: scaleY(0)"></div>控制台</header>
                        <svg style="transform: rotate(90deg);" aria-hidden="true" focusable="false" data-prefix="fas" data-icon="angle-right" class="svg-inline--fa fa-angle-right fa-w-8" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 512"><path fill="currentColor" d="M224.3 273l-136 136c-9.4 9.4-24.6 9.4-33.9 0l-22.6-22.6c-9.4-9.4-9.4-24.6 0-33.9l96.4-96.4-96.4-96.4c-9.4-9.4-9.4-24.6 0-33.9L54.3 103c9.4-9.4 24.6-9.4 33.9 0l136 136c9.5 9.4 9.5 24.6.1 34z"></path></svg>
                    </button>
                    <div class="collapse" id="collapse-1">
                        <div class="lis-body">
                            <div data-page="dashboard" onclick="addTab(this, true)">
                                <div></div>
                                <span>统计信息</span>
                                <div style="flex: 1"></div>
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 512"><path d="M118.6 105.4l128 127.1C252.9 239.6 256 247.8 256 255.1s-3.125 16.38-9.375 22.63l-128 127.1c-9.156 9.156-22.91 11.9-34.88 6.943S64 396.9 64 383.1V128c0-12.94 7.781-24.62 19.75-29.58S109.5 96.23 118.6 105.4z"/></svg>
                            </div>
                            <div data-page="user" onclick="addTab(this)">
                                <div></div>
                                <span>个人设置</span>
                                <div style="flex: 1"></div>
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 512"><path d="M118.6 105.4l128 127.1C252.9 239.6 256 247.8 256 255.1s-3.125 16.38-9.375 22.63l-128 127.1c-9.156 9.156-22.91 11.9-34.88 6.943S64 396.9 64 383.1V128c0-12.94 7.781-24.62 19.75-29.58S109.5 96.23 118.6 105.4z"/></svg>
                            </div>
                            <div data-page="theme" onclick="addTab(this)">
                                <div></div>
                                <span>主题设置</span>
                                <div style="flex: 1"></div>
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 512"><path d="M118.6 105.4l128 127.1C252.9 239.6 256 247.8 256 255.1s-3.125 16.38-9.375 22.63l-128 127.1c-9.156 9.156-22.91 11.9-34.88 6.943S64 396.9 64 383.1V128c0-12.94 7.781-24.62 19.75-29.58S109.5 96.23 118.6 105.4z"/></svg>
                            </div>
                            <div>
                                <div></div>
                                <span>站点备份</span>
                                <div style="flex: 1"></div>
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 512"><path d="M118.6 105.4l128 127.1C252.9 239.6 256 247.8 256 255.1s-3.125 16.38-9.375 22.63l-128 127.1c-9.156 9.156-22.91 11.9-34.88 6.943S64 396.9 64 383.1V128c0-12.94 7.781-24.62 19.75-29.58S109.5 96.23 118.6 105.4z"/></svg>
                            </div>
                        </div>
                    </div>
                    <button onclick="foldChange(this)" class="btn btn-primary lis-button" type="button" data-toggle="collapse" data-target="#collapse-2" aria-controls="collapse-2">
                        <header><div style="transform: scaleY(0)"></div>文章</header>
                        <svg style="transform: rotate(90deg);" aria-hidden="true" focusable="false" data-prefix="fas" data-icon="angle-right" class="svg-inline--fa fa-angle-right fa-w-8" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 512"><path fill="currentColor" d="M224.3 273l-136 136c-9.4 9.4-24.6 9.4-33.9 0l-22.6-22.6c-9.4-9.4-9.4-24.6 0-33.9l96.4-96.4-96.4-96.4c-9.4-9.4-9.4-24.6 0-33.9L54.3 103c9.4-9.4 24.6-9.4 33.9 0l136 136c9.5 9.4 9.5 24.6.1 34z"></path></svg>
                    </button>
                    <div class="collapse" id="collapse-2">
                        <div class="lis-body">
                            <div data-page="article" onclick="addTab(this, true)">
                                <div></div>
                                <span>新增文章</span>
                                <div style="flex: 1"></div>
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 512"><path d="M118.6 105.4l128 127.1C252.9 239.6 256 247.8 256 255.1s-3.125 16.38-9.375 22.63l-128 127.1c-9.156 9.156-22.91 11.9-34.88 6.943S64 396.9 64 383.1V128c0-12.94 7.781-24.62 19.75-29.58S109.5 96.23 118.6 105.4z"/></svg>
                            </div>
                            <div data-page="new-page" onclick="addTab(this)">
                                <div></div>
                                <span>新增页面</span>
                                <div style="flex: 1"></div>
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 512"><path d="M118.6 105.4l128 127.1C252.9 239.6 256 247.8 256 255.1s-3.125 16.38-9.375 22.63l-128 127.1c-9.156 9.156-22.91 11.9-34.88 6.943S64 396.9 64 383.1V128c0-12.94 7.781-24.62 19.75-29.58S109.5 96.23 118.6 105.4z"/></svg>
                            </div>
                        </div>
                    </div>
                    <button onclick="foldChange(this)" class="btn btn-primary lis-button" type="button" data-toggle="collapse" data-target="#collapse-3" aria-controls="collapse-3">
                        <header><div style="transform: scaleY(0)"></div>管理员</header>
                        <svg style="transform: rotate(90deg);" aria-hidden="true" focusable="false" data-prefix="fas" data-icon="angle-right" class="svg-inline--fa fa-angle-right fa-w-8" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 512"><path fill="currentColor" d="M224.3 273l-136 136c-9.4 9.4-24.6 9.4-33.9 0l-22.6-22.6c-9.4-9.4-9.4-24.6 0-33.9l96.4-96.4-96.4-96.4c-9.4-9.4-9.4-24.6 0-33.9L54.3 103c9.4-9.4 24.6-9.4 33.9 0l136 136c9.5 9.4 9.5 24.6.1 34z"></path></svg>
                    </button>
                    <div class="collapse" id="collapse-3">
                        <div class="lis-body">
                            <div data-page="article_list" onclick="addTab(this)">
                                <div></div>
                                <span>管理文章</span>
                                <div style="flex: 1"></div>
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 512"><path d="M118.6 105.4l128 127.1C252.9 239.6 256 247.8 256 255.1s-3.125 16.38-9.375 22.63l-128 127.1c-9.156 9.156-22.91 11.9-34.88 6.943S64 396.9 64 383.1V128c0-12.94 7.781-24.62 19.75-29.58S109.5 96.23 118.6 105.4z"/></svg>
                            </div>
                            <div data-page="sort" onclick="addTab(this)">
                                <div></div>
                                <span>管理分类</span>
                                <div style="flex: 1"></div>
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 512"><path d="M118.6 105.4l128 127.1C252.9 239.6 256 247.8 256 255.1s-3.125 16.38-9.375 22.63l-128 127.1c-9.156 9.156-22.91 11.9-34.88 6.943S64 396.9 64 383.1V128c0-12.94 7.781-24.62 19.75-29.58S109.5 96.23 118.6 105.4z"/></svg>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="main-iframe ss-card">
                <ul class="nav nav-tabs show-tab" id="myTab" role="tablist">
                    <li class="nav-item" id="left-list-button" style="display: none;margin-right: 10px !important;">
                        <a onclick="changeLeftList()" class="nav-link" style="padding: 5px 10px;background: var(--color-main);cursor: pointer;">
                            <svg style="height: 1rem;fill: var(--color-font-r);" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M0 96C0 78.33 14.33 64 32 64H416C433.7 64 448 78.33 448 96C448 113.7 433.7 128 416 128H32C14.33 128 0 113.7 0 96zM64 256C64 238.3 78.33 224 96 224H480C497.7 224 512 238.3 512 256C512 273.7 497.7 288 480 288H96C78.33 288 64 273.7 64 256zM416 448H32C14.33 448 0 433.7 0 416C0 398.3 14.33 384 32 384H416C433.7 384 448 398.3 448 416C448 433.7 433.7 448 416 448z"/></svg>
                        </a>
                    </li>
                    <li class="nav-item" id="tabp-welcome">
                        <a class="nav-link active" id="welcome-tab" data-toggle="tab" href="#welcome" role="tab" aria-controls="welcome" aria-selected="true">
                            欢迎使用
                        </a>
                    </li>
                </ul>
                <div class="tab-content tab-body" id="myTabContent">
                    <div class="tab-pane fade show active" id="welcome" role="tabpanel" aria-labelledby="welcome-tab">
                        <div class="ss-card welcome-top">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M497.5 91.1C469.6 33.13 411.8 0 352.4 0c-27.88 0-56.14 7.25-81.77 22.62L243.1 38.1C227.9 48.12 223 67.75 232.1 82.87l32.76 54.87c8.522 14.2 27.59 20.6 43.88 11.06l27.51-16.37c5.125-3.125 10.95-4.439 16.58-4.439c10.88 0 21.35 5.625 27.35 15.62c9 15.12 3.917 34.59-11.08 43.71L15.6 397.6c-15.25 9.125-20.13 28.62-11 43.87l32.76 54.87c8.522 14.2 27.59 20.66 43.88 11.12l347.4-206.5C500.2 258.1 533.2 167.5 497.5 91.1zM319.7 104.1L317.2 106.5l-20.5-61.5c9.75-4.75 19.88-8.125 30.38-10.25l20.63 61.87C337.8 97.37 328.2 99.87 319.7 104.1zM145.8 431.7l-60.5-38.5l30.88-18.25l60.5 38.5L145.8 431.7zM253.3 367.9l-60.5-38.5l30.88-18.25l60.5 38.5L253.3 367.9zM364.2 301.1L303.7 263.5l30.88-18.25l60.5 38.5L364.2 301.1zM384.7 104.7l46-45.1c8.375 6.5 16 13.1 22.5 22.5l-45.63 45.81C401.9 117.8 393.9 110.1 384.7 104.7zM466.7 212.5l-59.5-19.75c3.25-5.375 5.875-10.1 7.5-17.12c1-4.5 1.625-9.125 1.75-13.62l60.38 20.12C474.7 192.5 471.4 202.7 466.7 212.5z"/></svg>
                            <div>
                                <span>欢迎使用果糖博客管理后端</span>
                                <span>当前版本：<%=AppInfo.APP_TYPE%> <%=AppInfo.APP_VERSION%></span>
                            </div>
                        </div>
                        <div>

                        </div>
                    </div>
                    <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">baba</div>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="container-lg">
    <div class="ss-card">
        <header>
            果糖博客
            <span>
                <%=AppInfo.APP_TYPE%> - <%=AppInfo.APP_VERSION%>
            </span>
        </header>
        <span>Copyright © 2022 - <%=Calendar.getInstance().get(Calendar.YEAR)%> Stapx Steve [ 林槐 ]</span><br>
        <div style="float: right;margin-top: -44px;">
            <svg style="width: 50px;" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 274 259"><g id="圖層_2" data-name="圖層 2"><g id="本体"><rect class="cls-1" x="19" y="167" width="28" height="28"/><rect class="cls-1" x="62" y="181" width="14" height="14"/><rect class="cls-1" x="89" y="163" width="14" height="14"/><rect class="cls-1" x="62" y="63" width="14" height="14"/><rect class="cls-1" x="82" y="85" width="14" height="14"/><rect class="cls-1" x="114" y="66" width="28" height="28"/><polygon class="cls-1" points="112.54 153.5 33.5 153.5 33.5 106.5 155 106.5 155.5 106.5 159.37 106.5 159.46 207.5 159.5 254.36 159.5 254.46 144.5 254.47 144.5 254.37 144.5 238.5 116.5 238.5 116.5 254.4 116.5 254.5 112.63 254.5 112.63 254.4 3.54 254.5 3.5 207.64 112.59 207.54 112.54 153.5"/><polygon class="cls-1" points="183.51 114.5 198.5 114.5 198.5 130.5 198.5 131.49 269.5 131.42 269.48 103.5 219.5 103.55 219.5 85.47 269.5 85.42 269.5 84.5 269.5 36.42 269.5 20.5 269.48 20.5 251.5 20.5 251.5 5.52 251.5 5.5 232.5 5.5 232.5 5.53 183.5 5.58 183.53 36.5 232.5 36.45 232.5 54.53 219.5 54.55 219.5 54.5 182.5 54.5 182.5 114.5 183.51 114.5"/><rect class="cls-1" x="242" y="153" width="28" height="28"/><rect class="cls-1" x="183" y="240" width="14" height="14"/><rect class="cls-1" x="204" y="166" width="26" height="14"/><rect class="cls-1" x="183" y="190" width="14" height="41"/><polygon class="cls-1" points="269.5 189.5 269.5 230.62 245.62 254.5 203.5 254.5 203.5 189.5 269.5 189.5"/><rect class="cls-1" width="8" height="8"/><rect class="cls-1" x="266" y="251" width="8" height="8"/><polygon class="cls-1" points="3.5 33.16 30.77 5.5 50.5 5.5 145.5 5.5 145.5 19.5 159.5 19.5 159.5 52.5 50.5 52.5 50.5 153.5 3.5 153.5 3.5 52.5 3.5 33.16"/></g></g></svg>        </div>
    </div>
    <div>
        <span>
        </span>
    </div>
</footer>
</body>
<script src="https://api.stapxs.cn/js/util/jquery-1.12.4.js"></script>
<script src="https://api.stapxs.cn/bootstrap/popper.min.js"></script>
<script src="https://api.stapxs.cn/bootstrap/bootstrap.min.js"></script>
<script src="https://api.stapxs.cn/js/main.js"></script>
<script src="https://api.stapxs.cn/js/auto-theme.js"></script>
<script src="https://api.stapxs.cn/js/util/spacingjs.js"></script>
<script src="js/admin/index.js"></script>
</html>
