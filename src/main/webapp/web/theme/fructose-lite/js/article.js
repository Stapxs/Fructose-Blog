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
                console.log(i + "/" + id)
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
