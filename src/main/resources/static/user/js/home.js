var PATHPREFIX = "/user";
/**
 * jq部分
 */
$(function () {
    //注册新用户弹出层动画
    $('#reg-close,#regist').click(function (e) {
        e.preventDefault();  //阻止默认单击事件
        console.log("点击了注册按钮")
        $('#reg-popup').toggleClass('show');
    });

    //反馈弹出层动画
    $('#close,#feedback').click(function (e) {
        if (hasLogin()) {
            e.preventDefault();  //阻止默认单击事件
            $('#popup').toggleClass('show');
        } else {
            showPopup("<span id='popup-login-title'>小主，要登录才能反馈哦<br>(,,・ω・,,)</span><a href='/user/loginpage' id='popup-login'>登录</a><a id='popup-cancel'>取消</a>", 200, 200);
        }
    });

    //初始化页面时，隐藏Top按钮
    if ($(window).scrollTop() < 100) {
        $("#uptoTop").fadeOut(1);
    }

    $(window).scroll(function () {
        if ($(window).scrollTop() > 100) {
            //当页面起点距离窗口大于100时，淡入
            $("#uptoTop").fadeIn(1000);
        } else {
            //当页面起点距离窗口小于100时，淡出
            $("#uptoTop").fadeOut(1000);
        }
    });
    //跳转到顶部（动画）
    $("#uptoTop").click(function () {
        var dis = $(window).scrollTop();
        $("body,html").animate({scrollTop: 0}, 300);
        return false;
    });

});

/**
 * 鼠标悬浮通过ajax查询用户详细信息并显示
 */
//当前登录用户
var user = null;

function showUserDetail() {
    var userinfo = document.querySelector("#user");
    if (userinfo != null) {
        var userDetail = document.querySelector('#user_detail');

        var profile = document.querySelector("#profile");
        var profile_detail = document.querySelector("#profile-detail");
        var loginUserNickname = document.querySelectorAll(".loginUserNickname");
        var experience = document.querySelector("#experience");

        findUser();
        //鼠标悬浮
        userinfo.addEventListener('mouseover', function (e) {
            userDetail.style.display = 'block';
            //判断鼠标是否已经悬浮，放置重复发送请求
            let event = e || event;        //兼容处理
            let from = event.fromElement || event.relatedTarget;
            if (from && this.contains(from)) {      //如果在里面则返回
                return;
            }
            findUser();
        }, false);
        //鼠标移开
        userinfo.addEventListener('mouseout', function (e) {
            userDetail.style.display = 'none';
        }, false);
    }

    //获取用户
    function findUser() {
        //向后台发送ajax请求获取登录用户详细信息
        $.ajax({
            type: "GET",
            url: PRO_NAME + "/user/userDetail",
            dataType: "json",
            success: function (data) {
                //获得登录用户
                user = JSON.parse(data);
                fillData();
            },
            async: true
        });
    }

    //填充数据
    function fillData() {
        profile.src = "/" + user.profilePath;
        profile_detail.src = "/" + user.profilePath;
        for (let i = 0; i < loginUserNickname.length; i++) {
            loginUserNickname[i].innerHTML = user.nickname;
        }
        experience.innerHTML = "经验 " + user.experience;
    }
}

/**
 * 收藏夹
 */
function showFavoritesContent() {
    var favorites = document.querySelector("#favorites");
    if (favorites != null) {
        var favoritesContent = document.querySelector("#favorites-content");
        var favoritesContentTemp = "";

        favorites.addEventListener("mouseover", function (e) {
            favoritesContent.style.display = 'block';
            //判断鼠标是否已经悬浮，防止重复发送请求
            let event = e || event;        //兼容处理
            let from = event.fromElement || event.relatedTarget;
            if (from && this.contains(from)) {      //如果在里面则返回
                return;
            }
            loaded(favoritesContent);
            loading(favoritesContent);
            $.ajax({
                type: "GET",
                url: PRO_NAME + "/user/findFavoritesLimit",
                dataType: "json",
                success: function (data) {
                    loaded(favoritesContent);
                    if (!isnull(data)) {
                        var favoritesList = JSON.parse(data);
                        favoritesContentTemp = "";
                        for (let i = 0; i < favoritesList.length; i++) {
                            favoritesContentTemp = favoritesContentTemp +
                                "<a class='favorites-items' href='/user/note?noteId=" + favoritesList[i].articleId + "'><span title='" + favoritesList[i].authorNickname + "'>" + favoritesList[i].authorNickname + "</span><span title='" + favoritesList[i].articleTitle + "'>" + favoritesList[i].articleTitle + "</span></a>";
                        }
                        favoritesContent.innerHTML = "<a href='/user/myFavorites' id='more-favorite'>更多</a>" + favoritesContentTemp;

                        console.log("收藏夹不为空");
                    } else {
                        favoritesContent.innerHTML = "<span class='iconfont icon-zhaobudaojieguo'></span><span id='no-favorite'>收藏夹里没有东西哦</span>";
                        console.log("收藏夹为空");
                    }
                },
                async: true
            });
        });
        //鼠标移开
        favorites.addEventListener('mouseout', function (e) {
            //favoritesContent.innerHTML = "";
            favoritesContent.style.display = 'none';
        }, false);
    }
}

/**
 * 查找“优质手记”
 */
function findAllNotes() {
    var $noteSection = $("#noteSection");
    $.ajax({
        type: "GET",
        url: PRO_NAME + "/user/findAllNotesLimit",
        dataType: "json",
        success: function (data) {
            if (!isnull(data)) {
                var notes = JSON.parse(data);
                console.log(notes);
                for (let i = 0; i < notes.length; i++) {
                    $noteSection.append("<article class='article'>" +
                        "    <h2><a href='/user/note?noteId=" + notes[i].noteId + "'>" + notes[i].noteTitle + "</a></h2>" +
                        "    <p>点击进入详细阅读</p>" +
                        "    <div class='articleInfo'>" +
                        "        <div class='author-infobox' data-author-id='" + notes[i].blogger.userId + "'>" +
                        "            <a href='#' class='author'>" + notes[i].blogger.nickname + "</a>" +
                        "        </div>" +
                        "        <time>" + notes[i].createTime + "</time>" +
                        "        <div class='readInfo'>" +
                        "            <span><a href='#'>评论</a>" + notes[i].commentCount + "</span>" +
                        "        </div>" +
                        "    </div>" +
                        "</article>");
                }
            }
            findAllQuestions();
        },
        async: true
    });
}

/**
 * 查找“精品问答”
 */
function findAllQuestions() {
    var $questionSection = $("#questionSection");
    $.ajax({
        type: "GET",
        url: PRO_NAME + "/user/findAllQuestionsLimit",
        dataType: "json",
        success: function (data) {
            if (!isnull(data)) {
                var questions = JSON.parse(data);
                console.log(questions);
                for (let i = 0; i < questions.length; i++) {
                    $questionSection.append("<article class='article'>" +
                        "    <h2><a href='/user/question?questId=" + questions[i].questId + "'>" + questions[i].questTitle + "</a></h2>" +
                        "    <p>点击进入详细阅读</p>" +
                        "    <div class='articleInfo'>" +
                        "        <div class='author-infobox' data-author-id='" + questions[i].quizzer.userId + "'>" +
                        "            <a href='#' class='author'>" + questions[i].quizzer.nickname + "</a>" +
                        "        </div>" +
                        "        <time>" + questions[i].createTime + "</time>" +
                        "        <div class='readInfo'>" +
                        "            <span><a href='#'>回答</a>" + questions[i].answerCount + "</span>" +
                        "        </div>" +
                        "    </div>" +
                        "</article>");
                }
                showAuthorInfo();
            }
        },
        async: true
    });
}

/**
 * JS部分
 */
function addLoadEvent(func) {
    var oldonload = window.onload;
    if (typeof window.onload != 'function') {
        window.onload = func;
    } else {
        window.onload = function () {
            oldonload();
            func();
        }
    }
}

/**
 * 显示作者详细信息
 * <div class='author-info'>
 *     <div class='author-basic-info'>
 *         <div class='author-basic-infobg'>
 *             <img src='../image/csdn.png' class='author-profile'>
 *             <span class='author-nickname'>CSDN</span>
 *             <span class='author-job'>专业IT学习网站</span>
 *         </div>
 *     </div>
 *     <div class='author-devote'>
 *         <span>手记<em>9</em>篇</span><span>解答<em>9</em>次</span>
 *         <button>关注ta</button>
 *     </div>
 * </div>
 */
function showAuthorInfo() {
    var author_infobox = document.querySelectorAll(".author-infobox");

    for (let i = 0; i < author_infobox.length; i++) {
        let author_nickname;
        let flag = false;

        author_infobox[i].addEventListener("mouseover", function (e) {
            let event = e || event;        //兼容处理
            let from = event.fromElement || event.relatedTarget;//兼容处理
            if (from && this.contains(from)) {      //如果在里面则返回
                return;
            }
            if (!flag) {
                author_nickname = this.innerHTML;
                //ajax获取作者信息
                $.ajax({
                    type: "GET",
                    url: PRO_NAME + "/user/authorDetail",
                    data: {"authorId": author_infobox[i].getAttribute("data-author-id")},
                    dataType: "json",
                    success: function (data) {
                        if (!isnull(data)) {
                            var author = JSON.parse(data);
                            console.log(author);
                            author_infobox[i].innerHTML = author_infobox[i].innerHTML +
                                "<div class='author-info'>" +
                                "<div class='author-basic-info'>" +
                                "<div class='author-basic-infobg'>" +
                                "<img src='/" + author.profilePath + "' class='author-profile'>" +
                                "<span class='author-nickname'>" + author.nickname + "</span>" +
                                "<span class='author-job'>" + author.position + "</span>" +
                                "</div></div>" +
                                "<div class='author-devote'>" +
                                "<span>手记<em>" + author.noteCount + "</em>篇</span>" +
                                "<span>解答<em>" + author.answerCount + "</em>次</span>" +
                                "<a id='attentionBtn' class='pay-attention-to' data-followee-id='" + author.authorId + "'>关注ta</a>" +
                                "</div>" +
                                "</div>";
                            var attentionBtn = document.querySelector("#attentionBtn");
                            if (hasAttention(attentionBtn.getAttribute("data-followee-id"))) {
                                updateAttentionBtnStyle(attentionBtn, true);
                            }
                            //对主页面所有的关注按钮添加事件
                            var attentionBtns = document.querySelectorAll(".pay-attention-to");
                            for (let i = 0; i < attentionBtns.length; i++) {
                                attention(attentionBtns[i]);
                            }
                            flag = true;
                        }
                    },
                    async: true
                });
            } else {
                return;
            }
        });
        author_infobox[i].addEventListener("mouseout", function (e) {
            let event = e || event; //处理兼容
            let to = event.toElement || event.relatedTarget; //处理兼容
            if (to && this.contains(to)) {    //如果在里面则返回
                return;
            }
            if (flag) {
                this.innerHTML = author_nickname;
                flag = false;
            } else {
                return;
            }
        });
    }
}

/**
 * 轮播图
 */
function play() {
    var ad = document.getElementById('ad');
    var list = document.getElementById('list');
    var buttons = document.getElementById('buttons').getElementsByTagName('span');
    var prev = document.getElementById('prev');
    var next = document.getElementById('next');

    list.style.left = -imgWidth + 'px';
    var index = 1;
    var timer = null, t = null;
    var target;
    var speed;
    var animated = true;
    var imgWidth = 300;
    var imgNum = 4;

    prev.onclick = function () {
        if (!animated) {
            return;
        }
        move(imgWidth, imgWidth / 10, imgWidth, imgNum);
        index = index - 1;
        if (index < 1) {
            index = imgNum;
        }
        showButton();
    }
    next.onclick = function () {
        if (!animated) {
            return;
        }
        move(-imgWidth, imgWidth / 10, imgWidth, imgNum);
        index = index + 1;
        if (index > imgNum) {
            index = 1;
        }
        showButton();
    }
    for (var j = 0; j < buttons.length; j++) {
        buttons[j].onmouseover = function () {
            if (!animated) {
                return;
            }
            if (this.className == 'on') {
                return;
            }
            var myIndex = parseInt(this.getAttribute('index'));
            var moveDis = imgWidth * (index - myIndex);
            move(moveDis, imgWidth / 10, imgWidth, imgNum);
            index = myIndex;
            showButton();

        }
    }
    ad.onmouseout = autoMove;
    ad.onmouseover = stopMove;

    function move(distance, s, imgWidth, imgNum) {
        clearInterval(t);
        animated = false;
        target = parseInt(list.style.left) + distance;
        speed = distance / s;
        t = setInterval(function () {
            if (parseInt(list.style.left) == target) {
                if (target > -imgWidth) {
                    list.style.left = -imgWidth * imgNum + 'px';
                }
                if (target < -imgWidth * imgNum) {
                    list.style.left = -imgWidth + 'px';
                }
                clearInterval(t);
                animated = true;
                return;
            } else {
                list.style.left = list.offsetLeft + speed + 'px';
            }
        }, 10);
    }

    function showButton() {
        for (var i = 0; i < buttons.length; i++) {
            if (buttons[i].className == 'on') {
                buttons[i].className = '';
                break;
            }
        }
        buttons[index - 1].className = 'on';
    }

    function autoMove() {
        timer = setInterval(function () {
            next.onclick();
        }, 3000);
    }

    function stopMove() {
        clearInterval(timer);
    }
}

/**
 * 显示留言区
 */
function showMsgFunc() {
    var hoverBtn = document.getElementById('hoverBtn');
    var messageFunc = document.getElementById('messageFunc');
    hoverBtn.onclick = function () {
        if (parseInt(messageFunc.style.top) == 0) {
            messageFunc.style.top = -250 + 'px';
        } else {
            messageFunc.style.top = 0 + 'px';
        }
    }
}

/**
 * 显示微信账号二维码信息
 */
function showQRCode() {
    var wechat = document.querySelector("#wechat");
    var wechatQRC = wechat.querySelector("img");
    var arrow = wechat.querySelector("span");
    wechat.onmouseover = function () {
        wechatQRC.style.display = "block";
        arrow.style.display = "block";
    }
    wechat.onmouseout = function () {
        wechatQRC.style.display = "none";
        arrow.style.display = "none";
    }
}

/**
 * 显示QQ账号二维码信息
 */
function showQRCode1() {
    var qq = document.querySelector("#qq");
    var qqQRC = qq.querySelector("img");
    var arrow = qq.querySelector("span");
    qq.onmouseover = function () {
        qqQRC.style.display = "block";
        arrow.style.display = "block";
        arrow.style.borderTopColor = "rgba(239,239,239,100%)";
    }
    qq.onmouseout = function () {
        qqQRC.style.display = "none";
        arrow.style.display = "none";
    }
}

addLoadEvent(showUserDetail);
addLoadEvent(showFavoritesContent);
addLoadEvent(findAllNotes)
//addLoadEvent(showMsgFunc);
addLoadEvent(play);

addLoadEvent(showQRCode);
addLoadEvent(showQRCode1);