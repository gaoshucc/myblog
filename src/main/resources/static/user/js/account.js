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

/**
 * 显示用户详细信息
 */
//当前登录用户
var user = null;
function showUserDetail(){
    var userinfo = document.querySelector("#user");
    var loginUser = document.querySelector('#loginUser');
    var userDetail = document.querySelector('#user_detail');

    var myProfile = document.querySelectorAll(".myProfile");
    var username = document.querySelector("#username");
    var nicknames = document.querySelectorAll(".nickname");
    var gender = document.querySelector("#gender");
    var experience = document.querySelector("#experience");
    var signature = document.querySelector("#signature");
    //判断用户是否已登录
    if(loginUser != null && userDetail != null) {
        //获取登录用户名
        var userId = loginUser.value;
        console.log("userId:" + userId);
        //向后台发送ajax请求获取登录用户详细信息
        $.ajax({
            type: "GET",
            url: "/user/userDetail",
            data: {"userId": userId},
            dataType: "json",
            success: function (data) {
                //初始化登录用户
                user = JSON.parse(data);
                console.log("password:" + user.password);

                for(let i=0; i<myProfile.length; i++){
                    myProfile[i].src = "http://localhost:8080/user/image/profile/" + user.profilePath;
                }
                for (let j = 0; j < nicknames.length; j++) {
                    nicknames[j].innerHTML = user.nickname;
                }
                experience.innerHTML = "经验 " + user.experience;
                username.innerHTML = user.username;
                if (user.gender == null) {
                    gender.classList.add("icon-pinglun");
                } else {
                    if (user.gender == 0) {
                        gender.classList.add("icon-nvxing");
                    }
                    if (user.gender == 1) {
                        gender.classList.add("icon-nanxing");
                    }
                }
                signature.innerHTML = user.motto;
            },
            cache: true,
            async: true
        });
    }
    userinfo.addEventListener('mouseover',function(e){
        userDetail.style.display = 'block';
    },false);
    userinfo.addEventListener('mouseout',function(e){
        userDetail.style.display = 'none';
    },false);
}

//显示侧边栏
function showAside(){
    var switch_box = document.querySelector("#switch-box");
    var close_aside = document.querySelector("#close-aside");
    var account_funcbox = document.querySelector("#account-funcbox");
    var ani_tags = false;

    switch_box.addEventListener('click',function(){
        if(!ani_tags){
            account_funcbox.classList.add("hideaside");
        }
        ani_tags = true;
        account_funcbox.classList.toggle("showaside");
    });

    close_aside.addEventListener('click',function(){
        ani_tags = false;
        account_funcbox.classList.toggle("showaside");
    });
}

addLoadEvent(showUserDetail);
addLoadEvent(showAside);


//监听点击事件，显示或隐藏“关注的人”列表
function showfollweelist(){
    var $followee = $("#followee");
    var $followeelist = $("#list-followee");
    $followee.click(function(){
        $followee.toggleClass("pulldown");
        $followeelist.toggle(200,function(){});
    });
}

function showpasswordmanage(){
    var $mdfpwdbtn = $("#mdfpwd-btn");
    var $password_manage = $("#password-manage");
    var $confirm = $("#confirm");
    var $close_password_manage = $("#close-password-manage");

    $mdfpwdbtn.click(function(){
        $password_manage.toggle(200,function(){});
    });
    $confirm.click(function(){
        $password_manage.toggle(200,function(){});
    });
    $close_password_manage.click(function(){
        $password_manage.toggle(200,function(){});
    });
}

/**
 * 头像修改弹出层动画
 */
function showEditProfile() {
    $('#profile-close,#profile-revisability,#profile-popup-bg').click(function(e){
        e.preventDefault();  //阻止默认单击事件
        //$("body").append("<div id='popup'><div id='popup-content'><span class='iconfont icon-guanbi' id='close' title='关闭'></span><div id='edit-content'><img id='magnify-profile' th:src='@{/user/image/profile/profile.jpg}'><a href='#' id='submit-change'>修改</a></div></div><div id='popup-bg'></div></div>");
        $('#profile-popup').toggleClass('show');
    });
}

$(function(){
    showfollweelist();
    showpasswordmanage();
    showEditProfile();
})
