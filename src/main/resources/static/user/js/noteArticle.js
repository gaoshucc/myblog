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


/**
 * 悬浮显示用户详细信息
 */
var user = null;
function showUserDetail(){
    var userinfo = document.querySelector('#user');
    var userDetail = document.querySelector('#user_detail');
    var loginUser = document.querySelector('#loginUser');

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
        //判断用户是否已登录
        if(loginUser != null){
            var userId = loginUser.value;
            console.log("userId" + userId);
            //向后台发送ajax请求获取登录用户详细信息
            $.ajax({
                type: "GET",
                url: "/user/userDetail",
                data: {"userId":userId},
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
                cache: true,
                async: true
            });
        }
    }
}
