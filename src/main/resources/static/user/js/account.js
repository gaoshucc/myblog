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

var user = null;   //当前登录用户
/**
 * 显示用户详细信息
 */
function showUserDetail() {
    var userinfo = document.querySelector("#user");
    var userDetail = document.querySelector('#user_detail');

    var myProfile = document.querySelectorAll(".myProfile");
    var username = document.querySelector("#username");
    var nicknames = document.querySelectorAll(".nickname");
    var gender = document.querySelector("#gender");
    var position = document.querySelector("#position");
    var experience = document.querySelector("#experience");
    var signature = document.querySelector("#signature");

    findUser();
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
    userinfo.addEventListener('mouseout', function (e) {
        userDetail.style.display = 'none';
    }, false);

    //获得登录用户
    function findUser() {
        //向后台发送ajax请求获取登录用户详细信息
        $.ajax({
            type: "GET",
            url: PRO_NAME + "/user/userDetail",
            dataType: "json",
            success: function (data) {
                //获得登录用户
                user = JSON.parse(data);
                console.log(user);
                fillData();
            },
            async: true
        });
    }

    //填充数据
    function fillData() {
        for (let i = 0; i < myProfile.length; i++) {
            myProfile[i].src = "/" + user.profilePath;
        }
        for (let j = 0; j < nicknames.length; j++) {
            nicknames[j].innerHTML = user.nickname;
        }
        experience.innerHTML = "经验 " + user.experience;
        username.innerHTML = user.username;
        if (user.gender == null) {
            gender.classList.add("icon-pinglun");
        } else {
            if (user.gender == 2) {
                gender.classList.add("icon-nvxing");
            }
            if (user.gender == 1) {
                gender.classList.add("icon-nanxing");
            }
        }
        position.innerHTML = user.position.position;
        signature.innerHTML = user.motto;
    }
}

/**
 * 显示侧边栏
 */
function showAside() {
    var switch_box = document.querySelector("#switch-box");
    var close_aside = document.querySelector("#close-aside");
    var account_funcbox = document.querySelector("#account-funcbox");
    var ani_tags = false;

    switch_box.addEventListener('click', function () {
        if (!ani_tags) {
            account_funcbox.classList.add("hideaside");
        }
        ani_tags = true;
        account_funcbox.classList.toggle("showaside");
    });

    close_aside.addEventListener('click', function () {
        ani_tags = false;
        account_funcbox.classList.toggle("showaside");
    });
}

addLoadEvent(showUserDetail);
addLoadEvent(showAside);

/**
 * 监听点击事件，显示或隐藏“关注的人”列表
 */
function showfollweelist() {
    var $followee = $("#followee");
    var $followeeList = $("#list-followee");
    var tags = false;  //下拉列表是否展开的标志

    $followee.click(function () {
        //如果列表未展开，则查询填充数据并展开
        if (!tags) {
            $.ajax({
                type: "GET",
                url: PRO_NAME + "/user/findFolloweeList",
                dataType: "json",
                success: function (data) {
                    $followeeList.html("");
                    var followeeList = JSON.parse(data);
                    if (followeeList.length > 0) {
                        var followeeListContent = "";
                        console.log(followeeList);
                        for (let i = 0; i < followeeList.length; i++) {
                            followeeListContent = followeeListContent + "<a class='beAttention' data-followee-id='" + followeeList[i].userId + "'>" + followeeList[i].nickname + "</a>";
                        }
                        $followeeList.append(followeeListContent);
                    } else {
                        $followeeList.append("<span class='iconfont icon-meiyoudingdan-01'></span><span id='no-followee'>还没有关注的人哦</span>");
                    }
                    $followee.toggleClass("pulldown");
                    $followeeList.toggle(200, function () {
                    });
                    unfollow();
                    tags = true;
                },
                async: true
            });
        } else {
            $followeeList.html("");
            $followee.toggleClass("pulldown");
            $followeeList.toggle(200, function () {
            });
            tags = false;
        }
    });
}

/**
 * 为“关注的人”列表的每个用户添加“悬浮显示取消关注”事件
 */

/*
function unfollow() {
    //beAttention
    var beAttentions = document.querySelectorAll(".beAttention");
    if(beAttentions != null){
        var $followee = $("#followee");
        var $followeeList = $("#list-followee");
        var followeeList;
        var followeeListContent;
        var unfollowBtn = document.createElement("span");
        unfollowBtn.setAttribute("id","unfollowBtn");
        unfollowBtn.innerHTML = "取消关注";
        //用来定位当前选中的“关注的人”，初始值为-1，表示尚未选中任何人
        var flag = -1;
        //todo 为“取消关注”按钮添加实现
        unfollowBtn.addEventListener("click",function (e) {
            if(flag != -1){
                if(payAttentionTo(beAttentions[flag].getAttribute("data-followee-id"))){
                    $.ajax({
                        type: "GET",
                        url: "/user/findFolloweeList",
                        dataType: "json",
                        success: function (data) {
                            if(!isnull(data)){
                                followeeList = JSON.parse(data);
                                $followeeList.html("");
                                followeeListContent = "";
                                console.log(followeeList);
                                for(let i=0; i<followeeList.length; i++){
                                    //给每个后面加上取消关注按钮
                                    followeeListContent = followeeListContent + "<a class='beAttention' data-followee-id='"+ followeeList[i].userId +"'>"+ followeeList[i].nickname +"</a>";
                                }
                                $followeeList.append(followeeListContent);
                                unfollow();
                            }else{
                                $followeeList.append("没有关注的人哦");
                            }
                            console.log("取消关注了" + flag);
                        },
                        async: true
                    });
                }
            }else {
                return;
            }
        });
        //为“关注的人”添加事件，当鼠标悬浮于某个关注的人上时，显示“取消关注按钮”
        for(let i=0; i<beAttentions.length; i++){
            beAttentions[i].addEventListener("mouseover",function (e) {
                let event = e || event;        //兼容处理
                let from = event.fromElement || event.relatedTarget;//兼容处理
                if(from && this.contains(from)){      //如果在里面则返回
                    return;
                }
                console.log("悬浮了");
                beAttentions[i].appendChild(unfollowBtn);
                flag = i;
            });
            beAttentions[i].addEventListener("mouseout",function (e) {
                let event = e || event;        //兼容处理
                let from = event.fromElement || event.relatedTarget;//兼容处理
                if(from && this.contains(from)){      //如果在里面则返回
                    return;
                }
                console.log("离开了");
                beAttentions[i].removeChild(unfollowBtn);
                flag = -1;
            });
        }
    }
}
*/
/**
 * “关注的人”列表里取消关注
 */
function unfollow() {
    //beAttention
    var beAttentions = document.querySelectorAll(".beAttention");
    if (beAttentions != null) {
        //“取消关注”按钮
        var unfollowBtn = document.createElement("span");
        unfollowBtn.setAttribute("id", "unfollowBtn");
        unfollowBtn.innerHTML = "取消关注";
        //用来定位当前选中的“关注的人”，初始值为-1，表示尚未选中任何人。设置对象类型是因为传参数需要（对象是引用型的）
        var beCancelAttention = {
            flag: -1
        };

        unfollowAndRefresh(unfollowBtn, beCancelAttention);
        showUnfollowBtn(unfollowBtn, beAttentions, beCancelAttention);
    }
}

/**
 * 取消关注并刷新
 * @param unfollowBtn “取消关注”按钮
 * @param beCancelAttention 被取消关注的人的位置
 */
function unfollowAndRefresh(unfollowBtn, beCancelAttention) {
    var beAttentions;
    var $followee = $("#followee");
    var $followeeList = $("#list-followee");
    var followeeList;
    var followeeListContent;
    //todo 为“取消关注”按钮添加实现
    unfollowBtn.addEventListener("click", function (e) {
        console.log("点击了取消关注按钮");
        if (beCancelAttention.flag != -1) {
            //重新获取“关注的人”列表
            beAttentions = document.querySelectorAll(".beAttention");
            if (payAttentionTo(beAttentions[beCancelAttention.flag].getAttribute("data-followee-id"))) {
                console.log("flag1:" + beCancelAttention.flag);
                $.ajax({
                    type: "GET",
                    url: PRO_NAME + "/user/findFolloweeList",
                    dataType: "json",
                    success: function (data) {
                        $followeeList.html("");
                        followeeList = JSON.parse(data);
                        if (followeeList.length > 0) {
                            followeeListContent = "";
                            console.log(followeeList);
                            for (let i = 0; i < followeeList.length; i++) {
                                followeeListContent = followeeListContent + "<a class='beAttention' data-followee-id='" + followeeList[i].userId + "'>" + followeeList[i].nickname + "</a>";
                            }
                            $followeeList.append(followeeListContent);
                            console.log("准备执行showUnfollowBtn了");
                            showUnfollowBtn(unfollowBtn, document.querySelectorAll(".beAttention"), beCancelAttention);
                        } else {
                            $followeeList.append("<span class='iconfont icon-meiyoudingdan-01'></span><span id='no-followee'>还没有关注的人哦</span>");
                        }
                        console.log("取消关注了" + beCancelAttention.flag);
                        showAutoPopup("<span>已取消关注</span>", 120, 60, 3000);
                    },
                    async: true
                });
            }
        } else {
            return;
        }
    });
}

/**
 * 为“关注的人”添加事件，当鼠标悬浮于某个关注的人上时，显示“取消关注按钮”
 * @param unfollowBtn “取消关注”按钮
 * @param beAttentions 关注的人
 * @param flag 定位当前选中的用户名
 */
function showUnfollowBtn(unfollowBtn, beAttentions, beCancelAttention) {
    console.log("进入showUnfollowBtn了");
    for (let i = 0; i < beAttentions.length; i++) {
        console.log("进入showUnfollowBtn循环了" + i);
        beAttentions[i].addEventListener("mouseover", function (e) {
            let event = e || event;        //兼容处理
            let from = event.fromElement || event.relatedTarget;//兼容处理
            if (from && beAttentions[i].contains(from)) {      //如果在里面则返回
                return;
            }
            console.log("悬浮了");
            beAttentions[i].appendChild(unfollowBtn);
            beCancelAttention.flag = i;
        });
        beAttentions[i].addEventListener("mouseout", function (e) {
            let event = e || event;        //兼容处理
            let from = event.toElement || event.relatedTarget;//兼容处理
            if (from && beAttentions[i].contains(from)) {      //如果在里面则返回
                console.log("在里面");
                return;
            }
            console.log("离开了");
            beAttentions[i].removeChild(unfollowBtn);
            beCancelAttention.flag = -1;
        });
    }
}

function showpasswordmanage() {
    var $mdfpwdbtn = $("#mdfpwd-btn");
    var $password_manage = $("#password-manage");
    var $confirm = $("#confirm");
    var $close_password_manage = $("#close-password-manage");

    $mdfpwdbtn.click(function () {
        $password_manage.toggle(200, function () {
        });
    });
    $confirm.click(function () {
        $password_manage.toggle(200, function () {
        });
    });
    $close_password_manage.click(function () {
        $password_manage.toggle(200, function () {
        });
    });
}

/**
 * 修改账号信息弹出层
 */
function showEditPopup() {
    $('#edit-accountInfo').click(function (e) {
        e.preventDefault();  //阻止默认单击事件
        //向后台发送ajax请求获取用户可编辑信息
        $.ajax({
            type: "GET",
            url: PRO_NAME + "/user/editableAccountInfo",
            dataType: "json",
            success: function (data) {
                var editableUserInfo = JSON.parse(data);
                console.log(editableUserInfo);
                showAccountEditPopup(editableUserInfo);
                hideAccountEditPopup();
            },
            async: true
        });
    });
}

/**
 * 回显用户信息并显示编辑窗口
 */
var body;
var editAccountInfoPopup;

function showAccountEditPopup(editableUserInfo) {
    body = document.body;
    editAccountInfoPopup = document.createElement("div");
    editAccountInfoPopup.setAttribute("id", "edit-accountInfo-popup");
    editAccountInfoPopup.innerHTML = "<div id='edit-accountInfo-popup-content'>" +
        "            <span class='iconfont icon-guanbi' id='edit-accountInfo-close' title='关闭'></span>" +
        "            <form id='edit-form' action='/user/submitEditedAccountInfo' enctype='multipart/form-data' method='post'>" +
        "                <h2 id='edit-accountInfo-headline'>修改信息</h2>" +
        "                <div id='editable-accountInfo'>" +
        "                    <span class='editable-info' id='editable-profile'>头像" +
        "                        <img src='/" + editableUserInfo.user.profilePath + "'>" +
        "                        <span id='upload-profile'>更换<input name='editProfile' type='file'></span>" +
        "                    </span>" +
        "                    <span class='editable-info'>昵称" +
        "                        <input type='text' name='editNickname' value='" + editableUserInfo.user.nickname + "'>" +
        "                    </span>" +
        "                    <span class='editable-info'>性别" +
        "                        <span class='radio-box'>" +
        "                            <input type='radio' name='gender' value=2>女" +
        "                        </span>" +
        "                        <span class='radio-box'>" +
        "                            <input type='radio' name='gender' value=1>男" +
        "                        </span>" +
        "                    </span>" +
        "                    <span class='editable-info'>职位" +
        "                        <select name='editPosition' id='position-select'>" +
        "                        </select>" +
        "                    </span>" +
        "                    <span class='editable-info'>个性签名" +
        "                        <input type='text' name='editMotto' value='" + editableUserInfo.user.motto + "'>" +
        "                    </span>" +
        "                </div>" +
        "                <input id='submit-edited-accountInfo' type='submit' value='提交修改'>" +
        "            </form>" +
        "        </div>" +
        "        <div id='edit-accountInfo-popup-bg'></div>";
    body.appendChild(editAccountInfoPopup);
    if (editableUserInfo.positions != null) {
        var currentPositionId = editableUserInfo.user.position.positionId;
        var positions = editableUserInfo.positions;
        var positionSelect = document.querySelector("#position-select");
        var options = "";
        for (let i = 0; i < positions.length; i++) {
            if (currentPositionId == positions[i].positionId) {
                options = options + "<option " + "selected" + " value=" + positions[i].positionId + ">" + positions[i].position + "</option>";
            } else {
                options = options + "<option value=" + positions[i].positionId + ">" + positions[i].position + "</option>";
            }
        }
        positionSelect.innerHTML = options;
    }
    //校验修改后数据
    form_validate();
}

/**
 * 移除编辑窗口
 */
function hideAccountEditPopup() {
    var editAccountInfoClose = document.querySelector("#edit-accountInfo-close");
    editAccountInfoClose.addEventListener("click", function (e) {
        body.removeChild(editAccountInfoPopup);
    });
}

/**
 * 校验编辑信息
 */
function form_validate() {
    $("#edit-form").validate({
        rules: {
            editNickname: {
                required: true
            },
            gender: {
                required: true
            },
            editPosition: {
                required: true
            }
        },
        messages: {
            editNickname: {
                required: "昵称不能为空"
            },
            gender: {
                required: "性别不能为空"
            },
            editPosition: {
                required: "职位不能为空"
            }
        }
    });
}

$(function () {
    showEditPopup();
    showfollweelist();
    showpasswordmanage();
})

/**
 * 头像修改弹出层动画（可以用于查看头像）
 */
function showEditProfile() {
    $('#profile-close,#profile-revisability,#profile-popup-bg').click(function (e) {
        e.preventDefault();  //阻止默认单击事件
        $('#profile-popup').toggleClass('show');
    });
}