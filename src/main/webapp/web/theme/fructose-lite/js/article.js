// 加载编辑器
$(function() {
    window.editor = editormd("editor", {
        path : "/editor/lib/",
        editorTheme : 'pastel-on-dark',
        previewTheme : "dark",
        theme : "dark",
        htmlDecode : "style,script,iframe,sub,sup|on*",
        saveHTMLToTextarea : true,
        lineNumbers: false,
        toolbar: false,
        watch : false,
        tex: true,
        onload : function() {

        }
    });
});

// 处理链接显示
document.getElementById("link_address").innerText = "链接：" + document.URL
// 尝试加载头像
if(getCookie("id")) {
    document.getElementById("avatar-img").src = "/api/account/avatar/" + getCookie("id") + "/img";
    document.getElementById("send-user-img").src = "/api/account/avatar/" + getCookie("id") + "/img";
}

// 验证登陆状态
fetch("/api/account/verify?id=" + getCookie("id") + "&str=" + getCookie("token"), {
    method: "POST"
})
    .then(response => response.json())
    .then(json => {
        if (json.code === 200) {
            document.getElementById("login-name").innerText = getCookie("name")
            document.getElementById("no-login-pan").parentNode.removeChild(document.getElementById("no-login-pan"))
            window.logined = true
        } else {
            document.getElementById("login-info-pan").innerHTML = `<a href="/login?back=${encodeURIComponent(document.URL.substring(document.URL.indexOf("/")))}">登陆</a><span style="margin-left: 10px;">以使用站内评论。</span>`
        }
    })
    .catch(() => {
        document.getElementById("login-info-pan").innerHTML = `<a href="/login">登陆</a><span style="margin-left: 10px;">以使用站内评论。</span>`
    })


// 生成目录
const hTags = document.getElementById("article-main").querySelectorAll("h1, h2, h3, h4")
// 遍历转换成 h 等级和 id 的列表
const hList = Array.from(hTags).map(h => {
    const hLevel = h.tagName.substring(1)
    const hId = h.id
    const hText = h.getElementsByTagName("a")[0].name
  return { hLevel, hId, hText }
})
// 生成目录树
startLevel = 0
lastLevel = 0
let html = ""
hList.forEach(item => {
    if (item.hLevel > startLevel) {
        if (item.hLevel > lastLevel) {
            html += "";
        } else if (item.hLevel < lastLevel) {
            html += (new Array(lastLevel - item.hLevel + 2)).join("</ul></li>");
        } else {
            html += "</ul></li>";
        }

        html += "<li><a id=\"" + item.hId + "-list\" class=\"toc-level-" + item.hLevel + "\" href=\"#" + item.hId + "\" level=\"" + item.hLevel + "\">" + item.hText + "</a><ul>";
        lastLevel = item.hLevel;
    }
})
const div = document.createElement("div")
div.classList.add("content-body")
div.innerHTML = html
div.children[0].children[0].classList.add("li-a-show")
div.children[0].style.marginTop = "-30px"
document.getElementById("content").append(div)
// 添加进度条
const progress = document.createElement("div")
progress.id = "content-progress"
progress.classList.add("content-progress")
document.getElementById("content").append(progress)


// 监听 H 标头进入和离开可视状态
const observer = new IntersectionObserver((entries, observer) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList = "article-main-h-in-view"
        } else {
            entry.target.classList = ""
        }
    })
})
// 添加所有 H 进入监听
document.getElementById("article-main").querySelectorAll("h1, h2, h3, h4, h5, h6").forEach(h => {
    observer.observe(h)
})
// 加载当前分类推送
fetch("/api/article/list/" + document.getElementById("add-1").dataset.sort)
    .then(res => res.json())
    .then(data => {
        if (data.code === 200) {
            // 只取前三个
            if(data.data.length === 0) {
                document.getElementById("more-sort-card").style.display = "none"
            }
            let max = 3
            for (let i = 0; i < max; i++) {
                // 什么都没获取到
                if (data.data[i] === undefined) {
                    break
                }
                // 不输出日记文章
                if(data.data[i].article.art_appendix.indexOf("\"note\":true") > 0) {
                    max++
                    continue
                }
                // 创建详细内容板块
                if(max - 3 - i === 0) {
                    const card = document.createElement("div")
                    card.classList.add("art-body")
                    card.innerHTML = `<div></div><div><div><span style="line-height: 40px;margin-left: 30px;color: var(--color-font-r);">看看别的</span>
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 88.05 64"><defs><style>.cls-2A{fill:var(--color-main);}</style></defs><g id="P_2" data-name="P 2"><g id="P_2-2" data-name="P 2"><polygon class="cls-2A" points="0 0 88.05 0 42.19 33 88.05 64 0 64 0 0"></polygon></g></g></svg>
                    </div>
                    <a href="/article/${data.data[i].article.art_link}">${data.data[i].article.art_title}</a>
                    <div><span>${data.data[i].article.art_html}</span></div>
                    <div>
                        <img alt="头像" src="/api/account/avatar/${data.data[i].article.user_id}/img">
                        <div>
                            <a>${data.data[i].user_nick}</a>
                            <span>${data.data[i].article.art_date}</span>
                        </div></div></div></div>`
                    document.getElementById("more-sort-card").parentNode.append(card)
                }
                // 填充列表
                const id = i - (max - 3) + 1
                const a = document.getElementById("add-" + id)
                a.href = "/article/" + data.data[i].article.art_link
                a.innerText = data.data[i].article.art_title
                // 显示列表
                document.getElementById("more-sort-card").style.display = "flex"
            }
        }
    })
    .catch(err => {
        console.log(err)
    });

// 加载评论
fetch("/api/comment/get/article/" + art_id)
    .then(res => res.json())
    .then(data => {
        if (data.code === 200) {
            const comments = data.data
            comments.forEach(comment => {
                buildComment(comment, document.getElementById("comment-body"))
            });
        }
    })
    .catch(err => {
        console.log(err)
    });

function buildComment(comment, addAt) {
    // 获取参数
    let user_name = null
    let user_avatar = null
    if(comment.user_id === -1) {
        user_name = comment.user_name
        user_avatar = gravatar_url + md5(comment.user_mail) + "?s=50"
        addComment(comment, addAt, user_name, user_avatar)
    } else {
        // 获取用户信息
        fetch("/api/account/base/" + comment.user_id)
            .then(res => res.json())
            .then(data => {
                if (data.code === 200) {
                    user_name = data.data.user_nick
                    user_avatar = `/api/account/avatar/${comment.user_id}/img`
                    addComment(comment, addAt, user_name, user_avatar)
                }
            })
    }

}

function addComment(comment, addAt, user_name, user_avatar) {
    // 创建元素
    if(user_name != null && user_avatar != null) {
        const comment_div = document.createElement("div")
        comment_div.classList.add("comment-main")
        comment_div.id = comment.com_id
        comment_div.innerHTML = `<img name="avater" src="${user_avatar}">
                <div class="comment-body-body">
                    <p>${user_name}</p>
                    <span>${comment.com_comment}</span>
                </div>
                <div class="comment-body-control">
                    <div onclick="comReplay('${comment.com_id}')">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M8.31 189.9l176-151.1c15.41-13.3 39.69-2.509 39.69 18.16v80.05C384.6 137.9 512 170.1 512 322.3c0 61.44-39.59 122.3-83.34 154.1c-13.66 9.938-33.09-2.531-28.06-18.62c45.34-145-21.5-183.5-176.6-185.8v87.92c0 20.7-24.31 31.45-39.69 18.16l-176-151.1C-2.753 216.6-2.784 199.4 8.31 189.9z"/></svg>
                        <span>回复</span>
                    </div>
                </div>`
        addAt.appendChild(comment_div)
        if (comment.com_children !== undefined) {
            const childrens = comment.com_children.split(",")
            console.log(comment.com_children)
            console.log(childrens)
            childrens.forEach(children => {
                fetch("/api/comment/get/com/" + children)
                    .then(res => res.json())
                    .then(data => {
                        if (data.code === 200) {
                            const comment = data.data
                            buildComment(comment, comment_div)
                        }
                    })
                    .catch(err => {
                        console.log(err)
                    });
            })
        }
    }
}

function comReplay(id) {
    window.replay = id
    const p = document.createElement("p")
    p.innerHTML = "<span>回复评论，</span><a onclick=\"comRepCancel()\">取消</a></span>"
    document.getElementById("login-info-pan").append(p)
}

function comRepCancel() {
    window.replay = undefined
    document.getElementById("login-info-pan").removeChild(document.getElementById("login-info-pan").getElementsByTagName("p")[0])
}

window.onscroll = function() {
    // 处理顶栏
    const scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    if(scrollTop > 80 && window.onView !== true) {
        changeBar()
        document.getElementById("main-bar").style.transform = "translate(0, -60px)"
        document.getElementById("article-bar").style.transform = "translate(0, -20px)"
        document.getElementById("article-bar").style.setProperty ("display", "flex", "important");
        document.getElementById("nav").style.borderBottom = "3px solid var(--color-main)";
        document.getElementById("nav-controller").style.display = "flex"
    } else if(scrollTop < 80 && window.onView === true) {
        changeBar()
        document.getElementById("main-bar").style.transform = "translate(0)"
        document.getElementById("article-bar").style.transform = "translate(0, 60px)"
        document.getElementById("article-bar").style.setProperty ("display", "none", "important");
        document.getElementById("nav").style.borderBottom = "3px solid transparent";
        document.getElementById("nav-controller").style.display = "none"
    }
    // 寻找哪个标签在可视区域内
    const hTags = document.getElementById("article-main").querySelectorAll("h1, h2, h3, h4")
    for(let i = 0; i < hTags.length; i++) {
        const h = hTags[i]
        if(getElementTop(h) < scrollTop + document.body.clientHeight && getElementTop(h) > scrollTop) {
            if(window.lastInVIew !== h) {
                if(window.lastInVIew !== undefined) {
                    document.getElementById(window.lastInVIew.id + "-list").classList.remove("li-a-show")
                }
                window.lastInVIew = h
                document.getElementById(h.id + "-list").classList.add("li-a-show")
            }
            break;
        }
    }
    // 计算阅读进度
    const endHeight = getElementTop(document.getElementById("end-info"))
    const artTop = getElementTop(document.getElementById("article-main"))
    let percent = (scrollTop - artTop) / (endHeight - document.body.clientHeight)
    if(percent > 1) {
        percent = 1
    }
    if(percent < 0) {
        percent = 0
    }
    const progressBar = document.getElementById("content-progress")
    if(progressBar) {
        progressBar.style.width = "calc(calc(100% + 40px) * " + percent + ")"
    }
}

function barController() {
    const scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    if(scrollTop > 80) {
        changeBar()
        document.getElementById("nav").style.marginTop = "0"
    }
}

function changeBar() {
    if (window.onView === undefined || window.onView === false) {
        window.onView = true
        document.getElementById("nav").style.marginTop = "0"
        document.getElementById("nav").style.borderRadius = "0 0 7px 7px"
        document.getElementById("nav").style.transform = "translate(0, calc(-100% - 20px))"
        document.getElementById("nav-controller").style.margin = "30px auto 0"
        document.getElementById("nav-controller").style.opacity = "1"

        document.getElementById("content").style.top = "20px"
    } else {
        window.onView = false
        document.getElementById("nav").style.marginTop = "20px"
        document.getElementById("nav").style.borderRadius = "7px"
        document.getElementById("nav").style.transform = "translate(0)"
        document.getElementById("nav-controller").style.margin = "-10px auto 0"
        document.getElementById("nav-controller").style.opacity = "0"

        document.getElementById("content").style.top = "80px"
    }
}

function getElementTop (el) {
    let actualTop = el.offsetTop;
    let current = el.offsetParent;
    while (current !== null) {
        actualTop += current.offsetTop
        current = current.offsetParent
    }
    return actualTop
}

function changeContent() {
    const body = document.getElementById("main-right")
    if(body.style.width === "0px") {
        body.style.width = "300px"
        body.style.overflow = "unset"
    } else {
        body.style.width = "0px"
        body.style.overflow = "hidden"
    }
}

function logout() {
    const id = getCookie("id")
    if(id !== undefined) {
        document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/"
    }
    fetch("/api/account/logout/" + id)
        .finally(() => {
            window.location.reload()
        })
}

// 发送评论
function commentsSend(sender) {
    sender.innerHTML = '<svg style="width: 20px;fill: var(--color-font-r);" class="fa-spin" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M222.7 32.15C227.7 49.08 218.1 66.9 201.1 71.94C121.8 95.55 64 169.1 64 255.1C64 362 149.1 447.1 256 447.1C362 447.1 448 362 448 255.1C448 169.1 390.2 95.55 310.9 71.94C293.9 66.9 284.3 49.08 289.3 32.15C294.4 15.21 312.2 5.562 329.1 10.6C434.9 42.07 512 139.1 512 255.1C512 397.4 397.4 511.1 256 511.1C114.6 511.1 0 397.4 0 255.1C0 139.1 77.15 42.07 182.9 10.6C199.8 5.562 217.6 15.21 222.7 32.15V32.15z"/></svg>';
    // 收集提交内容
    const content = window.editor.getHTML()
    const content_md = window.editor.getMarkdown()
    if(content === "" || content_md === "") {
        sender.innerText = "评论不能为空"
        setTimeout(() => {
            sender.innerText = "发表评论"
        }, 3000)
        return
    }
    let url = "/api/comment/send?aid=" + art_id
    if(window.logined === true) {
        url += "&uid=" + getCookie("id") + "&utoken=" + getCookie("token")
    } else {
        const data = {}
        data.user_name = document.getElementById("comments-name").value
        data.user_mail = document.getElementById("comments-mail").value
        data.user_site = document.getElementById("comments-site").value
        if(data.user_site === "") {
            data.user_site = null
        }
        // 前两个不能为空
        if(data.user_name === "" || data.user_mail === "") {
            sender.innerText = "请填写完整的信息"
            setTimeout(() => {
                sender.innerText = "发表评论"
            }, 3000)
            return
        }
        url += "&info=" + encodeURIComponent(JSON.stringify(data))
    }
    if(window.replay !== undefined) {
        url += "&replay=" + window.replay
    }
    console.log(url)
    // 发起提交
    fetch(url, {
        method: "POST",
        body: JSON.stringify({md: content_md, html: content}),
    })
        .then(res => res.json())
        .then(res => {
            if(res.code === 200) {
                // window.location.reload()
            } else {
                console.log("发送失败：" + res.str)
                sender.innerText = "操作失败"
                setTimeout(() => {
                    sender.innerText = "发表评论"
                }, 3000)
            }
        })
        .catch(err => {
            console.log(err)
            sender.innerText = "操作失败"
            setTimeout(() => {
                sender.innerText = "发表评论"
            }, 3000)
        })
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