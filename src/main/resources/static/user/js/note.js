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
addLoadEvent(findNote);
addLoadEvent(findLikeCount);
addLoadEvent(whetherLike);
addLoadEvent(whetherCollect);
addLoadEvent(findComments);
addLoadEvent(submitComment);
addLoadEvent(main);

function main() {
    //点赞
    var like = document.querySelector("#like");
    like.addEventListener("click",function (e) {
        if(hasLogin()){
            likeNote();
        }else{
            showPopup("<span id='popup-login-title'>小主，要登录才能点赞哦<br>(,,・ω・,,)</span><a href='/user/loginpage' id='popup-login'>登录</a><a id='popup-cancel'>取消</a>",200,200);
        }
    });
    //收藏
    var favorite = document.querySelector("#favorite");
    favorite.addEventListener("click",function (e) {
        if(hasLogin()){
            collectNote();
        }else{
            showPopup("<span id='popup-login-title'>小主，要登录才能收藏哦<br>(,,・ω・,,)</span><a href='/user/loginpage' id='popup-login'>登录</a><a id='popup-cancel'>取消</a>",200,200);
        }
    })
}
/**
 * 悬浮显示用户详细信息
 */
var user = null;
function showUserDetail(){
    var userinfo = document.querySelector('#user');
    var userDetail = document.querySelector('#user_detail');

    var myProfile = document.querySelectorAll(".myProfile");
    var loginUserNickname = document.querySelectorAll(".loginUserNickname");
    var experience = document.querySelector("#experience");

    findUser();
    if(userinfo != null){
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
    }

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

/**
 * 获取手记详细信息
 */
function findNote() {
    var noteId = document.querySelector("#noteId");
    var title = document.querySelector("#title");
    var noteType = document.querySelector("#noteType");
    var createTime = document.querySelector("#createTime");
    //向后台发送ajax请求获取手记详细信息
    $.ajax({
        type: "GET",
        url: "/user/readNote",
        data: {"noteId":noteId.value},
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                var note = JSON.parse(data);
                console.log(note);
                document.title = note.noteTitle;
                title.innerHTML = note.noteTitle;
                noteType.innerHTML = note.noteType.typeName;
                createTime.innerHTML = note.createTime;
                findBloggerDetailInfo(note.blogger.userId);
            }
        },
        async: true
    });
}
/**
 * 获取作者信息
 */
function findBloggerDetailInfo(authorId) {
    var writerName = document.querySelector("#writerName");
    var writerProfile = document.querySelector("#writerProfile");
    var position = document.querySelector("#position");
    var noteAttentionBtn = document.querySelector("#noteAttentionBtn");
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
                noteAttentionBtn.setAttribute("data-followee-id",author.authorId);
                if(hasAttention(noteAttentionBtn.getAttribute("data-followee-id"))){
                    updateAttentionBtnStyle(noteAttentionBtn,true);
                }
                attention(noteAttentionBtn);
            }
        },
        async: true
    });
}
/**
 * 获得点赞人数
 */
function findLikeCount() {
    var noteId = document.querySelector("#noteId");
    var likeCount = document.querySelector("#like-count");
    //向后台发送ajax请求发布评论
    $.ajax({
        type: "GET",
        url: "/user/findLikeCount",
        data: {"noteId":noteId.value},
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                var count = JSON.parse(data);
                likeCount.innerHTML = count.likeCount + "点赞";
            }
        },
        async: true
    });
}
/**
 * 点赞
 */
function likeNote() {
    var noteId = document.querySelector("#noteId");
    //向后台发送ajax请求发布评论
    $.ajax({
        type: "POST",
        url: "/user/likeNote",
        data: {"noteId":noteId.value},
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                var success = JSON.parse(data).like;
                if(success == "1"){
                    console.log("已点赞");
                    hasLike();
                }else {
                    console.log("未点赞");
                    cancelLike();
                }
            }
        },
        async: true
    });
}
/**
 * 是否已点赞
 */
function whetherLike() {
    var noteId = document.querySelector("#noteId");
    //向后台发送ajax请求发布评论
    $.ajax({
        type: "POST",
        url: "/user/whetherLike",
        data: {"noteId":noteId.value},
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                var whetherLike = JSON.parse(data).like;
                if(whetherLike == "1"){
                    console.log("已点赞");
                    hasLike();
                }else {
                    console.log("未点赞");
                    cancelLike();
                }
            }
        },
        async: true
    });
}
//改变点赞图标为已点赞
function hasLike() {
    var like = document.querySelector("#like");
    var likeLabel = document.querySelector("#like-label");

    like.style.color = "#f01414";
    like.title = "已点赞";
    likeLabel.innerHTML = "感谢小主的点赞";
    likeLabel.style.color = "rgba(255, 210, 0, 1)";
}
//改变点赞图标为未点赞
function cancelLike() {
    var like = document.querySelector("#like");
    var likeLabel = document.querySelector("#like-label");

    like.style.color = "rgba(56, 168, 71, 1)";
    like.title = "小主还没点赞呢~";
    likeLabel.innerHTML = "动动小手，点个赞吧";
    likeLabel.style.color = "#000";
}

/**
 * 收藏
 */
function collectNote() {
    var noteId = document.querySelector("#noteId");
    //向后台发送ajax请求发布评论
    $.ajax({
        type: "POST",
        url: "/user/collectNote",
        data: {"noteId":noteId.value},
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                var success = JSON.parse(data).favorite;
                if(success == "1"){
                    console.log("已收藏");
                    hasCollect();
                }else {
                    console.log("未收藏");
                    cancelCollect();
                }
            }
        },
        async: true
    });
}
/**
 * 是否已收藏
 */
function whetherCollect() {
    var noteId = document.querySelector("#noteId");
    //向后台发送ajax请求发布评论
    $.ajax({
        type: "POST",
        url: "/user/whetherCollect",
        data: {"noteId":noteId.value},
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                var whetherCollect = JSON.parse(data).favorite;
                if(whetherCollect == "1"){
                    console.log("已收藏");
                    hasCollect();
                }else {
                    console.log("未收藏");
                    cancelCollect();
                }
            }
        },
        async: true
    });
}
//改变收藏图标为已收藏
function hasCollect() {
    var favorite = document.querySelector("#favorite");
    var favoriteIcon = document.querySelector("#favorite-icon");

    favorite.style.color = "rgba(54, 58, 57, 1)";
    favorite.title = "已收藏";
    favoriteIcon.style.color = "rgba(255, 63, 75, 1)";
}
//改变收藏图标为未收藏
function cancelCollect() {
    var favorite = document.querySelector("#favorite");
    var favoriteIcon = document.querySelector("#favorite-icon");

    favorite.style.color = "rgba(54, 58, 57, .7)";
    favorite.title = "未收藏";
    favoriteIcon.style.color = "rgba(54, 58, 57, .7)";
}

/**
 * 查找该手记的评论
 */
function findComments() {
    var noteId = document.querySelector("#noteId");
    //向后台发送ajax请求发布评论
    $.ajax({
        type: "GET",
        url: "/user/findComments",
        data: {"noteId":noteId.value},
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                var comments = JSON.parse(data);
                console.log(comments);
                showComments(comments);
                replyTo();
            }
        },
        async: true
    });
}
/**
 * Ajax从后台获得该手记的手记
 */
function showComments(comments) {
    //获得评论区
    var commentBox = document.querySelector("#comments");
    for(let i=0; i<comments.length; i++){
        if(comments[i] != null){
            //创建一条评论
            var comment = document.createElement("div");
            addClass("comment",comment);
            //创建评论者头像
            var observerImg = document.createElement("img");
            addClass("comment-profile",observerImg);
            observerImg.src = "/" + comments[i].user.profilePath;
            //创建父评论
            var observer = document.createElement("div");
            addClass("comment-rightpart",observer);
            observer.innerHTML = "<time class='comment-time'>"+ comments[i].commentTime +"</time>" +
                    "<a href='#' class='comment-nickname'>"+ comments[i].user.nickname +"</a><a class='reply commonReply' data-byReply-nickname='"+ comments[i].user.nickname +"' data-by-reply='"+ comments[i].commentId +"'>回复</a>" +
                    "<span class='comment-content'>"+ comments[i].commentContent +"</span>";
            //将主评论插入到评论
            comment.appendChild(observerImg);
            comment.appendChild(observer);

            if(comments[i].son != null){
                showReplies(comments[i],comments[i].son);
                //创建子评论（即回复）
                function showReplies(parentComment,childComment) {
                    for(let j=0; j<childComment.length; j++){
                        //创建回复
                        var replyBox = document.createElement("div");
                        addClass("replies",replyBox);
                        replyBox.innerHTML = "<img src='/"+ childComment[j].user.profilePath +"' class='comment-profile'>" +
                                "<time class='reply-time'>"+ childComment[j].commentTime +"</time>" +
                                "<a href='#' class='comment-nickname'>"+ childComment[j].user.nickname +"</a><span class='reply-text'>回复</span><a href='#' class='comment-nickname'>"+ parentComment.user.nickname +"</a>" +
                                "<span class='comment-content'>"+ childComment[j].commentContent +"</span>" +
                                "<a class='reply-to commonReply' data-byReply-nickname='"+ childComment[j].user.nickname +"' data-by-reply='"+ childComment[j].commentId +"'>回复</a>";
                        comment.appendChild(replyBox);
                        //如果该评论还有子评论，就继续递归创建子评论
                        if(childComment[j].son != null){
                            //传递父评论跟子评论过去，进行递归调用
                            showReplies(childComment[j],childComment[j].son);
                        }
                    }
                }
            }
            //将评论插入评论区
            commentBox.appendChild(comment);
        }
    }
}
/**
 * 发表评论
 */
function submitComment() {
    var noteId = document.querySelector("#noteId");
    var commentContent = document.querySelector("#comment-content");
    var submitComment = document.querySelector("#submit-comment");

    submitComment.addEventListener("click",function (e) {
        if(hasLogin()){
            kindEditor.sync();
            if(isnull(commentContent.value)){
                alert("评论内容不能为空");
                return;
            }
            //向后台发送ajax请求发布评论
            $.ajax({
                type: "POST",
                url: "/user/submitComment",
                data: {"noteId":noteId.value,"commentContent":commentContent.value},
                dataType: "json",
                success: function (data) {
                    if(!isnull(data)){
                        location.reload();
                        commentContent.value = null;
                        showAutoPopup("<span>评论成功</span>",100,60);
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
 * 回复
 */
function replyTo() {
    //回复按钮
    var replyToOthers = document.querySelectorAll(".commonReply");
    //评论区
    var writeCommentBox = document.querySelector("#write-comment-box");
    var commentContent = document.querySelector("#comment-content");
    var submitComment = document.querySelector("#submit-comment");
    //若该手记有评论，则进入
    if(replyToOthers != null){
        var submitReply;
        var replyText;
        var byReply;
        //记录原来的url路径
        var basePath;
        for(let i=0; i<replyToOthers.length; i++){
            replyToOthers[i].addEventListener("click",function (e) {
                if(hasLogin()){
                    //隐藏“发表评论”按钮
                    submitComment.style.display = "none";
                    //清除上一个回复的样式
                    if(submitReply != null) writeCommentBox.removeChild(submitReply);
                    if(replyText != null) writeCommentBox.removeChild(replyText);
                    if(byReply != null) writeCommentBox.removeChild(byReply);
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
                    writeCommentBox.appendChild(submitReply);
                    writeCommentBox.appendChild(replyText);
                    writeCommentBox.appendChild(byReply);

                    commentContent.focus();
                    basePath = location.href;
                    if(basePath.indexOf("#write-comment-box") == -1){
                        location.href = location.href + "#write-comment-box";
                    }
                    //发表回复
                    submitReply.addEventListener("click",function (e) {
                        var noteId = document.querySelector("#noteId");
                        var byReplyId = replyToOthers[i].getAttribute("data-by-reply");
                        kindEditor.sync();
                        if(isnull(commentContent.value)){
                            alert("回复内容不能为空");
                            return;
                        }
                        //向后台发送ajax请求发表回复
                        $.ajax({
                            type: "POST",
                            url: "/user/submitReply",
                            data: {"noteId":noteId.value,"byReplyId":byReplyId,"commentContent":commentContent.value},
                            dataType: "json",
                            success: function (data) {
                                if(!isnull(data)){
                                    commentContent.value = null;
                                    location.href = basePath;
                                    location.reload();
                                    showAutoPopup("<span>回复成功</span>",100,60);
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

