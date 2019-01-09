package com.funnycode.myblog.pojo.VO;

import com.funnycode.myblog.pojo.PO.Note;

/**
 * @author gaoshucc
 * @create 2019-01-07 19:10
 */
public class FavoritesDetailVO {
    private Note collectNote;
    private String collectTime;
    private Integer readCount;

    @Override
    public String toString() {
        return "FavoritesDetailVO{" +
                "collectNote=" + collectNote +
                ", collectTime='" + collectTime + '\'' +
                ", readCount=" + readCount +
                '}';
    }

    public Note getCollectNote() {
        return collectNote;
    }

    public void setCollectNote(Note collectNote) {
        this.collectNote = collectNote;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }
}
