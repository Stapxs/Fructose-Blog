// 获取文章列表
fetch("/api/article/list")
    .then(response => response.json())
    .then(data => {
        if (data.code === 200) {
            const articleList = data.data;
            // 类型对照表
            const typeMap = {
                "-2": "draft",
                "-1": "wait",
                "0": "private",
                "1": "public",
            }
            // 遍历列表
            articleList.forEach(info => {
                const body = document.getElementById(typeMap[String(info.article.art_statue)])
                let date = Date.parse(info.article.art_date)
                const newDate = new Date();
                newDate.setTime(date);
                date = newDate.toLocaleDateString();
                if(body.children.length === 0) {
                    // 添加表头
                    const listHead = document.createElement("div");
                    listHead.className = "list-body";
                    listHead.innerHTML = "<div>标题</div><div>作者</div><div>日期</div><div>操作</div>";
                    body.appendChild(listHead);
                }
                // 添加内容
                const listBody = document.createElement("div");
                listBody.className = "list-body";
                listBody.innerHTML = `<div><a>${info.article.art_title}</a></div>
<div>${info.user_nick}</div>
<div>${date}</div>
<div><button class="ss-button" style="background: var(--color-main);color: var(--color-font-r)" onclick="editArticle('${info.article.art_id}')">编辑</button>
<button class="ss-button" id="${info.article.art_id}" onclick="deleteArticle(this, '${info.article.art_id}')">删除</button></div>`;
                body.appendChild(listBody);
            });
            // 遍历类型
            for (const key in typeMap) {
                const body = document.getElementById(typeMap[key]);
                if(body.children.length === 0) {
                    const div = document.createElement("div");
                    div.className = "list-empty";
                    div.innerText = "什么都没有 :("
                    body.appendChild(div);
                }
            }
        } else {
            const div = document.createElement("div");
            div.className = "list-empty";
            div.innerText = "什么都没有 :("
            document.getElementById("body").appendChild(div);
        }
        // 返回的删除操作
        if(getQueryVariable("delete")) {
            // 显示加载图标
            const id = getQueryVariable("delete");
            console.log(id);
            document.getElementById(id).innerHTML = '<svg style="width: 20px;fill: var(--color-font-r);" class="fa-spin" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M222.7 32.15C227.7 49.08 218.1 66.9 201.1 71.94C121.8 95.55 64 169.1 64 255.1C64 362 149.1 447.1 256 447.1C362 447.1 448 362 448 255.1C448 169.1 390.2 95.55 310.9 71.94C293.9 66.9 284.3 49.08 289.3 32.15C294.4 15.21 312.2 5.562 329.1 10.6C434.9 42.07 512 139.1 512 255.1C512 397.4 397.4 511.1 256 511.1C114.6 511.1 0 397.4 0 255.1C0 139.1 77.15 42.07 182.9 10.6C199.8 5.562 217.6 15.21 222.7 32.15V32.15z"/></svg>';
            fetch("/api/article/delete?id=" + getCookie("id") + "&token=" + getCookie("token") + "&art_id=" + id, {
                method: "POST"
            })
                .finally(() => {
                    location.reload();
                })
        }
    })
    .catch(error => {
        console.log(error);
    })
    .finally(() => {
        const tab = window.parent.document.getElementById("tabp-article_list");
        const button = tab.children[0].getElementsByTagName("button")[0];
        button.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 320 512\"><path d=\"M310.6 361.4c12.5 12.5 12.5 32.75 0 45.25C304.4 412.9 296.2 416 288 416s-16.38-3.125-22.62-9.375L160 301.3L54.63 406.6C48.38 412.9 40.19 416 32 416S15.63 412.9 9.375 406.6c-12.5-12.5-12.5-32.75 0-45.25l105.4-105.4L9.375 150.6c-12.5-12.5-12.5-32.75 0-45.25s32.75-12.5 45.25 0L160 210.8l105.4-105.4c12.5-12.5 32.75-12.5 45.25 0s12.5 32.75 0 45.25l-105.4 105.4L310.6 361.4z\"></path></svg>";
    });

// 打开编辑标签卡
function editArticle(id) {
    window.parent.createTab("/article/" + id, "编辑文章", true);
}

// 删除文章
function deleteArticle(sender, id) {
    // 跳转二次验证
    window.location.href = '/login?action=verify&title=验证你的密码&back=' + encodeURIComponent('admin/article_list?delete=' + id);
}

//-------------------------------------

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