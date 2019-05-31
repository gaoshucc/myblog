$(function () {
    deleteNote();
})

function deleteNote() {
    var deleteNote = document.querySelector("#delete");
    deleteNote.addEventListener("click", function (e) {
        var ifDelete = confirm("永久删除该手记（不可恢复），是否删除？");
        if (ifDelete) {
            console.log("删除成功");
            location.href = "/user/noteArticle";
        }
    })
}

function form_validate() {
    $("#note-form").validate({
        rules: {
            title: {
                required: true
            },
            typeId: {
                required: true
            },
            markdownDoc: {
                required: true
            }
        },
        messages: {
            title: {
                required: "标题不能为空"
            },
            typeId: {
                required: "类型不能为空"
            },
            markdownDoc: {
                required: "手记内容不能为空"
            }
        }
    });
}
