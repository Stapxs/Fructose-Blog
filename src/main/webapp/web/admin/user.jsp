<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: Stapxs
  Date: 2022/05/07
  Time: 下午 01:37
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">

    <link rel="stylesheet" href="https://api.stapxs.cn/css/color-dark.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://api.stapxs.cn/css/style.css">
    <link rel="stylesheet" href="/css/admin/user.css">

    <script>
        is_auto_dark = true
    </script>
</head>
<body style="margin-right: 6px;">
<div class="info-card">
    <div>
        <img src="https://gravatar.loli.net/avatar/75c55a45cadac8669a1c5e4943cda5ce?s\u003d200">
        <p>林小槐</p>
        <span>Stapx Steve</span>
    </div>
    <div class="option-card opt-card-r">
        <div class="ss-card">
            <div></div>
            <div>
                <a>昵称</a>
                <span>只影响显示名称哦</span>
            </div>
            <div>
                <label>
                    <input type="text">
                </label>
            </div>
        </div>
        <div class="ss-card">
            <div></div>
            <div>
                <a>主页地址</a>
                <span>你也有博客？一起来玩吧！</span>
            </div>
            <div>
                <label>
                    <input type="text">
                </label>
            </div>
        </div>
        <div class="ss-card" style="margin-bottom: 0;">
            <div></div>
            <div>
                <a>* 邮箱</a>
                <span>邮箱是主要联系方式，会影响好多东西哦。</span>
            </div>
            <div>
                <label>
                    <input type="text">
                </label>
            </div>
        </div>
    </div>
</div>
<div class="opt-bar">
    <div></div>
    <span>撰写设置</span>
    <button class="ss-button">保存</button>
</div>
<div class="option-card opt-card-r" style="margin-top: 10px;">
    <div class="ss-card">
        <div></div>
        <div>
            <a>启用 Markdown 语法</a>
            <span>使用更方便排版的 Markdown 语法来编写文章，这个选项不影响已经保存的内容。</span>
        </div>
        <div>
            <label class="ss-switch">
                <input type="checkbox">
                <div><div></div></div>
            </label>
        </div>
    </div>
    <div class="ss-card">
        <div></div>
        <div>
            <a>自动保存</a>
            <span>让系统每分钟自动保存编辑内容来防止意外丢失。</span>
        </div>
        <div>
            <label class="ss-switch">
                <input type="checkbox">
                <div><div></div></div>
            </label>
        </div>
    </div>
    <div class="ss-card">
        <div></div>
        <div>
            <a>不让别人评论我</a>
            <span>让别人无法在你的消息下回复，同时他也无法引用你的评论。</span>
        </div>
        <div>
            <label class="ss-switch">
                <input type="checkbox">
                <div><div></div></div>
            </label>
        </div>
    </div>
</div>
<div class="opt-bar">
    <div></div>
    <span>修改密码</span>
    <button class="ss-button">确认</button>
</div>
<div class="option-card opt-card-r" style="margin-top: 10px;">
    <div class="ss-card">
        <div></div>
        <div>
            <a>输入密码</a>
            <span>真的要改密码吗（小声</span>
        </div>
        <div>
            <label>
                <input type="text">
            </label>
        </div>
    </div>
    <div class="ss-card">
        <div></div>
        <div>
            <a>确认密码</a>
            <span>请确认你的密码, 与上面输入的密码保持一致。</span>
        </div>
        <div>
            <label>
                <input type="text">
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
</html>

