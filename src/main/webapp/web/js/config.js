// 下一步跳转
function nextStep(sender) {
    const id = Number(sender.dataset.id) + 1;
    window.location.href='#list-item-' + id;
}

// 站点设置修改事件
function selectIcon(input) {
    const blob = input.files[0]
    if(blob.type.indexOf("image/") >= 0 && blob.size !== 0) {
        if(blob.size < 3145728) {
            // 转换为 Base64
            const reader = new FileReader();
            reader.readAsDataURL(blob);
            reader.onloadend = function() {
                const base64data = reader.result;
                // 显示图片
                document.getElementById("site-icon").innerHTML = `<img src="${base64data}">`;
                // 缓存
                window.siteConfig['fb_icon'] = base64data;
            }
        } else {
            // 图片过大
        }
    } else {
        // 不是图片
    }
}
function siteCChange(input) {
    const name = input.name;
    const value = input.value;
    document.getElementById(name).innerText = value;
    window.siteConfig[name] = value;
}
function siteCBChange(input) {
    const name = input.name;
    window.siteConfig[name] = input.checked;
}

// 站点设置确认
function siteConfirm(sender) {
    // 替换图标
    document.getElementById("base-set").innerHTML = '<svg style="width: 20px;" class="fa-spin" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M222.7 32.15C227.7 49.08 218.1 66.9 201.1 71.94C121.8 95.55 64 169.1 64 255.1C64 362 149.1 447.1 256 447.1C362 447.1 448 362 448 255.1C448 169.1 390.2 95.55 310.9 71.94C293.9 66.9 284.3 49.08 289.3 32.15C294.4 15.21 312.2 5.562 329.1 10.6C434.9 42.07 512 139.1 512 255.1C512 397.4 397.4 511.1 256 511.1C114.6 511.1 0 397.4 0 255.1C0 139.1 77.15 42.07 182.9 10.6C199.8 5.562 217.6 15.21 222.7 32.15V32.15z"/></svg>'
    // 验证数据
    const siteName = document.getElementById("fb_name").innerText;
    const siteDesc = document.getElementById("fb_desc").innerText;
    window.siteConfig['fb_name'] = siteName;
    window.siteConfig['fb_desc'] = siteDesc;
    if(siteName.length === 0 || siteDesc.length === 0) {
        showError("请填写完整");
        document.getElementById("base-set").innerHTML = "确定"
        return;
    }
    if(siteName.length > 10 || siteDesc.length > 10) {
        showError("站点名称或描述过长");
        document.getElementById("base-set").innerHTML = "确定"
        return;
    }
    // POST 提交数据
    const json = JSON.stringify(window.siteConfig);
    console.log(json);
    fetch('api/site/config', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + window.token
        },
        body: json
    }).then(res => res.json())
    .then(data => {
        if(data.code === 200) {
            // 翻页
            nextStep(sender);
            document.getElementById("base-set").innerHTML = "确定"
        } else {
            showError(data.data);
            document.getElementById("base-set").innerHTML = "确定"
        }
    }).catch(err => {
        console.log(err);
        showError(err);
        document.getElementById("base-set").innerHTML = "确定"
    });
}

// 显示错误
function showError(msg) {
    document.getElementById("err-bar-txt").innerText = msg;
    document.getElementById("err-bar").style.width = "50%";
    setTimeout(() => {
        document.getElementById("err-bar").style.width = "0";
    }, 4000);
}