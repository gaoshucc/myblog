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
                    myProfile[j].src = "http://localhost:8080/user/image/profile/" + user.profilePath;
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
    var writerName = document.querySelector("#writerName");
    var writerProfile = document.querySelector("#writerProfile");
    var position = document.querySelector("#position");
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
                writerName.innerHTML = note.blogger.nickname;
                position.innerHTML = note.blogger.positionId;
                writerProfile.src = "/user/image/profile/" + note.blogger.profilePath;
            }
        },
        async: true
    });
}