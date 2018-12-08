package com.funnycode.myblog.pojo.VO;

import com.funnycode.myblog.pojo.PO.NoteType;

/**
 * @author gaoshucc
 * @create 2018-12-04 0:45
 */
public class NoteVO {
    private String noteId;
    private String noteTitle;
    private String createTime;
    private NoteType noteType;
    private UserVO blogger;
    private String noteHtml;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public UserVO getBlogger() {
        return blogger;
    }

    public void setBlogger(UserVO blogger) {
        this.blogger = blogger;
    }

    public String getNoteHtml() {
        return noteHtml;
    }

    public void setNoteHtml(String noteHtml) {
        this.noteHtml = noteHtml;
    }
}
