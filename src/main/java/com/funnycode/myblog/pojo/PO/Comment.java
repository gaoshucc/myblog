package com.funnycode.myblog.pojo.PO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author gaoshucc
 * @create 2018-12-03 21:40
 */
public class Comment {
    private String commentId;
    private Note note;
    private User user;
    private String parentId;
    private String commentContent;
    private String commentTime;

    public Comment(String userId, String noteId, String commentContent) {
        User user = new User();
        user.setUserId(userId);
        Note note = new Note();
        note.setNoteId(noteId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.commentId = UUID.randomUUID().toString().replace("-","");
        this.user = user;
        this.note = note;
        this.parentId = "0";
        this.commentContent = commentContent;
        this.commentTime = sdf.format(new Date());
    }

    public Comment(String userId, String noteId, String byReplyId, String commentContent) {
        User user = new User();
        user.setUserId(userId);
        Note note = new Note();
        note.setNoteId(noteId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.commentId = UUID.randomUUID().toString().replace("-","");
        this.user = user;
        this.note = note;
        this.parentId = byReplyId;
        this.commentContent = commentContent;
        this.commentTime = sdf.format(new Date());
    }

    public Comment() {

    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }
}
