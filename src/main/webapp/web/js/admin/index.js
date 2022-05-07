// 覆写展开事件
function foldChange(sender) {
    const svg = sender.getElementsByTagName('svg');
    const div = sender.getElementsByTagName('header')[0].getElementsByTagName('div');
    if(svg[0].style.transform === 'rotate(-90deg)') {
        svg[0].style.transform = 'rotate(90deg)'
    } else {
        svg[0].style.transform = 'rotate(-90deg)'
    }
    if(div[0].style.transform === 'scaleY(0)') {
        div[0].style.transform = 'scaleY(1)'
    } else {
        div[0].style.transform = 'scaleY(0)'
    }
}

// 关闭标签卡
function closeTab(sender) {
    let a = sender.parentNode
    const div = document.getElementById(a.href.substring(a.href.indexOf('#') + 1))
    a = a.parentNode
    const farther = a.parentNode
    a.style.transform = "scaleX(0)"
    setTimeout(() => {
        // 删除需要关闭的元素
        farther.removeChild(a)
        div.parentNode.removeChild(div)
        setTimeout(() => {
            // 点击第一个元素（除了第一个菜单按钮）
            if(farther.children.length > 1) {
                farther.children[1].children[0].click()
            }
        }, 100)
    }, 100)
}

// 开关侧边栏
function changeLeftList() {
    const list = document.getElementById('left-list')
    const button = document.getElementById('left-list-button')
    if(list.style.width !== "0px") {
        list.style.width = "0px"
        list.style.marginLeft = "-60px"
        list.style.transform = "translateX(-300px)"
        button.style.display = "block"
    } else {
        list.style.width = "300px"
        list.style.marginLeft = "0px"
        list.style.transform = "translateX(0px)"
        button.style.display = "none"
    }
}

function addTab(sender) {
    const page = sender.dataset.page
    const title = sender.children[1].innerText

    if(document.getElementById("tabp-" + page) !== null) {
        document.getElementById("tabp-" + page).children[0].click()
        return
    }

    // 创建显示区域
    const div = document.createElement('div')
    div.className = 'tab-pane fade'
    div.id = page
    div.setAttribute('role', 'tabpanel')
    div.setAttribute('aria-labelledby', page + '-tab')
    // 创建 iframe
    const iframe = document.createElement('iframe')
    iframe.src = "/admin/" + page
    div.innerHTML = iframe.outerHTML
    // 添加元素
    document.getElementById("myTabContent").append(div)

    // 创建标签卡
    const li = document.createElement('li')
    li.className = 'nav-item'
    li.id = "tabp-" + page
    let html = '<a class="nav-link" id="{name}-tab" data-toggle="tab" href="#{name}" role="tab" aria-controls="{name}" aria-selected="false">\n' +
        '            {text}\n' +
        '            <button onclick="closeTab(this)">\n' +
        '                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512"><path d="M310.6 361.4c12.5 12.5 12.5 32.75 0 45.25C304.4 412.9 296.2 416 288 416s-16.38-3.125-22.62-9.375L160 301.3L54.63 406.6C48.38 412.9 40.19 416 32 416S15.63 412.9 9.375 406.6c-12.5-12.5-12.5-32.75 0-45.25l105.4-105.4L9.375 150.6c-12.5-12.5-12.5-32.75 0-45.25s32.75-12.5 45.25 0L160 210.8l105.4-105.4c12.5-12.5 32.75-12.5 45.25 0s12.5 32.75 0 45.25l-105.4 105.4L310.6 361.4z"/></svg>\n' +
        '            </button>\n' +
        '        </a>'
    html = html.replaceAll('{name}', page)
    html = html.replace('{text}', title)
    li.innerHTML = html
    // 添加元素
    document.getElementById("myTab").append(li)
    // 点击创建的标签卡
    li.children[0].click()
}