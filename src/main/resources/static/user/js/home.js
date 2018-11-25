/* 
jq部分
*/
$(function(){
    //注册新用户弹出层动画
    $('#reg-close,#regist').click(function(e){
        e.preventDefault();  //阻止默认单击事件
        console.log("点击了注册按钮")
        $('#reg-popup').toggleClass('show');
    });

    //注册表单校验
    form_validate();

    //反馈弹出层动画
    $('#close,#feedback').click(function(e){
        e.preventDefault();  //阻止默认单击事件
        $('#popup').toggleClass('show');
    });
    //初始化页面时，隐藏Top按钮
    if($(window).scrollTop()<100){
        $("#uptoTop").fadeOut(1);
    }

    $(window).scroll(function(){
        if($(window).scrollTop()>100){
            //当页面起点距离窗口大于100时，淡入
            $("#uptoTop").fadeIn(1000);
        }else{
            //当页面起点距离窗口小于100时，淡出
            $("#uptoTop").fadeOut(1000);
        }
    });
    //跳转到顶部（动画）
    $("#uptoTop").click(function(){
        var dis = $(window).scrollTop();
        $("body,html").animate({scrollTop:0},300);  
        return false;
     });

});

/**
 * 前端注册表单校验
 */
function form_validate(){
    $("#regist-form").validate({
        rules:{
            username:{
                required:true,
                rangelength:[11,11],
                remote:{
                    url: "/user/userexists",
                    type: "post",
                    dataType: "json",
                    data: {
                        username: function () {
                            return $("#username").val();
                        }
                    }
                }
            },
            nickname:{
                required:true,
                maxlength:20,
                remote:{
                    url: "/user/nicknameexists",
                    type: "post",
                    dataType: "json",
                    data: {
                        username: function () {
                            return $("#nickname").val();
                        }
                    }
                }
            },
            password:{
                required:true,
                minlength:8
            },
            valicode:{
                required:true,
            }
        },
        messages:{
            username:{
                required:"账号不能为空",
                rangelength:"请输入正确的手机号",
                remote: "用户已存在，请直接登录"
            },
            nickname:{
                required:"昵称不能为空",
                maxlength:"昵称长度不能超过20",
                remote: "昵称已存在"
            },
            password:{
                required: "密码不能为空",
                minlength: "密码长度必须大于8个字符"
            },
            valicode:{
                required:"验证码不能为空",
            }
        }
    });
}

/**
 * JS部分
 */
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
 * 悬浮显示用户详细信息
 */
function showUserDetail(){
    var user = document.querySelector('#user');
    var userDetail = document.querySelector('#user_detail');
    //如果用户已登录
    if(user != null){
        user.addEventListener('mouseover',function(e){
            userDetail.style.display = 'block';
        },false);
        user.addEventListener('mouseout',function(e){
            userDetail.style.display = 'none';
        },false);
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
function showAuthorInfo(){
    var author_infobox = document.querySelectorAll(".author-infobox");

    for(let i=0; i<author_infobox.length; i++){
        let author_nickname;
        let flag = false;

        author_infobox[i].addEventListener("mouseover",function(e){
            let event = e || event;        //兼容处理
            let from = event.fromElement || event.relatedTarget;//兼容处理
            if(from && this.contains(from)){      //如果在里面则返回
                return;    
            }
            if(!flag){
                author_nickname = this.innerHTML;
                this.innerHTML = this.innerHTML + "<div class='author-info'><div class='author-basic-info'><div class='author-basic-infobg'><img th:src='@{/user/image/csdn.png}' class='author-profile'><span class='author-nickname'>CSDN</span><span class='author-job'>专业IT学习网站</span></div></div><div class='author-devote'><span>手记<em>9</em>篇</span><span>解答<em>9</em>次</span><button>关注ta</button></div></div>";
                flag = true;
            }else{
                return;
            }            
        });
        author_infobox[i].addEventListener("mouseout",function(e){
            let event = e || event; //处理兼容
            let to = event.toElement || event.relatedTarget; //处理兼容
            if(to && this.contains(to)){    //如果在里面则返回
                return;
            }
            if(flag){
                this.innerHTML = author_nickname;
                flag = false;
            }else{
                return;
            }
        });
    }
}

/**
 * 轮播图
 */
function play(){
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

    prev.onclick = function(){
        if(!animated){
            return;
        }
        move(imgWidth, imgWidth/10, imgWidth, imgNum);
        index = index - 1;
        if(index < 1){
            index = imgNum;
        }
        showButton();
    }
    next.onclick = function(){
        if(!animated){
            return;
        }
        move(-imgWidth,  imgWidth/10, imgWidth, imgNum);
        index = index + 1;
        if(index > imgNum){
            index = 1;
        }
        showButton();
    }
    for(var j=0; j<buttons.length; j++){
        buttons[j].onmouseover = function(){
            if(!animated){
                return;
            }
            if(this.className == 'on'){
                return;
            }
            var myIndex = parseInt(this.getAttribute('index'));
            var moveDis = imgWidth*(index - myIndex);
            move(moveDis,  imgWidth/10, imgWidth, imgNum);
            index = myIndex;
            showButton();

        }
    }
    ad.onmouseout = autoMove;
    ad.onmouseover = stopMove; 

    function move(distance, s, imgWidth, imgNum){
        clearInterval(t);
        animated = false;
        target =  parseInt(list.style.left) + distance;
        speed = distance/s;
        t = setInterval(function(){
            if(parseInt(list.style.left) == target){
                if(target > -imgWidth){
                    list.style.left = -imgWidth*imgNum + 'px';
                }
                if(target < -imgWidth*imgNum){
                    list.style.left = -imgWidth + 'px';
                }
                clearInterval(t);
                animated = true;
                return;
            }else{
                list.style.left = list.offsetLeft + speed + 'px';
            }
        },10);
    }
    function showButton(){
        for(var i=0; i<buttons.length; i++){
            if(buttons[i].className == 'on'){
                buttons[i].className = '';
                break;
            }
        }
        buttons[index-1].className = 'on';
    }
    function autoMove(){
        timer = setInterval(function(){
            next.onclick();
        },3000);
    }
    function stopMove(){
        clearInterval(timer);
    }
}

//显示留言区
function showMsgFunc(){
    var hoverBtn = document.getElementById('hoverBtn');
    var messageFunc = document.getElementById('messageFunc');
    hoverBtn.onclick = function(){
        if(parseInt(messageFunc.style.top) == 0){
            messageFunc.style.top = -250 + 'px';
        }else{
            messageFunc.style.top = 0 + 'px';
        }  
    }
}

/**
 * 显示微信账号二维码信息
 */
function showQRCode(){
    var wechat = document.querySelector("#wechat");
    var wechatQRC = wechat.querySelector("img");
    var arrow = wechat.querySelector("span");
    wechat.onmouseover = function(){
        wechatQRC.style.display = "block";
        arrow.style.display = "block";
    }
    wechat.onmouseout = function(){
        wechatQRC.style.display = "none";
        arrow.style.display = "none";
    }

}

/**
 * 显示QQ账号二维码信息
 */
function showQRCode1(){
    var qq = document.querySelector("#qq");
    var qqQRC = qq.querySelector("img");
    var arrow = qq.querySelector("span");
    qq.onmouseover = function(){
        qqQRC.style.display = "block";
        arrow.style.display = "block";
        arrow.style.borderTopColor = "rgba(239,239,239,100%)";
    }
    qq.onmouseout = function(){
        qqQRC.style.display = "none";
        arrow.style.display = "none";
    }

}

addLoadEvent(showUserDetail);
addLoadEvent(showAuthorInfo);
addLoadEvent(showMsgFunc);
addLoadEvent(play);

addLoadEvent(showQRCode);
addLoadEvent(showQRCode1);