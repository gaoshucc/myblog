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
addLoadEvent(showWin);
addLoadEvent(hideWin);
addLoadEvent(form_validate);
addLoadEvent(findMyQuestions);
addLoadEvent(noteRecycleBin);

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
                if(!isnull(data)){
                    user = JSON.parse(data);

                    for(let j=0; j<myProfile.length; j++){
                        myProfile[j].src = "/" + user.profilePath;
                    }
                    for(let i=0; i<loginUserNickname.length; i++){
                        loginUserNickname[i].innerHTML = user.nickname;
                    }
                    experience.innerHTML = "经验 " + user.experience;
                }
            },
            async: true
        });
    }
}
/*
* 提问题
*/
function showWin(){
    var write = document.querySelector("#write");
    var askingbg = document.querySelector("#askingbg");
    var askbox = document.querySelector("#askbox");

    askingbg.style.display = "none";
    write.addEventListener("click",function(e){
        askingbg.style.display = "block";
        askingbg.style.opacity = "1";
    },"false");

    askbox.addEventListener("click",function(e){
        if(e.target == e.currentTarget){
            askingbg.style.display = "none";
        }
    },"false");
}

function hideWin(){
    var askingbg = document.querySelector("#askingbg");
    var cancel = document.querySelector("#cancel");

    cancel.addEventListener("click",function(){
        var confirmCancel = confirm("是否取消编辑？");
        if(confirmCancel){
            askingbg.style.display = "none";
        }
    },"false");
}
/*
* 提问表单校验
* */
function form_validate(){
    $("#question-form").validate({
        rules:{
            questionTitle:{
                required:true
            }
        },
        messages:{
            questionTitle:{
                required: "标题不能为空"
            }
        }
    });
}
/**
 * 查找“我的手记”并显示
 */
function findMyQuestions() {
    var $questionSection = $("#questionSection");
    $questionSection.empty();
    $.ajax({
        type: "GET",
        url: "/user/findMyQuestions",
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                let questions = JSON.parse(data);
                console.log(questions);
                for(let i=0; i<questions.length; i++){
                    $questionSection.append("<article class='article'><h2><a class='questTitle' href='/user/question?questId=" + questions[i].questId + "'>" + questions[i].questTitle + "</a><a class='deleteQuestion' data-questionId='"+ questions[i].questId +"'>删除</a></h2><p>"+"点击进入详细阅读"+"</p><div class='articleInfo'><span class='content-type'>"+ questions[i].questType.typeName +"</span><time>"+ questions[i].createTime +"</time><div class='readInfo'><span><a href='#'>回答</a>"+ questions[i].answerCount +"</span></div></article>");
                }
                deleteQuestion();
            }else {
                $questionSection.append("<span class='iconfont icon-stack'></span><span id='no-content'>没有内容哦</span>");
            }
        },
        async: true
    });
}
/**
 * 删除“我的提问”并重新加载
 */
function deleteQuestion() {
    var deleteList = document.querySelectorAll(".deleteQuestion");
    for(let i=0; i<deleteList.length; i++){
        deleteList[i].addEventListener("click",function (e) {
            var confirmDel = confirm("确认删除该提问？（可通过回收站还原）");
            if(confirmDel){
                var questionId = deleteList[i].getAttribute("data-questionId");
                console.log("questionId:"+questionId);
                $.ajax({
                    type: "GET",
                    url: "/user/deleteQuestion",
                    data:{"questionId":questionId},
                    dataType: "json",
                    success: function (data) {
                        if(!isnull(data)){
                            if(data == "1"){
                                confirm("删除成功");
                                location.href = "/user/myQuestion";
                            }else {
                                confirm("删除失败");
                            }
                        }
                    },
                    async: true
                });
            }
        });
    }
}
/**
 * 查找“我的提问”回收站
 */
function noteRecycleBin() {
    var $questionSection = $("#questionSection");
    var note_recycle_bin = document.querySelector("#question-recycle-bin");
    note_recycle_bin.addEventListener("click",function (e) {
        $questionSection.empty();
        $.ajax({
            type: "GET",
            url: "/user/questionRecycleBin",
            dataType: "json",
            success: function (data) {
                if(!isnull(data)){
                    let questions = JSON.parse(data);
                    console.log(questions);
                    for(let i=0; i<questions.length; i++){
                        $questionSection.append("<article class='article'><h2><a class='questTitle' href='/user/question?questId=" + questions[i].questId + "'>" + questions[i].questTitle + "</a><a class='recycleQuestion' data-questionId='"+ questions[i].questId +"'>还原</a><a class='completelyDel' data-questionId='"+ questions[i].questId +"'>彻底删除</a></h2><p>"+"点击进入详细阅读"+"</p><div class='articleInfo'><span class='content-type'>"+ questions[i].questType.typeName +"</span><time>"+ questions[i].createTime +"</time><div class='readInfo'><span><a href='#'>回答</a>"+ questions[i].answerCount +"</span></div></article>");
                    }
                    recycleQuestion();
                    completelyQuestion();
                }else {
                    $questionSection.append("<span class='iconfont icon-stack'></span><span id='no-content'>回收站里没有内容哦</span>");
                }
            },
            async: true
        });
    });
}
/**
 * 还原“我的提问”并重新加载
 */
function recycleQuestion() {
    var recycleList = document.querySelectorAll(".recycleQuestion");
    for(let i=0; i<recycleList.length; i++){
        recycleList[i].addEventListener("click",function (e) {
            var confirmRecycle = confirm("确认还原该提问？");
            if(confirmRecycle){
                var questionId = recycleList[i].getAttribute("data-questionId");
                console.log("questionId:"+questionId);
                $.ajax({
                    type: "GET",
                    url: "/user/recycleQuestion",
                    data:{"questionId":questionId},
                    dataType: "json",
                    success: function (data) {
                        if(!isnull(data)){
                            if(data == "1"){
                                confirm("还原成功");
                                location.href = "/user/myQuestion";
                            }else {
                                confirm("还原失败");
                            }
                        }
                    },
                    async: true
                });
            }
        });
    }
}
/**
 * 彻底删除“我的提问”并重新加载
 */
function completelyQuestion() {
    var completelyDelList = document.querySelectorAll(".completelyDel");
    for(let i=0; i<completelyDelList.length; i++){
        completelyDelList[i].addEventListener("click",function (e) {
            var confirmDel = confirm("确认彻底删除该提问？（该操作不可恢复）");
            if(confirmDel){
                var questionId = completelyDelList[i].getAttribute("data-questionId");
                console.log("questionId:"+questionId);
                $.ajax({
                    type: "GET",
                    url: "/user/completelyDelQuestion",
                    data:{"questionId":questionId},
                    dataType: "json",
                    success: function (data) {
                        if(!isnull(data)){
                            if(data == "1"){
                                confirm("已彻底删除");
                                location.href = "/user/myQuestion";
                            }else {
                                confirm("彻底删除失败");
                            }
                        }
                    },
                    async: true
                });
            }
        });
    }
}