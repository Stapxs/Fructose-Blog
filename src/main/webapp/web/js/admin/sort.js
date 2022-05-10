// 获取分类列表
fetch('/api/sort/list')
    .then(res => res.json())
    .then(data => {
        if (data.code === 200) {
            const list = data.data;
            if(list.length > 0) {
                // 列表头
                const head = document.createElement('div');
                head.innerHTML = `<span>名称</span><div>文章数</div><div>操作</div>`;
                document.getElementById('sort-list').appendChild(head);
                // 遍历显示分类
                list.forEach(item => {
                    const div = document.createElement('div');
                    div.id = item.info.sort_name;
                    div.innerHTML = `<span>${item.info.sort_title}<span>${item.info.sort_name}</span></span>
                    <div>${item.count}</div>
                    <div><button class="ss-button" style="background: var(--color-main) !important;color: var(--color-font-r);">编辑</button>
                    <button class="ss-button">删除</button></div>
                    <div>`;
                    document.getElementById('sort-list').appendChild(div);
                });
            } else {
                document.getElementById("sort-list").innerHTML = '<div class="sort-empty">什么都没有 :(</div>';
            }
        }
    })
    .catch(err => {
        console.log(err);
    });

// 添加分类
function addSort() {
    // 显示加载图标
    document.getElementById("sort-send").innerHTML = '<svg style="width: 20px;fill: var(--color-font-r);" class="fa-spin" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M222.7 32.15C227.7 49.08 218.1 66.9 201.1 71.94C121.8 95.55 64 169.1 64 255.1C64 362 149.1 447.1 256 447.1C362 447.1 448 362 448 255.1C448 169.1 390.2 95.55 310.9 71.94C293.9 66.9 284.3 49.08 289.3 32.15C294.4 15.21 312.2 5.562 329.1 10.6C434.9 42.07 512 139.1 512 255.1C512 397.4 397.4 511.1 256 511.1C114.6 511.1 0 397.4 0 255.1C0 139.1 77.15 42.07 182.9 10.6C199.8 5.562 217.6 15.21 222.7 32.15V32.15z"/></svg>';
    // 获取内容
    const sortName = document.getElementById("sort-name").value;
    const sortTitle = document.getElementById("sort-title").value;
    const sortMark = document.getElementById("sort-mark").value;
    // 检查内容
    if (sortName === '' || sortTitle === '') {
        document.getElementById("sort-send").innerHTML = "请检查输入内容";
        setTimeout(() => {
            document.getElementById("sort-send").innerHTML = "添加";
        }, 1500);
        return;
    }
    // 分类名只能是英文
    if (!/^[a-zA-Z]+$/.test(sortName)) {
        document.getElementById("sort-send").innerHTML = "分类名只能是英文";
        setTimeout(() => {
            document.getElementById("sort-send").innerHTML = "添加";
        }, 3000);
        return;
    }
    // 构建 JSON
    const data = JSON.stringify({
        sort_name: sortName,
        sort_title: sortTitle,
        sort_mark: sortMark
    });
    // 提交数据
    fetch('/api/sort/add?id=' + getCookie("id") + "&token=" + getCookie("token"), {
        method: 'POST',
        body: data
    })
        .then(res => res.json())
        .then(data => {
            if(data.code === 200) {
                // 刷新页面
                location.reload();
            } else {
                console.log(data);
                document.getElementById("sort-send").innerHTML = data.str;
                setTimeout(() => {
                    document.getElementById("sort-send").innerHTML = "添加";
                }, 3000);
            }
        })
        .catch(err => {
            console.log(err);
            document.getElementById("sort-send").innerHTML = "操作失败";
            setTimeout(() => {
                document.getElementById("sort-send").innerHTML = "添加";
            }, 3000);
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
