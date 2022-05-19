// 获取主题附加参数
fetch("/api/config/string/cfg_theme")
    .then(res => res.json())
    .then(data => {
        if (data.code === 200) {
            const theme = data.str;
            // 获取主题配置文件
            // 获取详细信息
            fetch('/theme/config/' + theme + ".json")
                .then(response => response.json())
                .then(data => {
                    // 显示配置项
                    const config = data.config;
                    const list = document.getElementById("add-opt-list")
                    config.forEach(item => {
                        let input = ""
                        switch (item[0]) {
                            case "checkbox" : {
                                input = `<label class="ss-switch"><input type="checkbox" id="${item[1]}"><div><div></div></div></label>`;
                                break;
                            }
                            case "input" : {
                                input = `<input type="text" id="${item[1]}">`;
                                break;
                            }
                            case "select" : {
                                input = `<select id="${item[1]}">`;
                                item[4].forEach(option => {
                                    input += `<option value="${option}">${option}</option>`;
                                });
                                input += "</select>";
                                break;
                            }
                        }
                        let html = document.createElement("div");
                        html.classList = "ss-card";
                        html.innerHTML = `<div></div>
                    <div>
                        <a>${item[2]}</a>
                        <span>${item[3]}</span>
                    </div>
                    <div>
                        <label>
                            ${input}
                        </label>
                    </div>`;
                        list.appendChild(html);
                    });
                });
        }
    })
// 初始化编辑器
$(function() {
    window.parent.editor = editormd("editor", {
        path : "/editor/lib/",
        theme : "dark",
        previewTheme : "dark",
        editorTheme : 'pastel-on-dark',
        watch : false,
        saveHTMLToTextarea : true,
        htmlDecode : "style|on*",
        tex: true,
        imageName: "uploadFile",
        imageUpload : true,
        imageFormats : ["jpg", "jpeg", "gif", "png"],
        imageUploadURL : "/api/article/img/upload?id=" + getCookie("id") + "&token=" + getCookie("token"),
        toolbarIcons : function() {
            return ["undo", "redo", "|",
                "bold", "del", "italic", "quote", "|",
                "h1", "h2", "h3", "h4", "|",
                "table", "list-ul", "list-ol", "hr", "|",
                "link", "reference-link", "image", "code", "code-block", "|",
                "watch", "preview", "full", "search", "|",
                "help", "info"
            ]
        },
        toolbarIconsClass : {
            full : "fa-arrows-alt"  // 重新实现的全屏按钮
        },
        lang : {
            toolbar : {
                full : "全屏"
            }
        },
        toolbarHandlers : {
            full : function() {
                if (self.frameElement && self.frameElement.tagName === "IFRAME") {
                    if(self.frameElement.style.position === "absolute") {
                        self.frameElement.style.position = "relative";
                    } else {
                        self.frameElement.style.position = "absolute";
                    }
                }
                this.fullscreen();
            },
        },
        onload : function() {
            // 加载编辑模式
            if(window.msgId !== undefined && window.msgId !== null) {
                const id = window.msgId;
                // 获取文章信息
                fetch(`/api/article/get/${id}/md`)
                    .then(res => res.json())
                    .then(data => {
                        if (data.code === 200) {
                            // 填充信息
                            window.parent.editor.setMarkdown(data.data.art_markdown);
                            document.getElementById("title").value = data.data.art_title;
                            document.getElementById("send_date").value = data.data.art_date;
                            document.getElementById("link").value = data.data.art_link;
                            document.getElementById("public").value = data.data.art_statue;
                            document.getElementById("use").value = data.data.art_quote === undefined ? "" : data.data.art_quote;
                            const tags = data.data.art_tag === undefined ? []:data.data.art_tag.split(",");
                            tags.forEach(item => {
                                createTag(item);
                            });
                            const sort = data.data.art_sort === undefined ? []:data.data.art_sort.split(",");
                            sort.forEach(item => {
                                const body = document.getElementById("sort-list").children;
                                for(let i = 0; i < body.length; i++) {
                                    if(body[i].dataset.name === item) {
                                        body[i].getElementsByTagName("input")[0].click();
                                    }
                                }
                            });
                            const appendix = JSON.parse(data.data.art_appendix);
                            Object.keys(appendix).forEach(item => {
                                const input = document.getElementById(item);
                                if(input.type === "checkbox") {
                                    input.checked = appendix[item];
                                } else {
                                    input.value = appendix[item];
                                }
                            })
                        } else {
                            document.getElementById("body").innerHTML = "加载失败：" + data.str;
                        }
                    })
                    .catch(err => {
                        console.log(err);
                    })
                    .finally(() => {
                        const tab = window.parent.document.getElementById("tabp-article");
                        const button = tab.children[0].getElementsByTagName("button")[0];
                        button.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 320 512\"><path d=\"M310.6 361.4c12.5 12.5 12.5 32.75 0 45.25C304.4 412.9 296.2 416 288 416s-16.38-3.125-22.62-9.375L160 301.3L54.63 406.6C48.38 412.9 40.19 416 32 416S15.63 412.9 9.375 406.6c-12.5-12.5-12.5-32.75 0-45.25l105.4-105.4L9.375 150.6c-12.5-12.5-12.5-32.75 0-45.25s32.75-12.5 45.25 0L160 210.8l105.4-105.4c12.5-12.5 32.75-12.5 45.25 0s12.5 32.75 0 45.25l-105.4 105.4L310.6 361.4z\"></path></svg>";
                    });
            } else {
                const tab = window.parent.document.getElementById("tabp-article");
                const button = tab.children[0].getElementsByTagName("button")[0];
                button.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 320 512\"><path d=\"M310.6 361.4c12.5 12.5 12.5 32.75 0 45.25C304.4 412.9 296.2 416 288 416s-16.38-3.125-22.62-9.375L160 301.3L54.63 406.6C48.38 412.9 40.19 416 32 416S15.63 412.9 9.375 406.6c-12.5-12.5-12.5-32.75 0-45.25l105.4-105.4L9.375 150.6c-12.5-12.5-12.5-32.75 0-45.25s32.75-12.5 45.25 0L160 210.8l105.4-105.4c12.5-12.5 32.75-12.5 45.25 0s12.5 32.75 0 45.25l-105.4 105.4L310.6 361.4z\"></path></svg>";
            }
        }
    });
});
// 获取分类列表
fetch('/api/sort/list')
    .then(res => res.json())
    .then(data => {
        if (data.code === 200) {
            const list = data.data;
            if(list.length > 0) {
                list.forEach(item => {
                    const div = document.createElement("div");
                    div.classList.add("fl-body");
                    div.dataset.name = item.info.sort_name;
                    div.innerHTML = `<span>${item.info.sort_title}</span><label class="ss-switch"><input type="checkbox"><div><div></div></div></label>`;
                    document.getElementById("sort-list").appendChild(div);
                });
            } else {
                document.getElementById("sort-body").style.display = "none";
            }
        }
    })
    .catch(err => {
        console.log(err);
    });

// 添加 tag
function addTag(sender) {
    createTag(sender.getElementsByTagName("input")[0].value)
    // 清空输入框
    sender.getElementsByTagName("input")[0].value = "";
    return false;
}
function createTag(name) {
    // name 不能为空或空白符号
    if(name.trim() === "") {
        return false;
    }
    // 添加 html 进入页面
    const div = document.createElement("div");
    div.classList.add("tab-body");
    div.innerHTML = `<span>${name}</span><button onclick="delTag(this)">
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512"><path d="M310.6 361.4c12.5 12.5 12.5 32.75 0 45.25C304.4 412.9 296.2 416 288 416s-16.38-3.125-22.62-9.375L160 301.3L54.63 406.6C48.38 412.9 40.19 416 32 416S15.63 412.9 9.375 406.6c-12.5-12.5-12.5-32.75 0-45.25l105.4-105.4L9.375 150.6c-12.5-12.5-12.5-32.75 0-45.25s32.75-12.5 45.25 0L160 210.8l105.4-105.4c12.5-12.5 32.75-12.5 45.25 0s12.5 32.75 0 45.25l-105.4 105.4L310.6 361.4z"/></svg>
</button>`;
    document.getElementById("tag-list").appendChild(div);
}

function delTag(sender) {
    sender.parentNode.remove();
}

// 上传文章
function uploadArticle(sender) {
    // 替换加载图标
    sender.innerHTML = '<svg style="width: 20px;fill: var(--color-font-r);" class="fa-spin" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M222.7 32.15C227.7 49.08 218.1 66.9 201.1 71.94C121.8 95.55 64 169.1 64 255.1C64 362 149.1 447.1 256 447.1C362 447.1 448 362 448 255.1C448 169.1 390.2 95.55 310.9 71.94C293.9 66.9 284.3 49.08 289.3 32.15C294.4 15.21 312.2 5.562 329.1 10.6C434.9 42.07 512 139.1 512 255.1C512 397.4 397.4 511.1 256 511.1C114.6 511.1 0 397.4 0 255.1C0 139.1 77.15 42.07 182.9 10.6C199.8 5.562 217.6 15.21 222.7 32.15V32.15z"/></svg>';
    // 获取数据
    const title = document.getElementById("title").value;
    const link = document.getElementById("link").value;
    // 标题和正文不能为空
    if(title.trim() === "" || window.parent.editor.getMarkdown() === "") {
        sender.innerText = "标题和正文不能为空";
        setTimeout(() => {
            sender.innerText = "发布文章";
        }, 3000);
        return
    }
    // 生成参数
    let params = { id: getCookie("id"), token: getCookie("token"), title: title }
    if(link.trim() !== "") params.link = link;
    let str = JSON.stringify(params)
        .replaceAll("{", "")
        .replaceAll("}", "")
        .replaceAll(":", "=")
        .replaceAll(",", "&")
        .replaceAll("\"", "");
    // 访问 API
    let url = "/api/article/upload?";
    if(window.msgId !== undefined && window.msgId !== null) {
        url = "/api/article/update?art_id=" + window.msgId + "&";
    }
    fetch(url + str, {
        method: "POST",
        body: window.parent.editor.getMarkdown()
    }).then(res => res.json()).then(data => {
        if(data.code === 200) {
            // 继续提交 HTML 预渲染数据
            updateHTML(sender, data.str);
        } else {
            console.log(data.str);
            sender.innerText = "操作失败";
            setTimeout(() => {
                sender.innerText = "发布文章";
            }, 3000);
        }
    }).catch(err => {
        console.log(err);
        sender.innerText = "操作失败";
        setTimeout(() => {
            sender.innerText = "发布文章";
        }, 3000);
    });
}

// ----------------------------------------


function updateHTML(sender, artId) {
    const params = { id: getCookie("id"), token: getCookie("token"), artId: artId }
    const str = JSON.stringify(params)
        .replaceAll("{", "")
        .replaceAll("}", "")
        .replaceAll(":", "=")
        .replaceAll(",", "&")
        .replaceAll("\"", "");
    // 继续提交预渲染 HTML
    fetch("/api/article/upload/html?" + str, {
        method: "POST",
        body: window.parent.editor.getHTML()
    }).then(res => res.json()).then(data => {
        if (data.code === 200) {
            // 继续提交设置
            updateOption(sender, str);
        } else {
            console.log("上传预渲染 HTML 失败了，文章已被保存为草稿。")
            console.log(data.str);
        }
    }).catch(err => {
        console.log("上传预渲染 HTML 失败了，文章已被保存为草稿。")
        console.log(err);
    }).finally(() => {
        const tab = window.parent.document.getElementById("tabp-article");
        const button = tab.children[0].getElementsByTagName("button")[0];
        button.click();
    });
}

function updateOption(sender, str) {
    // 处理数据
    let dataOpt = {}
    if(document.getElementById("send_date").value !== "") {
        dataOpt.art_date = document.getElementById("send_date").value;
    }
    if(document.getElementById("link").value !== "") {
        dataOpt.art_link = document.getElementById("link").value.trim();
        // 链接不能包含斜杠、空白字符
        if(dataOpt.art_link.includes("/") || dataOpt.art_link.includes(" ")) {
            sender.innerText = "名字不能包含斜杠和空白字符";
            setTimeout(() => {
                sender.innerText = "发布文章";
            }, 3000);
            return
        }
    }
    dataOpt.art_sort = [];
    if(document.getElementById("sort-list").children.length > 0) {
        for(let i = 0; i < document.getElementById("sort-list").children.length; i++) {
            if(document.getElementById("sort-list").children[i].getElementsByTagName("input")[0].checked) {
                dataOpt.art_sort.push(document.getElementById("sort-list").children[i].dataset.name);
            }
        }
    }
    dataOpt.art_tag = [];
    if(document.getElementById("tag-list").children.length > 0) {
        for(let i = 0; i < document.getElementById("tag-list").children.length; i++) {
            dataOpt.art_tag.push(document.getElementById("tag-list").children[i].children[0].innerText);
        }
    }
    if(document.getElementById("use").value !== "") {
        dataOpt.art_quote = document.getElementById("use").value;
    }
    dataOpt.art_statue = document.getElementById("public").value;

    dataOpt.art_appendix = {}
    const list = document.getElementById("add-opt-list").children
    for(let i = 0; i < list.length; i++) {
        const input = list[i].getElementsByTagName("input")[0];
        if(input.type === "checkbox") {
            dataOpt.art_appendix[input.id] = input.checked;
        } else {
            dataOpt.art_appendix[input.id] = input.value;
        }
    }

    dataOpt.art_sort = dataOpt.art_sort.toString()
    dataOpt.art_tag = dataOpt.art_tag.toString()
    dataOpt.art_appendix = JSON.stringify(dataOpt.art_appendix)

    console.log(dataOpt);
    // 访问 API
    fetch("/api/article/upload/option?" + str, {
        method: "POST",
        body: JSON.stringify(dataOpt)
    }).then(res => res.json()).then(data => {
            if (data.code !== 200) {
                console.log("上传设置失败了，文章已经被保存为草稿。")
                console.log(data.str);
            }
        })
        .catch(err => {
            console.log("上传设置失败了，文章已经被保存为草稿。")
            console.log(err);
        })
        .finally(() => {
            const tab = window.parent.document.getElementById("tabp-article");
            const button = tab.children[0].getElementsByTagName("button")[0];
            button.click();
        });
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