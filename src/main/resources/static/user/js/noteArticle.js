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
addLoadEvent(findNoteCate);
addLoadEvent(findAllNotes);

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
                user = JSON.parse(data);

                for(let j=0; j<myProfile.length; j++){
                    myProfile[j].src = "/" + user.profilePath;
                }
                for(let i=0; i<loginUserNickname.length; i++){
                    loginUserNickname[i].innerHTML = user.nickname;
                }
                experience.innerHTML = "经验 " + user.experience;
            },
            cache: true,
            async: true
        });
    }
}

function findNoteCate() {
    var $noteCate = $("#noteCate");
    $.ajax({
        type: "GET",
        url: "/user/findNoteCate",
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                var noteCate = JSON.parse(data);
                console.log(noteCate);
                for(let i=0; i<noteCate.length; i++){
                    $noteCate.append("<li><a href='/user/findNoteBelongToType?"+ noteCate[i].typeId +"'>"+ noteCate[i].typeName +"</a></li>");
                }
            }
        },
        async: true
    });
}

function findAllNotes() {
    var $noteSection = $("#noteSection");
    $.ajax({
        type: "GET",
        url: "/user/findAllNotes",
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                var notes = JSON.parse(data);
                console.log(notes);
                for(let i=0; i<notes.length; i++){
                    $noteSection.append("<article class='article'><h2><a href='/user/note?noteId=" + notes[i].noteId + "'>" + notes[i].noteTitle + "</a></h2><p>"+"Struts2的核心部分是拦截器模块"+"</p><div class='articleInfo'><a href=\"#\"><span class='author'>"+ notes[i].blogger.nickname +"</span></a><span class='content-type'>"+ notes[i].noteType.typeName +"</span><time>"+ notes[i].createTime +"</time><div class='readInfo'><span><a href='#'>评论</a>99</span><span><a href='#'>浏览</a>99</span></div></article>");
                }
            }
        },
        async: true
    });
}
