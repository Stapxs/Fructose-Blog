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
                    themeView.innerHTML = `<img style="${data.img === undefined ? "display:none;" : ""}" src="/images/${data.img}"><div><div>
<p>${data.title}</p><span>${data.description}</span><a>作者：${data.author}</a>
</div><div><button class="ss-button">预览主题</button><button class="ss-button">使用主题</button></div></div>`;
                    document.getElementById('theme-list').appendChild(themeView);
                    // 加载完成
                    const tab = window.parent.document.getElementById("tabp-theme");
                    const button = tab.children[0].getElementsByTagName("button")[0];
                    button.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 320 512\"><path d=\"M310.6 361.4c12.5 12.5 12.5 32.75 0 45.25C304.4 412.9 296.2 416 288 416s-16.38-3.125-22.62-9.375L160 301.3L54.63 406.6C48.38 412.9 40.19 416 32 416S15.63 412.9 9.375 406.6c-12.5-12.5-12.5-32.75 0-45.25l105.4-105.4L9.375 150.6c-12.5-12.5-12.5-32.75 0-45.25s32.75-12.5 45.25 0L160 210.8l105.4-105.4c12.5-12.5 32.75-12.5 45.25 0s12.5 32.75 0 45.25l-105.4 105.4L310.6 361.4z\"></path></svg>";
                })
                .catch(error => {

                });
        });
    })
    .catch(error => {

    });
