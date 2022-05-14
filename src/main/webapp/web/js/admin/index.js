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
function closeTab(sender, openSidebar) {
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
    if(openSidebar) {
        changeLeftList()
    }
}

// 开关侧边栏
function changeLeftList() {
    const list = document.getElementById('left-list')
    const button = document.getElementById('left-list-button')
    if(list.style.width !== "0px") {
        list.style.width = "0px"
        button.style.display = "block"
        setTimeout(() => {
            list.style.display = "none"
        }, 200)
    } else {
        list.style.display = "block"
        setTimeout(() => {
            list.style.width = "300px"
            button.style.display = "none"
        }, 100)
    }
}

// 添加标签卡
function addTab(sender, closeSidebar) {
    const page = sender.dataset.page
    const title = sender.children[1].innerText
    createTab(page, title, closeSidebar)
}
function createTab(page, title, closeSidebar) {
    if(document.getElementById("tabp-" + page) !== null) {
        document.getElementById("tabp-" + page).children[0].click()
        return
    }

    let id_name = page
    if(id_name.indexOf('/') !== -1) {
        id_name = id_name.substring(0, id_name.lastIndexOf('/'))
    }
    if(id_name.indexOf('/') === 0) {
        id_name = id_name.substring(1)
    }
    const div = document.createElement('div')
    div.className = 'tab-pane fade'
    div.id = id_name
    div.setAttribute('role', 'tabpanel')
    div.setAttribute('aria-labelledby', id_name + '-tab')
    // 创建 iframe
    const iframe = document.createElement('iframe')
    iframe.src = "/admin/" + page
    div.innerHTML = iframe.outerHTML
    // 添加元素
    document.getElementById("myTabContent").append(div)

    // 创建标签卡
    const li = document.createElement('li')
    li.className = 'nav-item'
    li.id = "tabp-" + id_name
    let html = '<a class="nav-link" id="{name}-tab" data-toggle="tab" href="#{name}" role="tab" aria-controls="{name}" aria-selected="false">\n' +
        '            {text}\n' +
        '            <button onclick="{close}">\n' +
        '                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512"><path d="M310.6 361.4c12.5 12.5 12.5 32.75 0 45.25C304.4 412.9 296.2 416 288 416s-16.38-3.125-22.62-9.375L160 301.3L54.63 406.6C48.38 412.9 40.19 416 32 416S15.63 412.9 9.375 406.6c-12.5-12.5-12.5-32.75 0-45.25l105.4-105.4L9.375 150.6c-12.5-12.5-12.5-32.75 0-45.25s32.75-12.5 45.25 0L160 210.8l105.4-105.4c12.5-12.5 32.75-12.5 45.25 0s12.5 32.75 0 45.25l-105.4 105.4L310.6 361.4z"/></svg>\n' +
        '            </button>\n' +
        '        </a>'
    html = html.replaceAll('{name}', id_name)
    html = html.replace('{text}', title)
    if(closeSidebar) {
        html = html.replace('{close}', 'closeTab(this, true)')
    } else {
        html = html.replace('{close}', 'closeTab(this)')
    }
    li.innerHTML = html
    // 添加元素
    document.getElementById("myTab").append(li)
    // 点击创建的标签卡
    li.children[0].click()
    // 显示加载动画
    const button = li.children[0].getElementsByTagName("button")[0];
    button.innerHTML = "<svg style=\"margin-left: -4px;width: 20px;fill: var(--color-font-r);\" class=\"fa-spin\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 512 512\"><path d=\"M222.7 32.15C227.7 49.08 218.1 66.9 201.1 71.94C121.8 95.55 64 169.1 64 255.1C64 362 149.1 447.1 256 447.1C362 447.1 448 362 448 255.1C448 169.1 390.2 95.55 310.9 71.94C293.9 66.9 284.3 49.08 289.3 32.15C294.4 15.21 312.2 5.562 329.1 10.6C434.9 42.07 512 139.1 512 255.1C512 397.4 397.4 511.1 256 511.1C114.6 511.1 0 397.4 0 255.1C0 139.1 77.15 42.07 182.9 10.6C199.8 5.562 217.6 15.21 222.7 32.15V32.15z\"/></svg>";
    // 关闭侧边栏
    if(closeSidebar) {
        changeLeftList()
    }
}