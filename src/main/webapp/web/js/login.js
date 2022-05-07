// 获取 cookies
const cookies = document.cookie.split('; ')
let idIn = -1
let tok = ''
cookies.forEach(function (cookie) {
    if(cookie.indexOf("id") >= 0) {
        idIn = cookie.split('=')[1]
    }
    if(cookie.indexOf("token") >= 0) {
        tok = cookie.split('=')[1]
    }
})
// if(idIn !== -1 && tok !== '') {
//     // 验证登录
//     showErr(true, "加载中")
//     loading(true)
//     try {
//         const httpRequest = new XMLHttpRequest();
//         httpRequest.open("POST", "/acc/verifyLogin", true);
//         httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
//         httpRequest.send("id=" + idIn + "&token=" + tok);
//
//         httpRequest.onreadystatechange = function () {
//             if (httpRequest.readyState === 4 && httpRequest.status === 200) {
//                 if(getQueryVariable("back") !== false) {
//                     window.location.href = "/" + getQueryVariable("back")
//                 } else {
//                     window.location.href = "/Center"
//                 }
//             } else if(httpRequest.status === 403 || httpRequest.status === 500) {
//                 showErr(true, "验证登录失败，请重新登陆！")
//             }
//         };
//     } catch (e) {
//         console.error(e)
//         showErr(true, "获取信息失败！(2)")
//     }
// }

function loginAcc() {
    showErr(false)
    loading(true)

    const name = document.getElementById("inuname").value;
    const pwd = document.getElementById("inupwd").value;

    // 验证 Name 有效性
    const uPattern = /^[a-zA-Z0-9_-]{4,16}$/;
    if(!uPattern.test(name)) {
        showErr(true, "用户名无效")
        return false
    }

    // 获取 key
    fetch("/api/account/key/" + name)
        .then(response => response.json())
        .then(data => {
            if(data.code === 200) {
                const id = data.data.id
                let key = data.data.key
                key = "-----BEGIN PUBLIC KEY-----" + key + "-----END PUBLIC KEY-----"
                // 加密密码
                const encrypt = new JSEncrypt()
                encrypt.setPublicKey(key)
                const encrypted = encrypt.encrypt(pwd)
                // 请求登录接口
                try {
                    const httpRequest = new XMLHttpRequest();
                    httpRequest.open("POST", "api/account/login", true);
                    httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                    httpRequest.send("id=" + id + "&str=" + encrypted);

                    httpRequest.onreadystatechange = function () {
                        if (httpRequest.readyState === 4 && httpRequest.status === 200) {
                            const json = JSON.parse(httpRequest.responseText)
                            console.log("登陆成功")
                            // 保存 cookie
                            const exp = new Date();
                            exp.setTime(exp.getTime() + 50 * 60 * 1000);
                            document.cookie = "id=" + json.data.id + "; expires=" + exp.toGMTString() + "; path=/";
                            document.cookie = "token=" + json.data.key + "; expires=" + exp.toGMTString() + "; path=/";
                            // // 获取用户昵称
                            // fetch("/acc/info/getNick/" + id)
                            //     .then(response => response.json())
                            //     .then(data => {
                            //         if (data.message !== "没有找到这个账号！") {
                            //             document.cookie = "name=" + data.message + "; expires=" + exp.toGMTString() + "; path=/";
                            //         } else {
                            //             document.cookie = "name=" + name + "; expires=" + exp.toGMTString() + "; path=/";
                            //         }
                            //     })
                            //     .catch(function (e) {
                            //         console.error(e)
                            //         showErr(true, "获取信息失败！(1)")
                            //     })
                            // 跳转页面
                            if (getQueryVariable("back") !== false) {
                                window.location.href = "/" + getQueryVariable("back")
                            } else if(get_back != undefined) {
                                window.location.href = "/" + get_back
                            } else {
                                window.location.href = "/admin"
                            }
                        } else if (httpRequest.readyState === 4 && (
                            Math.floor(httpRequest.status / 100) === 4 || Math.floor(httpRequest.status / 100) === 5)) {
                            const json = JSON.parse(httpRequest.responseText)
                            showErr(true, json.str)
                        }
                    };
                } catch (e) {
                    console.error(e)
                    showErr(true, "登录失败！")
                }
            } else {
                showErr(true, "获取验证密钥失败！")
            }
        })
        .catch(function (e) {
            console.error(e)
            showErr(true, "请求验证密钥失败！")
        })

    return false
}

// 注册账户
function regAcc() {
    const name = document.getElementById("reg-inuname").value;
    const mail = document.getElementById("reg-inumail").value;
    const pwd = document.getElementById("reg-inupwd").value;
    // 验证数据
    if(name === "" || mail === "" || pwd === "") {
        showErr(true, "请填写内容！")
        return false
    }

    // 请求注册接口
    try {
        const httpRequest = new XMLHttpRequest();
        httpRequest.open("POST", "api/account/register", true);
        httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        httpRequest.send("name=" + name + "&email=" + mail + "&password=" + pwd);

        httpRequest.onreadystatechange = function () {
            if (httpRequest.readyState === 4 && httpRequest.status === 200) {
                const json = JSON.parse(httpRequest.responseText)
                console.log("注册成功！")
                // 跳回登录面板
                backRegPan()
                showErr(true, "注册成功，请登录！")
            } else if (httpRequest.readyState === 4 &&
                (httpRequest.status === 409 || httpRequest.status === 500 || httpRequest.status === 400)) {
                const json = JSON.parse(httpRequest.responseText)
                showErr(true, json.str, true)
            }
        };
    } catch (e) {
        console.error(e)
        showErr(true, "注册失败！")
    }

    return false
}

function showErr(isShow, info, reg) {
    const mainCard = document.getElementById("pan-main")
    let card = document.getElementById("err-card")
    let txt = document.getElementById("err-info")
    if(reg !== undefined) {
        card = document.getElementById("regerr-card")
        txt = document.getElementById("regerr-info")
    }
    if(isShow) {
        loading(false)
        if(getComputedStyle(mainCard, null).marginTop !== "0px") {
            mainCard.style.marginTop = "calc(50vh - 310px)"
        }
        card.style.visibility = "visible"
        txt.innerText = info
    } else {
        if(getComputedStyle(mainCard, null).marginTop !== "0px") {
            mainCard.style.marginTop = "calc(50vh - 260px)"
        }
        card.style.visibility = "hidden"
    }
}

function loading(isLoad) {
    const button = document.getElementById("go-btn")
    const icon = document.getElementById("go-icon")
    const path = document.getElementById("go-icon-path")
    if(isLoad) {
        button.disabled = true
        path.setAttribute("d", "M487.4 315.7l-42.6-24.6c4.3-23.2 4.3-47 0-70.2l42.6-24.6c4.9-2.8 7.1-8.6 5.5-14-11.1-35.6-30-67.8-54.7-94.6-3.8-4.1-10-5.1-14.8-2.3L380.8 110c-17.9-15.4-38.5-27.3-60.8-35.1V25.8c0-5.6-3.9-10.5-9.4-11.7-36.7-8.2-74.3-7.8-109.2 0-5.5 1.2-9.4 6.1-9.4 11.7V75c-22.2 7.9-42.8 19.8-60.8 35.1L88.7 85.5c-4.9-2.8-11-1.9-14.8 2.3-24.7 26.7-43.6 58.9-54.7 94.6-1.7 5.4.6 11.2 5.5 14L67.3 221c-4.3 23.2-4.3 47 0 70.2l-42.6 24.6c-4.9 2.8-7.1 8.6-5.5 14 11.1 35.6 30 67.8 54.7 94.6 3.8 4.1 10 5.1 14.8 2.3l42.6-24.6c17.9 15.4 38.5 27.3 60.8 35.1v49.2c0 5.6 3.9 10.5 9.4 11.7 36.7 8.2 74.3 7.8 109.2 0 5.5-1.2 9.4-6.1 9.4-11.7v-49.2c22.2-7.9 42.8-19.8 60.8-35.1l42.6 24.6c4.9 2.8 11 1.9 14.8-2.3 24.7-26.7 43.6-58.9 54.7-94.6 1.5-5.5-.7-11.3-5.6-14.1zM256 336c-44.1 0-80-35.9-80-80s35.9-80 80-80 80 35.9 80 80-35.9 80-80 80z")
        icon.style.marginLeft = "0"
        icon.style.marginTop = "0"
        icon.style.width = "30px"
        icon.style.animation = "fa-spin 2s infinite linear";
    } else {
        button.disabled = false
        path.setAttribute("d", "M224.3 273l-136 136c-9.4 9.4-24.6 9.4-33.9 0l-22.6-22.6c-9.4-9.4-9.4-24.6 0-33.9l96.4-96.4-96.4-96.4c-9.4-9.4-9.4-24.6 0-33.9L54.3 103c9.4-9.4 24.6-9.4 33.9 0l136 136c9.5 9.4 9.5 24.6.1 34z")
        icon.style.marginLeft = "13px"
        icon.style.marginTop = "3px"
        icon.style.width = "40px"
        icon.style.animation = "";
    }
}

function showRegPan() {
    showErr(false)
    document.getElementById('login-pan').classList.remove('login-box-1')
    document.getElementById('login-pan').classList.add('login-box-2')
    document.getElementById('login-body').classList.remove('login-body-1')
    document.getElementById('login-body').classList.add('login-body-2')
    document.getElementById('reg-pan').classList.remove('reg-box-1')
    document.getElementById('reg-pan').classList.add('reg-box-2')

    document.getElementById('err-card').style.display = 'none'

    setTimeout(() => {
        document.getElementById('login-body').style.opacity = '1'
        document.getElementById('back-button').style.display = 'initial'
    }, 300)
}

function backRegPan() {
    showErr(false, "", true)
    document.getElementById('login-pan').classList.remove('login-box-2')
    document.getElementById('login-pan').classList.add('login-box-1')
    document.getElementById('login-body').classList.remove('login-body-2')
    document.getElementById('login-body').classList.add('login-body-1')
    document.getElementById('reg-pan').classList.remove('reg-box-2')
    document.getElementById('reg-pan').classList.add('reg-box-1')

    document.getElementById('back-button').style.display = 'none'
    document.getElementById('err-card').style.display = 'block'
}