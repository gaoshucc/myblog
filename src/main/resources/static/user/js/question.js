/*
jq部分
*/
$(function(){
    $('#close,#report').click(function(e){
        e.preventDefault();  //阻止默认单击事件
        $('#popup').toggleClass('show');
    });

    if($(window).scrollTop()<100){
        $("#uptoTop").fadeOut(1);   //初始化页面时，隐藏Top按钮
    }

    $(window).scroll(function(){
        if($(window).scrollTop()>100){
            $("#uptoTop").fadeIn(1000);  //当页面起点距离窗口大于100时，淡入
        }else{
            $("#uptoTop").fadeOut(1000);    //当页面起点距离窗口小于100时，淡出
        }
    });
    //跳转到顶部（动画）
    $("#uptoTop").click(function(){
        var dis = $(window).scrollTop();
        $("body,html").animate({scrollTop:0},300);
        return false;
    });
});

function addLoadEvent(func){
    var oldonload = window.onload;
    if(typeof window.onload != 'function'){
        window.onload = func;
    }else{
        window.onload = function(){
            oldonload();
            func();
        }
    }
}

addLoadEvent(showUserDetail);
addLoadEvent(findQuestion);
addLoadEvent(submitAnswer);
addLoadEvent(findAnswers);
/**
 * 悬浮显示用户详细信息
 */
var user = null;
function showUserDetail(){
    if(hasLogin()){
        var userinfo = document.querySelector('#user');
        var userDetail = document.querySelector('#user_detail');

        var myProfile = document.querySelectorAll(".myProfile");
        var loginUserNickname = document.querySelectorAll(".loginUserNickname");
        var experience = document.querySelector("#experience");

        findUser();
        userinfo.addEventListener('mouseover',function(e){
            userDetail.style.display = 'block';
            //判断鼠标是否已经悬浮，放置重复发送请求
            let event = e || event;        //兼容处理
            let from = event.fromElement || event.relatedTarget;//兼容处理
            if(from && this.contains(from)){      //如果在里面则返回
                return;
            }
            findUser();
        },false);
        userinfo.addEventListener('mouseout',function(e){
            userDetail.style.display = 'none';
        },false);

        //获取登录用户
        function findUser() {
            //向后台发送ajax请求获取登录用户详细信息
            $.ajax({
                type: "GET",
                url: "/user/userDetail",
                dataType: "json",
                success: function (data) {
                    user = JSON.parse(data);

                    for(let j=0; j<myProfile.length; j++){
                        myProfile[j].src = "/" + user.profilePath;
                    }
                    for(let i=0; i<loginUserNickname.length; i++){
                        loginUserNickname[i].innerHTML = user.nickname;
                    }
                    experience.innerHTML = "经验 " + user.experience;
                },
                async: true
            });
        }
    }
}

/**
 * 获取手记详细信息
 */
function findQuestion() {
    var questId = document.querySelector("#questId");
    var title = document.querySelector("#title");
    var questType = document.querySelector("#questType");
    var createTime = document.querySelector("#createTime");
    var writerName = document.querySelector("#writerName");
    var writerProfile = document.querySelector("#writerProfile");
    var position = document.querySelector("#position");
    var questionAttentionBtn = document.querySelector("#questionAttentionBtn");
    //向后台发送ajax请求获取手记详细信息
    $.ajax({
        type: "GET",
        url: "/user/readQuestion",
        data: {"questId":questId.value},
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                var question = JSON.parse(data);
                console.log(question);
                document.title = question.questTitle;
                title.innerHTML = question.questTitle;
                questType.innerHTML = question.questType.typeName;
                createTime.innerHTML = question.createTime;
                findQuzzierDetailInfo(question.quizzer.userId);
            }
        },
        async: true
    });
}
/**
 * 获取作者信息
 */
function findQuzzierDetailInfo(authorId) {
    var writerName = document.querySelector("#writerName");
    var writerProfile = document.querySelector("#writerProfile");
    var position = document.querySelector("#position");
    var questionAttentionBtn = document.querySelector("#questionAttentionBtn");
    var noteCount = document.querySelector("#noteCount");
    var answerCount = document.querySelector("#answerCount");
    $.ajax({
        type: "GET",
        url: "/user/authorDetail",
        data: {"authorId":authorId},
        dataType: "json",
        success: function (data) {
            console.log(data);
            if(!isnull(data)){
                var author = JSON.parse(data);
                console.log(author);
                writerName.innerHTML = author.nickname;
                position.innerHTML = author.position;
                writerProfile.src = "/" + author.profilePath;
                noteCount.innerHTML = author.noteCount + "篇手记";
                answerCount.innerHTML = "解答" + author.answerCount + "次";
                questionAttentionBtn.setAttribute("data-followee-id",author.authorId);
                if(hasAttention(questionAttentionBtn.getAttribute("data-followee-id"))){
                    updateAttentionBtnStyle(questionAttentionBtn,true);
                }
                attention(questionAttentionBtn);
            }
        },
        async: true
    });
}
/**
 * 发表评论
 */
function submitAnswer() {
    var questId = document.querySelector("#questId");
    var answerContent = document.querySelector("#answer-content");
    var submitAnswer = document.querySelector("#submit-answer");

    submitAnswer.addEventListener("click",function (e) {
        if(hasLogin()){
            kindEditor.sync();
            if(isnull(answerContent.value)){
                alert("评论内容不能为空");
                return;
            }
            //向后台发送ajax请求发布评论
            $.ajax({
                type: "POST",
                url: "/user/submitAnswer",
                data: {"questId":questId.value,"answerContent":answerContent.value},
                dataType: "json",
                success: function (data) {
                    if(!isnull(data)){
                        answerContent.value = null;
                        location.reload();
                    }
                },
                async: true
            });
        }else {
            showPopup("<span id='popup-login-title'>小主，要登录才能评论哦<br>(,,・ω・,,)</span><a href='/user/loginpage' id='popup-login'>登录</a><a id='popup-cancel'>取消</a>",200,200);
        }
    });
}
/**
 * 查找该问题的评论
 */
function findAnswers() {
    var questId = document.querySelector("#questId");
    //向后台发送ajax请求发布评论
    $.ajax({
        type: "GET",
        url: "/user/findAnswers",
        data: {"questId":questId.value},
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                var answers = JSON.parse(data);
                console.log(answers);
                showAnswers(answers);
                replyTo();
            }
        },
        async: true
    });
}
/**
 * Ajax从后台获得该问题的回答
 */
function showAnswers(answers) {
    //获得回答区
    var answerBox = document.querySelector("#answers");
    for(let i=0; i<answers.length; i++){
        if(answers[i] != null){
            //创建一条回答
            var answer = document.createElement("div");
            addClass("answer",answer);
            //创建回答者头像
            var observerImg = document.createElement("img");
            addClass("answer-profile",observerImg);
            observerImg.src = "/" + answers[i].user.profilePath;
            //创建父回答
            var observer = document.createElement("div");
            addClass("answer-rightpart",observer);
            observer.innerHTML = "<time class='answer-time'>"+ answers[i].answerTime +"</time>" +
                "<a href='#' class='answer-nickname'>"+ answers[i].user.nickname +"</a><a class='reply commonReply' data-byReply-nickname='"+ answers[i].user.nickname +"' data-by-reply='"+ answers[i].answerId +"'>回复</a>" +
                "<span class='answer-content'>"+ answers[i].answerContent +"</span>";
            //将主回答插入到回答区
            answer.appendChild(observerImg);
            answer.appendChild(observer);

            if(answers[i].son != null){
                showReplies(answers[i],answers[i].son);
                //创建子回答（即回答）
                function showReplies(parentAnswer,childAnswer) {
                    for(let j=0; j<childAnswer.length; j++){
                        //创建回复
                        var replyBox = document.createElement("div");
                        addClass("replies",replyBox);
                        replyBox.innerHTML = "<img src='/"+ childAnswer[j].user.profilePath +"' class='answer-profile'>" +
                            "<time class='reply-time'>"+ childAnswer[j].answerTime +"</time>" +
                            "<a href='#' class='answer-nickname'>"+ childAnswer[j].user.nickname +"</a><span class='reply-text'>回复</span><a href='#' class='answer-nickname'>"+ parentAnswer.user.nickname +"</a>" +
                            "<span class='answer-content'>"+ childAnswer[j].answerContent +"</span>" +
                            "<a class='reply-to commonReply' data-byReply-nickname='"+ childAnswer[j].user.nickname +"' data-by-reply='"+ childAnswer[j].answerId +"'>回复</a>";
                        answer.appendChild(replyBox);
                        //如果该答案还有子回答，就继续递归创建子回答
                        if(childAnswer[j].son != null){
                            //传递父回答跟子回答过去，进行递归调用
                            showReplies(childAnswer[j],childAnswer[j].son);
                        }
                    }
                }
            }
            //将回答插入回答区
            answerBox.appendChild(answer);
        }
    }
}
/**
 * 回复
 */
function replyTo() {
    //回复按钮
    var replyToOthers = document.querySelectorAll(".commonReply");
    //回答区
    var writeAnswerBox = document.querySelector("#write-answer-box");
    var answerContent = document.querySelector("#answer-content");
    var submitAnswer = document.querySelector("#submit-answer");
    //若该问题有回答，则进入
    if(replyToOthers != null){
        var submitReply;
        var replyText;
        var byReply;
        //记录原来的url路径
        var basePath;
        for(let i=0; i<replyToOthers.length; i++){
            replyToOthers[i].addEventListener("click",function (e) {
                if(hasLogin()){
                    //隐藏“回答问题”按钮
                    submitAnswer.style.display = "none";
                    //清除上一个回复的样式
                    if(submitReply != null) writeAnswerBox.removeChild(submitReply);
                    if(replyText != null) writeAnswerBox.removeChild(replyText);
                    if(byReply != null) writeAnswerBox.removeChild(byReply);
                    //初始化回复输入框
                    submitReply = document.createElement("a");
                    submitReply.setAttribute("id", "submit-reply");
                    submitReply.innerHTML = "发表回复";
                    replyText = document.createElement("span");
                    addClass("reply-text", replyText);
                    replyText.innerHTML = "回复";
                    byReply = document.createElement("input");
                    addClass("byReply", byReply);
                    byReply.value = replyToOthers[i].getAttribute("data-byReply-nickname");
                    writeAnswerBox.appendChild(submitReply);
                    writeAnswerBox.appendChild(replyText);
                    writeAnswerBox.appendChild(byReply);

                    answerContent.focus();
                    basePath = location.href;
                    location.href = location.href + "#write-answer-box";
                    //发表回复
                    submitReply.addEventListener("click",function (e) {
                        var questId = document.querySelector("#questId");
                        var byReplyId = replyToOthers[i].getAttribute("data-by-reply");
                        kindEditor.sync();
                        if(isnull(answerContent.value)){
                            alert("回复内容不能为空");
                            return;
                        }
                        //向后台发送ajax请求发表回复
                        $.ajax({
                            type: "POST",
                            url: "/user/submitAnswerReply",
                            data: {"questId":questId.value,"byReplyId":byReplyId,"answerContent":answerContent.value},
                            dataType: "json",
                            success: function (data) {
                                if(!isnull(data)){
                                    answerContent.value = null;
                                    location.href = basePath;
                                    location.reload();
                                }
                            },
                            error: function () {
                                alert("回复失败，请检查网络后重试");
                            },
                            async: true
                        });
                    })
                }else {
                    showPopup("<span id='popup-login-title'>小主，要登录才能回复哦<br>(,,・ω・,,)</span><a href='/user/loginpage' id='popup-login'>登录</a><a id='popup-cancel'>取消</a>",200,200);
                }
            });
        }
    }
}

