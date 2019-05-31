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
 * 悬浮显示用户详细信息
 */
var user = null;

function showUserDetail() {
    if (hasLogin()) {
        var userinfo = document.querySelector('#user');
        var userDetail = document.querySelector('#user_detail');

        var myProfile = document.querySelectorAll(".myProfile");
        var loginUserNickname = document.querySelectorAll(".loginUserNickname");
        var experience = document.querySelector("#experience");

        findUser();
        userinfo.addEventListener('mouseover', function (e) {
            userDetail.style.display = 'block';
            //判断鼠标是否已经悬浮，放置重复发送请求
            let event = e || event;        //兼容处理
            let from = event.fromElement || event.relatedTarget;//兼容处理
            if (from && this.contains(from)) {      //如果在里面则返回
                return;
            }
            findUser();
        }, false);
        userinfo.addEventListener('mouseout', function (e) {
            userDetail.style.display = 'none';
        }, false);

        //获取登录用户
        function findUser() {
            //向后台发送ajax请求获取登录用户详细信息
            $.ajax({
                type: "GET",
                url: PRO_NAME + "/user/userDetail",
                dataType: "json",
                success: function (data) {
                    user = JSON.parse(data);

                    for (let j = 0; j < myProfile.length; j++) {
                        myProfile[j].src = "/" + user.profilePath;
                    }
                    for (let i = 0; i < loginUserNickname.length; i++) {
                        loginUserNickname[i].innerHTML = user.nickname;
                    }
                    experience.innerHTML = "经验 " + user.experience;
                },
                cache: true,
                async: true
            });
        }
    }
}

/**
 * 查找问题分类
 */
function findQuestCate() {
    var $questCate = $("#questCate");
    $.ajax({
        type: "GET",
        url: PRO_NAME + "/user/findQuestCate",
        dataType: "json",
        success: function (data) {
            if (!isnull(data)) {
                var questCate = JSON.parse(data);
                console.log(questCate);
                for (let i = 0; i < questCate.length; i++) {
                    $questCate.append("<li><a class='quest-type' data-quest-type-id='" + questCate[i].typeId + "'>" + questCate[i].typeName + "</a></li>");
                }
                var questTypeList = document.querySelectorAll(".quest-type");
                var lastClick = -1;
                for (let j = 0; j < questTypeList.length; j++) {
                    questTypeList[j].addEventListener("click", function (e) {
                        if (lastClick != -1) {
                            if (hasClass("clicked", questTypeList[lastClick])) {
                                questTypeList[lastClick].classList.remove("clicked");
                            }
                        }
                        addClass("clicked", questTypeList[j]);
                        lastClick = j;
                        findQuestionsByTypeId(questTypeList[j].getAttribute("data-quest-type-id"));
                    })
                }
            }
        },
        async: true
    });
}

function findAllQuestions() {
    var $questSection = $("#questSection");
    $.ajax({
        type: "GET",
        url: PRO_NAME + "/user/findAllQuestions",
        dataType: "json",
        success: function (data) {
            $questSection.html("");
            if (!isnull(data)) {
                var questions = JSON.parse(data);
                console.log(questions);
                for (let i = 0; i < questions.length; i++) {
                    $questSection.append("<article class='article'><h2><a href='/user/question?questId=" + questions[i].questId + "'>" + questions[i].questTitle + "</a></h2><p>" + "Struts2的核心部分是拦截器模块" + "</p><div class='articleInfo'><a href=\"#\"><span class='author'>" + questions[i].quizzer.nickname + "</span></a><span class='content-type'>" + questions[i].questType.typeName + "</span><time>" + questions[i].createTime + "</time><div class='readInfo'><span><a href='#'>回答</a>" + questions[i].answerCount + "</span></div></article>");
                }
            }
        },
        async: true
    });
}

/**
 * 根据noteTypeId获取某种类型的手记
 */
function findQuestionsByTypeId(questTypeId) {
    var $questSection = $("#questSection");
    $.ajax({
        type: "GET",
        url: PRO_NAME + "/user/findQuestionsByTypeId",
        data: {"questTypeId": questTypeId},
        dataType: "json",
        success: function (data) {
            $questSection.html("");
            if (!isnull(data)) {
                var questions = JSON.parse(data);
                console.log(questions);
                for (let i = 0; i < questions.length; i++) {
                    $questSection.append("<article class='article'><h2><a href='/user/question?questId=" + questions[i].questId + "'>" + questions[i].questTitle + "</a></h2><p>" + "Struts2的核心部分是拦截器模块" + "</p><div class='articleInfo'><a href=\"#\"><span class='author'>" + questions[i].quizzer.nickname + "</span></a><span class='content-type'>" + questions[i].questType.typeName + "</span><time>" + questions[i].createTime + "</time><div class='readInfo'><span><a href='#'>回答</a>" + questions[i].answerCount + "</span></div></article>");
                }
            } else {
                $questSection.append("<span class='iconfont icon-stack'></span><span id='no-content'>没有有关内容哦</span>");
            }
        },
        async: true
    });
}

function showWin() {
    var write = document.querySelector("#write");
    var askingbg = document.querySelector("#askingbg");
    var askbox = document.querySelector("#askbox");

    askingbg.style.display = "none";
    write.addEventListener("click", function (e) {
        if (hasLogin()) {
            askingbg.style.display = "block";
            askingbg.style.opacity = "1";
        } else {
            showPopup("<span id='popup-login-title'>小主，要登录才能提问哦<br>(,,・ω・,,)</span><a href='/user/loginpage' id='popup-login'>登录</a><a id='popup-cancel'>取消</a>", 200, 200);
        }
    }, "false");

    askbox.addEventListener("click", function (e) {
        if (e.target == e.currentTarget) {
            askingbg.style.display = "none";
        }
    }, "false");
}

function hideWin() {
    var askingbg = document.querySelector("#askingbg");
    var cancel = document.querySelector("#cancel");

    cancel.addEventListener("click", function () {
        var confirmCancel = confirm("是否取消编辑？");
        if (confirmCancel) {
            askingbg.style.display = "none";
        }
    }, "false");
}

function dynamicLoadCss(url) {
    var head = document.getElementsByTagName('head')[0];
    var link = document.createElement('link');
    link.type = 'text/css';
    link.rel = 'stylesheet';
    link.href = url;
    head.appendChild(link);
}

addLoadEvent(showUserDetail);
addLoadEvent(findQuestCate);
addLoadEvent(findAllQuestions);
addLoadEvent(showWin);
addLoadEvent(hideWin);