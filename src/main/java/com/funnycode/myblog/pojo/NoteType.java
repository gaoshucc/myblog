package com.funnycode.myblog.pojo;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-11-30 23:39
 */
public class NoteType {
    private Integer typeId;
    private String typeName;
    private List<Note> notes;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
