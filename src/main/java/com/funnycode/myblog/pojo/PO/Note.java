package com.funnycode.myblog.pojo.PO;

import java.util.UUID;

/**
 * @author gaoshucc
 * @create 2018-11-29 19:22
 */
public class Note {
    private String noteId;
    private String noteTitle;
    private String createTime;
    /**
     * 0代表已删除，1代表已发布，2代表保存
     */
    private Integer status;
    private NoteType noteType;
    private User blogger;
    private String noteHtml;
    private Integer commentCount;

    public Note() {
    }

    public Note(String userId, String title, NoteType noteType, String noteHtml){
        User user = new User();
        user.setUserId(userId);

        this.noteId = UUID.randomUUID().toString().replace("-","");
        this.noteTitle = title;
        this.noteType = noteType;
        this.blogger = user;
        this.status = 1;
        this.noteHtml = noteHtml;
        this.commentCount = 0;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public User getBlogger() {
        return blogger;
    }

    public void setBlogger(User blogger) {
        this.blogger = blogger;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNoteHtml() {
        return noteHtml;
    }

    public void setNoteHtml(String noteHtml) {
        this.noteHtml = noteHtml;
    }
}
