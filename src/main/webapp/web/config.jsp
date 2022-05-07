<%@ page import="cn.stapxs.blog.AppInfo" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="cn.stapxs.blog.config.JdbcConfig" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/05/01
  Time: 下午 04:19
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://api.stapxs.cn/css/color-dark.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/css/prism-dark.css">

    <link rel="stylesheet" href="https://api.stapxs.cn/font/fira_code.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/css/style.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/css/index.css">
    <link rel="stylesheet" href="/css/config.css">
    <title>初始化配置 - 果糖博客</title>

    <script src="/js/config.js"></script>
    <script>
        is_auto_dark = true
        window.siteConfig = {}
        <%
            String token = (String) request.getAttribute("token");
            out.print("window.token = '" + token + "'");
        %>
    </script>
</head>
<body>
<div style="flex: 1" class="container-lg">
    <!-- 顶栏 -->
    <div class="ss-card" style="display: flex;align-items: center;margin-top: 20px;">
        <div style="width: 5px;height: 2rem;background: var(--color-main);border-radius: 5px;margin-right: 10px;"></div>
        <span style="font-size: 1.2rem;font-weight: bold;color: var(--color-font);">果糖博客</span>
        <span style="font-size: 1rem;color: var(--color-font-1);display: inline-block;margin-left: 10px;margin-top: 0.4rem;">初始化配置</span>
        <div style="flex: 1"></div>
        <div class="err-bar" id="err-bar">
            <span id="err-bar-txt">这是错误条</span>
        </div>
    </div>
    <!-- 主卡片 -->
    <div class="ss-card show-card">
        <!-- 侧边选项卡 -->
        <div id="list-options" class="list-group opt-list">
            <a class="list-group-item list-group-item-action">目录</a>
            <a class="list-group-item list-group-item-action" href="#list-item-1">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M480 288H32c-17.62 0-32 14.38-32 32v128c0 17.62 14.38 32 32 32h448c17.62 0 32-14.38 32-32v-128C512 302.4 497.6 288 480 288zM352 408c-13.25 0-24-10.75-24-24s10.75-24 24-24s24 10.75 24 24S365.3 408 352 408zM416 408c-13.25 0-24-10.75-24-24s10.75-24 24-24s24 10.75 24 24S429.3 408 416 408zM480 32H32C14.38 32 0 46.38 0 64v128c0 17.62 14.38 32 32 32h448c17.62 0 32-14.38 32-32V64C512 46.38 497.6 32 480 32zM352 152c-13.25 0-24-10.75-24-24S338.8 104 352 104S376 114.8 376 128S365.3 152 352 152zM416 152c-13.25 0-24-10.75-24-24S402.8 104 416 104S440 114.8 440 128S429.3 152 416 152z"/></svg>
                系统配置
            </a>
            <a class="list-group-item list-group-item-action" href="#list-item-2">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M362.7 19.32C387.7-5.678 428.3-5.678 453.3 19.32L492.7 58.75C517.7 83.74 517.7 124.3 492.7 149.3L444.3 197.7L314.3 67.72L362.7 19.32zM421.7 220.3L188.5 453.4C178.1 463.8 165.2 471.5 151.1 475.6L30.77 511C22.35 513.5 13.24 511.2 7.03 504.1C.8198 498.8-1.502 489.7 .976 481.2L36.37 360.9C40.53 346.8 48.16 333.9 58.57 323.5L291.7 90.34L421.7 220.3z"/></svg>
                基本设置
            </a>
            <a class="list-group-item list-group-item-action" href="#list-item-3">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M224 256c70.7 0 128-57.31 128-128s-57.3-128-128-128C153.3 0 96 57.31 96 128S153.3 256 224 256zM274.7 304H173.3C77.61 304 0 381.6 0 477.3c0 19.14 15.52 34.67 34.66 34.67h378.7C432.5 512 448 496.5 448 477.3C448 381.6 370.4 304 274.7 304z"/></svg>
                初始账号
            </a>
            <a class="list-group-item list-group-item-action" href="#list-item-4">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512"><path d="M336 64h-53.88C268.9 26.8 233.7 0 192 0S115.1 26.8 101.9 64H48C21.5 64 0 85.48 0 112v352C0 490.5 21.5 512 48 512h288c26.5 0 48-21.48 48-48v-352C384 85.48 362.5 64 336 64zM192 64c17.67 0 32 14.33 32 32s-14.33 32-32 32S160 113.7 160 96S174.3 64 192 64zM282.9 262.8l-88 112c-4.047 5.156-10.02 8.438-16.53 9.062C177.6 383.1 176.8 384 176 384c-5.703 0-11.25-2.031-15.62-5.781l-56-48c-10.06-8.625-11.22-23.78-2.594-33.84c8.609-10.06 23.77-11.22 33.84-2.594l36.98 31.69l72.52-92.28c8.188-10.44 23.3-12.22 33.7-4.062C289.3 237.3 291.1 252.4 282.9 262.8z"/></svg>
                完成配置
            </a>
        </div>
        <!-- 主内容 -->
        <div data-spy="scroll" data-target="#list-options" data-offset="0" class="opt-body" id="scroll-body">
            <!-- 1 -->
            <div class="opt-in-body db-info" style="height: calc(100vh - 260px - 2rem);">
                <h4 id="list-item-1">系统配置</h4>
                <%
                    Boolean statue = (Boolean) request.getAttribute("databaseStatue");
                    String sqlVersion = (String) request.getAttribute("sqlVer");
                    if(statue) {
                        out.print("<div class=\"ss-card\" style=\"background: #d4edda;\">已连接</div>");
                    } else {
                        out.print("<div class=\"ss-card\" style=\"background: #f8d7da;\">未连接</div>");
                    }
                    JdbcConfig configInfo = (JdbcConfig) request.getAttribute("sqlConfig");
                    List<String> sysInfo = (List<String>) request.getAttribute("sysInfo");
                %>
                <div class="ss-card">
                    <div>
                        <span><%=sysInfo.get(0)%></span>
                        <span>内存占用：<%=sysInfo.get(1)%></span>
                    </div>
                    <div>
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M480 288H32c-17.62 0-32 14.38-32 32v128c0 17.62 14.38 32 32 32h448c17.62 0 32-14.38 32-32v-128C512 302.4 497.6 288 480 288zM352 408c-13.25 0-24-10.75-24-24s10.75-24 24-24s24 10.75 24 24S365.3 408 352 408zM416 408c-13.25 0-24-10.75-24-24s10.75-24 24-24s24 10.75 24 24S429.3 408 416 408zM480 32H32C14.38 32 0 46.38 0 64v128c0 17.62 14.38 32 32 32h448c17.62 0 32-14.38 32-32V64C512 46.38 497.6 32 480 32zM352 152c-13.25 0-24-10.75-24-24S338.8 104 352 104S376 114.8 376 128S365.3 152 352 152zM416 152c-13.25 0-24-10.75-24-24S402.8 104 416 104S440 114.8 440 128S429.3 152 416 152z"/></svg>
                    </div>
                </div>
                <div class="ss-card">
                    <div>
                        <span><%=configInfo.getType().toUpperCase()%><span><%=sqlVersion%></span></span>
                        <span>数据库中间件：<%=configInfo.getDriver()%></span>
                        <span>数据库链接：<%=configInfo.getUrl().substring(configInfo.getUrl().indexOf("mysql://"), configInfo.getUrl().indexOf("?"))%></span>
                        <span>数据库名字：<%=configInfo.getName()%></span>
                        <span>数据库密码：<%=configInfo.getPassword()%></span>
                    </div>
                    <div>
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M448 80V128C448 172.2 347.7 208 224 208C100.3 208 0 172.2 0 128V80C0 35.82 100.3 0 224 0C347.7 0 448 35.82 448 80zM393.2 214.7C413.1 207.3 433.1 197.8 448 186.1V288C448 332.2 347.7 368 224 368C100.3 368 0 332.2 0 288V186.1C14.93 197.8 34.02 207.3 54.85 214.7C99.66 230.7 159.5 240 224 240C288.5 240 348.3 230.7 393.2 214.7V214.7zM54.85 374.7C99.66 390.7 159.5 400 224 400C288.5 400 348.3 390.7 393.2 374.7C413.1 367.3 433.1 357.8 448 346.1V432C448 476.2 347.7 512 224 512C100.3 512 0 476.2 0 432V346.1C14.93 357.8 34.02 367.3 54.85 374.7z"/></svg>
                    </div>
                </div>
                <div style="flex: 1;background: transparent;"></div>
                <%
                    if(statue) {
                        out.print("<button data-id=\"1\" class=\"ss-button next-btn\" onclick=\"nextStep(this)\">下一步</button>");
                    } else {
                        out.print("<div class=\"ss-card\" style=\"background: #f8d7da;\">数据库连接异常，请检查后再次尝试。</div>");
                    }
                %>
            </div>
            <!-- 2 -->
            <div class="opt-in-body">
                <h4 id="list-item-2">基本设置</h4>
                <div class="info-card ss-card">
                    <div style="border-radius: 7px;background: var(--color-card);" id="site-icon">
                        <svg style="fill: var(--color-font-1);" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M497.5 91.1C469.6 33.13 411.8 0 352.4 0c-27.88 0-56.14 7.25-81.77 22.62L243.1 38.1C227.9 48.12 223 67.75 232.1 82.87l32.76 54.87c8.522 14.2 27.59 20.6 43.88 11.06l27.51-16.37c5.125-3.125 10.95-4.439 16.58-4.439c10.88 0 21.35 5.625 27.35 15.62c9 15.12 3.917 34.59-11.08 43.71L15.6 397.6c-15.25 9.125-20.13 28.62-11 43.87l32.76 54.87c8.522 14.2 27.59 20.66 43.88 11.12l347.4-206.5C500.2 258.1 533.2 167.5 497.5 91.1zM319.7 104.1L317.2 106.5l-20.5-61.5c9.75-4.75 19.88-8.125 30.38-10.25l20.63 61.87C337.8 97.37 328.2 99.87 319.7 104.1zM145.8 431.7l-60.5-38.5l30.88-18.25l60.5 38.5L145.8 431.7zM253.3 367.9l-60.5-38.5l30.88-18.25l60.5 38.5L253.3 367.9zM364.2 301.1L303.7 263.5l30.88-18.25l60.5 38.5L364.2 301.1zM384.7 104.7l46-45.1c8.375 6.5 16 13.1 22.5 22.5l-45.63 45.81C401.9 117.8 393.9 110.1 384.7 104.7zM466.7 212.5l-59.5-19.75c3.25-5.375 5.875-10.1 7.5-17.12c1-4.5 1.625-9.125 1.75-13.62l60.38 20.12C474.7 192.5 471.4 202.7 466.7 212.5z"/></svg>
                    </div>
                    <div>
                        <span id="fb_name" style="font-weight: bold;">果糖博客</span>
                        <span id="fb_desc">啊吧啊吧，这是个甜甜的博客 ——</span>
                    </div>
                </div>
                <div class="option-card">
                    <div class="ss-card">
                        <div></div>
                        <div>
                            <a>博客图标</a>
                            <span>要有个好看的图标才是。</span>
                        </div>
                        <div>
                            <label>
                                <button class="ss-button" style="min-width: 193px;" onclick="document.getElementById('icon-input').click()">
                                    选择图标
                                </button>
                                <input style="opacity: 0;width: 0" type="file" id="icon-input" onchange="selectIcon(this)">
                            </label>
                        </div>
                    </div>
                    <div class="ss-card">
                        <div></div>
                        <div>
                            <a>博客标题</a>
                            <span>起个响亮的名字！</span>
                        </div>
                        <div>
                            <label>
                                <input oninput="siteCChange(this)" type="text" name="fb_name" value="果糖博客" placeholder="请输入博客标题"/>
                            </label>
                        </div>
                    </div>
                    <div class="ss-card">
                        <div></div>
                        <div>
                            <a>博客简介</a>
                            <span>这是干什么的呢（小声</span>
                        </div>
                        <div>
                            <label>
                                <input oninput="siteCChange(this)" type="text" name="fb_desc" value="啊吧啊吧，这是个甜甜的博客 ——" placeholder="请输入博客简介"/>
                            </label>
                        </div>
                    </div>
                    <div class="ss-card">
                        <div></div>
                        <div>
                            <a>允许注册</a>
                            <span>让大家可以一起加入吧！</span>
                        </div>
                        <div>
                            <label class="ss-switch">
                                <input type="checkbox" name="cfg_allow_reg" onchange="siteCBChange(this)">
                                <div><div></div></div>
                            </label>
                        </div>
                    </div>
                </div>
                <div style="flex: 1;"></div>
                <button id="base-set" data-id="2" class="ss-button next-btn" onclick="siteConfirm(this)">确定</button>
            </div>
            <!-- 3 -->
            <div class="opt-in-body">
                <h4 id="list-item-3">初始账号</h4>
                <div class="user-card ss-card">
                    <div style="border-radius: 7px;background: var(--color-card);"></div>
                    <div>
                        <span>果糖博客</span>
                    </div>
                </div>
                <div class="option-card">
                    <div class="ss-card">
                        <div></div>
                        <div>
                            <a>用户名</a>
                            <span>叫什么好呢 ——</span>
                        </div>
                        <div>
                            <label>
                                <input type="text" name="uname" placeholder="你的名字"/>
                            </label>
                        </div>
                    </div>
                    <div class="ss-card">
                        <div></div>
                        <div>
                            <a>邮箱</a>
                            <span>要用来联系哦</span>
                        </div>
                        <div>
                            <label>
                                <input type="text" name="umail" placeholder="你的邮箱"/>
                            </label>
                        </div>
                    </div>
                    <div class="ss-card">
                        <div></div>
                        <div>
                            <a>密码</a>
                            <span>看不见看不见</span>
                        </div>
                        <div>
                            <label>
                                <input type="password" name="upwd" placeholder="你的密码"/>
                            </label>
                        </div>
                    </div>
                </div>
                <div style="flex: 1;"></div>
                <button class="ss-button next-btn">确定</button>
            </div>
            <!-- 4 -->
            <div class="opt-in-body">
                <h4 id="list-item-4">完成配置</h4>
                <div class="ss-card finish-card">
                    <div>
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path d="M438.6 105.4C451.1 117.9 451.1 138.1 438.6 150.6L182.6 406.6C170.1 419.1 149.9 419.1 137.4 406.6L9.372 278.6C-3.124 266.1-3.124 245.9 9.372 233.4C21.87 220.9 42.13 220.9 54.63 233.4L159.1 338.7L393.4 105.4C405.9 92.88 426.1 92.88 438.6 105.4H438.6z"/></svg>
                    </div>
                    <span>差不多了</span>
                </div>
                <div class="ss-card" style="margin-top: 20px;display: flex;align-items: center;background: var(--color-card-1);justify-content: center;">
                    <span>配置已经完成，在你点击确认之后将无法再回到这里</span>
                </div>
                <div style="flex: 1"></div>
                <button class="ss-button next-btn">结束吧</button>
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
<script src="https://api.stapxs.cn/js/util/jquery-1.12.4.js"></script>
<script src="https://api.stapxs.cn/bootstrap/popper.min.js"></script>
<script src="https://api.stapxs.cn/bootstrap/bootstrap.min.js"></script>
<script src="https://api.stapxs.cn/js/main.js"></script>
<script src="https://api.stapxs.cn/js/util/prism.js"></script>
<script src="https://api.stapxs.cn/js/auto-theme.js"></script>
</body>
</html>
