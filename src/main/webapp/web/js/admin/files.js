// 加载文件列表
fetch("/api/article/img/list")
    .then(response => response.json())
    .then(data => {
        if(data.code === 200) {
            const body = document.getElementById("main-list")
            const list = data.data.value
            if (list.length > 0) {
                body.innerHTML = `<div><div>预览</div><a>文件大小</a><span>上传日期</span><div>操作</div></div>`
                list.forEach(item => {
                    const div = document.createElement("div")
                    div.innerHTML = `<div style="background: url(${item.file_url})"></div>
<a>${item.file_size}</a><span>${item.file_date}</span><div><button onclick="deleteFile(this, '${item.file_name}')" class="ss-button">删除</button></div>`
                    body.append(div)
                })
            } else {
                body.innerHTML = `<div class="list-empty">什么都没有 :(</div>`
            }
        }
    })
    .catch(err => {

    })
    .finally(() => {
        const tab = window.parent.document.getElementById("tabp-files");
        const button = tab.children[0].getElementsByTagName("button")[0];
        button.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 320 512\"><path d=\"M310.6 361.4c12.5 12.5 12.5 32.75 0 45.25C304.4 412.9 296.2 416 288 416s-16.38-3.125-22.62-9.375L160 301.3L54.63 406.6C48.38 412.9 40.19 416 32 416S15.63 412.9 9.375 406.6c-12.5-12.5-12.5-32.75 0-45.25l105.4-105.4L9.375 150.6c-12.5-12.5-12.5-32.75 0-45.25s32.75-12.5 45.25 0L160 210.8l105.4-105.4c12.5-12.5 32.75-12.5 45.25 0s12.5 32.75 0 45.25l-105.4 105.4L310.6 361.4z\"></path></svg>";
    })

function deleteFile(sender, name) {
    fetch("/api/article/img/delete?id=" + getCookie("id") + "&token=" + getCookie("token") + "&name=" + name, {
        method: "POST"
    })
        .then(response => response.json())
        .then(data => {
            if(data.code === 200) {
                window.location.reload()
            } else {
                console.log(data.str)
                sender.innerText = "操作失败！"
                setTimeout(() => {
                    sender.innerHTML = "删除";
                }, 3000);
            }
        })
        .catch(err => {
            console.log(err)
            sender.innerText = "操作失败！"
            setTimeout(() => {
                sender.innerHTML = "删除";
            }, 3000);
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