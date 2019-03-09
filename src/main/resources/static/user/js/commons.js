/**
 * 根据userId从后台获得当前登录用户user
 */
function findUserDetail(userId, user){
    console.log("findUserDetail------userId:" + userId);
    //向后台发送ajax请求获取登录用户详细信息
    $.ajax({
        type: "GET",
        url: PRO_NAME + "/user/userDetail",
        dataType: "json",
        success: function (data) {
            //初始化登录用户
            user = JSON.parse(data);
            console.log("password:" + user.password);
        },
        cache: true,
        async: true
    });
}