// 获取主题配置文件
fetch('/theme/config/config.json')
    .then(response => response.json())
    .then(data => {
        const themes = data["theme-include"];
        themes.forEach(theme => {
            // 获取详细信息
            fetch('/theme/config/' + theme)
                .then(response => response.json())
                .then(data => {
                    // 创建主题元素
                    const themeView = document.createElement('div');
                    themeView.classList.add('theme-view');
                    themeView.id = data.name.toLowerCase();
                    const buttonDisabled = data.name.toLowerCase() === used_theme ? "disabled" : "onclick=\"changeTheme(this.parentNode.parentNode.parentNode.id, this)\"";
                    const buttonTitle = data.name.toLowerCase() === used_theme ? "正在使用" : "使用主题";
                    themeView.innerHTML = `<img style="${data.img === undefined ? "display:none;" : ""}" src="/images/${data.img}"><div><div>
<p>${data.title}</p><span>${data.description}</span><a>作者：${data.author}</a>
</div><div><button ${buttonDisabled} class="ss-button">${buttonTitle}</button></div></div>`;
                    document.getElementById('theme-list').appendChild(themeView);
                    // 加载完成
                    const tab = window.parent.document.getElementById("tabp-theme");
                    const button = tab.children[0].getElementsByTagName("button")[0];
                    button.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 320 512\"><path d=\"M310.6 361.4c12.5 12.5 12.5 32.75 0 45.25C304.4 412.9 296.2 416 288 416s-16.38-3.125-22.62-9.375L160 301.3L54.63 406.6C48.38 412.9 40.19 416 32 416S15.63 412.9 9.375 406.6c-12.5-12.5-12.5-32.75 0-45.25l105.4-105.4L9.375 150.6c-12.5-12.5-12.5-32.75 0-45.25s32.75-12.5 45.25 0L160 210.8l105.4-105.4c12.5-12.5 32.75-12.5 45.25 0s12.5 32.75 0 45.25l-105.4 105.4L310.6 361.4z\"></path></svg>";
                })
                .catch(error => {
                    console.log(error);
                    document.body.innerText = "页面加载失败！<br>" + error;
                });
        });
    })
    .catch(error => {
        console.log(error);
        document.body.innerText = "页面加载失败！<br>" + error;
    });

// 改变主题
function changeTheme(name, sender) {
    fetch(`/api/config/update/string/cfg_theme?id=${getCookie("id")}&token=${getCookie("token")}`, {
        method: 'POST',
        body: name
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                window.location.reload();
            } else {
                sender.innerText = "更改失败";
                setTimeout(() => {
                    sender.innerText = "使用主题";
                }, 3000);
            }
        })
        .catch(error => {
            console.log(error);
            sender.innerText = "更改失败";
            setTimeout(() => {
                sender.innerText = "使用主题";
            }, 3000);
        });
}

function openConfigPage() {
    window.location.href = '/admin/theme_config';
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

