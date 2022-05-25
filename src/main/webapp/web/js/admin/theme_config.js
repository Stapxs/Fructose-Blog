// 加载主题设置列表
fetch(`/theme/config/${used_theme}.json`)
.then(response => response.json())
.then(data => {
    const list = document.getElementById("config-list");
    const configs = data.config_site;
    configs.forEach(item => {
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
    })
})
    .catch(error => {
        console.log(error);
        document.body.innerHTML = "加载失败<br>" + error;
    });
// 加载设置项的值
fetch("/api/config/string/theme_cfgs")
.then(response => response.json())
.then(data => {
    if(data.str !== undefined) {
        const configs = JSON.parse(data.str);
        console.log(configs);
        for(let key in configs) {
            const input = document.getElementById(key);
            if(input !== null) {
                if(input.type === "checkbox") {
                    input.checked = configs[key];
                } else {
                    input.value = configs[key];
                }
            }
        }
    }
    // 加载完成
    const tab = window.parent.document.getElementById("tabp-theme");
    const button = tab.children[0].getElementsByTagName("button")[0];
    button.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 320 512\"><path d=\"M310.6 361.4c12.5 12.5 12.5 32.75 0 45.25C304.4 412.9 296.2 416 288 416s-16.38-3.125-22.62-9.375L160 301.3L54.63 406.6C48.38 412.9 40.19 416 32 416S15.63 412.9 9.375 406.6c-12.5-12.5-12.5-32.75 0-45.25l105.4-105.4L9.375 150.6c-12.5-12.5-12.5-32.75 0-45.25s32.75-12.5 45.25 0L160 210.8l105.4-105.4c12.5-12.5 32.75-12.5 45.25 0s12.5 32.75 0 45.25l-105.4 105.4L310.6 361.4z\"></path></svg>";
})
    .catch(error => {
        console.log(error);
        document.body.innerHTML = "加载失败<br>" + error;
    });

function saveConfig(sender) {
    const configs = document.getElementById("config-list").getElementsByTagName("input");
    let config = {};
    for(let i = 0; i < configs.length; i++) {
        const item = configs[i];
        if(item.type === "checkbox") {
            config[item.id] = item.checked;
        } else {
            if(item.value && item.value !== "") {
                config[item.id] = item.value;
            }
        }
    }
    console.log(config);
    // 提交
    fetch(`/api/config/update/string/theme_cfgs?id=${getCookie("id")}&token=${getCookie("token")}`, {
        method: 'POST',
        body: JSON.stringify(config)
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                window.location.reload();
            } else {
                sender.innerText = "更改失败";
                setTimeout(() => {
                    sender.innerText = "保存";
                }, 3000);
            }
        })
        .catch(error => {
            console.log(error);
            sender.innerText = "更改失败";
            setTimeout(() => {
                sender.innerText = "保存";
            }, 3000);
        });
}

function backTheme() {
    window.location.href = '/admin/theme';
    // 加载图标
    const tab = window.parent.document.getElementById("tabp-theme");
    const button = tab.children[0].getElementsByTagName("button")[0];
    button.innerHTML = "<svg style=\"margin-left: -4px;width: 20px;fill: var(--color-font-r);\" class=\"fa-spin\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 512 512\"><path d=\"M222.7 32.15C227.7 49.08 218.1 66.9 201.1 71.94C121.8 95.55 64 169.1 64 255.1C64 362 149.1 447.1 256 447.1C362 447.1 448 362 448 255.1C448 169.1 390.2 95.55 310.9 71.94C293.9 66.9 284.3 49.08 289.3 32.15C294.4 15.21 312.2 5.562 329.1 10.6C434.9 42.07 512 139.1 512 255.1C512 397.4 397.4 511.1 256 511.1C114.6 511.1 0 397.4 0 255.1C0 139.1 77.15 42.07 182.9 10.6C199.8 5.562 217.6 15.21 222.7 32.15V32.15z\"/></svg>";
}

// ---------------------------------------------------------------

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