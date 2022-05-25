<%@ page import="cn.stapxs.blog.model.Config" %>
<%@ page import="cn.stapxs.blog.model.Article" %>
<%@ page import="cn.stapxs.blog.model.SortInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/05/12
  Time: 下午 12:26
  To change this template use File | Settings | File Templates.
--%>
<%
    Config configInfo = (Config) request.getAttribute("config");
    List<Article> articles = (List<Article>) request.getAttribute("now-page-articles");
    SortInfo[] sorts = (SortInfo[]) request.getAttribute("sort");
    int page_num = (int) request.getAttribute("page");
    int max_page = (int) request.getAttribute("max-page");
%>
<html>
<head>
    <title><%=configInfo.getFb_name()%></title>

    <link rel="stylesheet" href="/theme/<%=configInfo.getCfg_theme()%>/css/index.css">
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
    <div class="info-body">
        <div>
            <div id="art-body" class="art-body">
                <%
                for (Article article : articles) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // 去除 html 标签
                    String summary = article.getArt_html();
                    summary = summary.replaceAll("<[^>]+>", "");
                    summary = summary.replaceAll("\\s*", "");
                    summary = summary.substring(0, Math.min(summary.length(), 300));
                    out.print(String.format("<div>\n" +
                            "                    <a href=\"/article/%s\">%s</a>\n" +
                            "                    <div>\n" +
                            "                        <span>作者：</span><a href=\"#\">%s</a>\n" +
                            "                        <div></div>\n" +
                            "                        <span>时间：%s</span>\n" +
                            "                        <div></div>\n" +
                            "                        <span>分类：</span><span>%s</span>\n" +
                            "                    </div>\n" +
                            "                    <span>%s</span>\n" +
                            "                </div>",
                            article.getArt_link(),
                            article.getArt_title(),
                            article.getUser_id(),
                            sdf.format(article.getArt_date()),
                            article.getArt_sort().equals("") ? "无分类" : article.getArt_sort(),
                            summary));
                }
                %>
            </div>
        </div>
        <div>

        </div>
    </div>
    <div id="page-controller" class="page-controller">
        <button onclick="changePage(false)">上一页</button>
        <button onclick="changePage(true)">下一页</button>
    </div>
    <footer>
        <p><%=configInfo.getFb_name()%></p>
        <span>Powered by 果糖博客. Copyright © Stapx Steve [ 林槐 ].</span>
    </footer>
</div>
</body>
<script>
    // 切换页面
    function changePage(next) {
        if(!next) {
            window.location.href = "/page/<%=Math.max(page_num - 1, 1)%>";
        } else {
            window.location.href = "/page/<%=Math.min(page_num + 1, max_page)%>";
        }
    }
    // 处理用户名
    const list = document.getElementById("art-body").children;
    for(let i = 0; i < list.length; i++) {
        const nameBody = list[i].children[1].children[1];
        const name = nameBody.innerText;
        fetch("/api/account/base/" + name)
            .then(res => res.json())
            .then(res => {
                if (res.code === 200) {
                    if (res.data.user_nick !== undefined) {
                        nameBody.innerText = res.data.user_nick;
                    } else {
                        nameBody.innerText = res.data.user_name;
                    }
                }
            });
    }
</script>

</html>
