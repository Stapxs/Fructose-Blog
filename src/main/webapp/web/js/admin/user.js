// 获取用户信息
fetch('/api/account/info/' + getCookie('id') + "?token=" + getCookie('token'), {
    method: 'POST'
}).then(response => response.json())
    .then(data => {
    if(data.code === 200) {
        // 设置用户名
        document.getElementById('username').innerText = data.data.user_name;
        document.getElementById('nickname').innerText = data.data.user_name;
        if(data.data.user_nick === undefined) {
            document.getElementById('username').style.display = 'none';
        } else  {
            document.getElementById('nickname').innerText = data.data.user_nick;
        }
        // 填充输入框
        if(data.data.user_nick !== undefined) {
            document.getElementById('nick_input').value = data.data.user_nick;
            document.getElementById('nick_input').dataset.value = data.data.user_nick;
        }
        if(data.data.user_link !== undefined) {
            document.getElementById('link_input').value = data.data.user_link;
            document.getElementById('link_input').dataset.value = data.data.user_link;
        }
        document.getElementById('mail_input').value = data.data.user_mail;
        document.getElementById('mail_input').dataset.value = data.data.user_mail;
        // 获取头像链接
        fetch('/api/account/avatar/' + getCookie('id'))
            .then(response => response.json())
            .then(data => {
                if(data.code === 200) {
                    if(data.data.cross) {
                        // TODO 添加 referrer
                    }
                    document.getElementById('avatar').src = data.data.url;
                }
            })
            .catch(console.log);
    }
}).catch(console.log);

// 获取设置档
fetch('/api/account/config/' + getCookie('id') + "?token=" + getCookie('token'), {
    method: 'POST'
}).then(response => response.json())
    .then(data => {
        if(data.code === 200) {
            let json = data.str;
            // URL 解码
            json = decodeURIComponent(json);
            json = JSON.parse(json);
            for(let key in json) {
                if(json[key] === true) {
                    document.getElementById(key).click();
                }
            }
        }
    })
    .catch(console.log);

// 其他特殊操作
if(getQueryVariable("changePwd")) {
    document.getElementById("changePwdAcc").innerHTML = '<svg style="width: 20px;fill: var(--color-font-r);" class="fa-spin" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M222.7 32.15C227.7 49.08 218.1 66.9 201.1 71.94C121.8 95.55 64 169.1 64 255.1C64 362 149.1 447.1 256 447.1C362 447.1 448 362 448 255.1C448 169.1 390.2 95.55 310.9 71.94C293.9 66.9 284.3 49.08 289.3 32.15C294.4 15.21 312.2 5.562 329.1 10.6C434.9 42.07 512 139.1 512 255.1C512 397.4 397.4 511.1 256 511.1C114.6 511.1 0 397.4 0 255.1C0 139.1 77.15 42.07 182.9 10.6C199.8 5.562 217.6 15.21 222.7 32.15V32.15z"/></svg>';
    // 提交密码修改
    let pwd = getQueryVariable("changePwd");
    pwd = pwd.replaceAll("_", "=");
    try {
        const httpRequest = new XMLHttpRequest();
        httpRequest.open("POST", "/api/account/password/", true);
        httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        httpRequest.send("id=" + getCookie("id") + "&str=" + pwd);

        httpRequest.onreadystatechange = function () {
            if (httpRequest.readyState === 4 && httpRequest.status === 200) {
                if (self.frameElement && self.frameElement.tagName === "IFRAME") {
                    // 让主网页跳转到登陆页面
                    window.parent.location.href = "/login";
                }
            } else if (httpRequest.readyState === 4) {
                const json = JSON.parse(httpRequest.responseText);
                document.getElementById("changePwdAcc").innerText = json.str;
                setTimeout(() => {
                    document.getElementById("changePwdAcc").innerHTML = '确定';
                }, 1500);
            }
        };
    } catch (e) {
        document.getElementById("changePwdAcc").innerText = "操作失败";
        setTimeout(() => {
            document.getElementById("changePwdAcc").innerHTML = '确定';
        }, 1500);
    }
}

// -------------------------------------------------

// 刷新基本信息
function updateBaseInfo(sender) {
    const name = sender.dataset.name;
    const value = sender.value;
    // 输入不能是空白字符或者空格，也不能和原始数据一样
    if(value.trim() === '' || value === sender.dataset.value) {
        return;
    }
    // 提交数据（不需要检查是否成功）
    fetch('/api/account/info/set/' + name + '/' + getCookie('id') +
        "?token=" + getCookie('token') + "&value=" + value, {
        method: 'POST'
    }).then(() => {
        // 刷新页面
        location.reload();
    }).catch(console.log);
}

// 提交设置项
function submitSetting(sender) {
    // 显示加载图标
    sender.innerHTML = '<svg style="width: 20px;fill: var(--color-font-r);" class="fa-spin" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M222.7 32.15C227.7 49.08 218.1 66.9 201.1 71.94C121.8 95.55 64 169.1 64 255.1C64 362 149.1 447.1 256 447.1C362 447.1 448 362 448 255.1C448 169.1 390.2 95.55 310.9 71.94C293.9 66.9 284.3 49.08 289.3 32.15C294.4 15.21 312.2 5.562 329.1 10.6C434.9 42.07 512 139.1 512 255.1C512 397.4 397.4 511.1 256 511.1C114.6 511.1 0 397.4 0 255.1C0 139.1 77.15 42.07 182.9 10.6C199.8 5.562 217.6 15.21 222.7 32.15V32.15z"/></svg>';
    // 提交数据
    let data = '';
    if(window.userConfig !== undefined) {
        data = JSON.stringify(window.userConfig);
    }
    // 杀了特殊符号
    // URL 编码
    data = encodeURIComponent(data);
    fetch('/api/account/info/set/userinfo-user_config/' + getCookie('id') +
        "?token=" + getCookie('token') + "&value=" + data, {
        method: 'POST'
    }).then(() => {
        sender.innerHTML = '保存';
    }).catch(err => {
        console.log(err);
        sender.innerHTML = '操作失败';
        setTimeout(() => {
            sender.innerHTML = '保存';
        }, 1500);
    });
}

// 修改密码
function changePassword(sender) {
    // 显示加载图标
    sender.innerHTML = '<svg style="width: 20px;fill: var(--color-font-r);" class="fa-spin" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M222.7 32.15C227.7 49.08 218.1 66.9 201.1 71.94C121.8 95.55 64 169.1 64 255.1C64 362 149.1 447.1 256 447.1C362 447.1 448 362 448 255.1C448 169.1 390.2 95.55 310.9 71.94C293.9 66.9 284.3 49.08 289.3 32.15C294.4 15.21 312.2 5.562 329.1 10.6C434.9 42.07 512 139.1 512 255.1C512 397.4 397.4 511.1 256 511.1C114.6 511.1 0 397.4 0 255.1C0 139.1 77.15 42.07 182.9 10.6C199.8 5.562 217.6 15.21 222.7 32.15V32.15z"/></svg>';
    // 获取密码
    const newPassword = document.getElementById('new_password').value;
    const newPassword2 = document.getElementById('new_password2').value;
    // 检查密码
    if(newPassword !== newPassword2 && newPassword !== '') {
        sender.innerHTML = '两次密码不一致';
        setTimeout(() => {
            sender.innerHTML = '确定';
        }, 1500);
        return;
    }
    // 获取 key
    fetch("/api/account/key/" + getCookie('name'))
        .then(response => response.json())
        .then(data => {
            if(data.code === 200) {
                let key = data.data.key
                key = "-----BEGIN PUBLIC KEY-----" + key + "-----END PUBLIC KEY-----"
                // 加密密码
                const encrypt = new JSEncrypt()
                encrypt.setPublicKey(key)
                const encrypted = encrypt.encrypt(newPassword)
                // 跳转到二次验证
                window.location.href = '/login?action=verify&notice=' + encodeURIComponent(data.data.key) + '&title=验证你的密码&back=' + encodeURIComponent('admin/user?changePwd=' + encrypted);
            } else {
                sender.innerHTML = '获取验证密钥失败！';
                setTimeout(() => {
                    sender.innerHTML = '确定';
                }, 1500);
            }
        })
        .catch(function (e) {
            console.error(e)
            sender.innerHTML = '请求验证密钥失败！';
            setTimeout(() => {
                sender.innerHTML = '确定';
            }, 1500);
        })
}

// -------------------------------------------------

// 修改全局存储的 JSON 数据
function setJSONValue(name, key, value) {
    if(window[name] === undefined) {
        window[name] = {};
    }
    window[name][key] = value;
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