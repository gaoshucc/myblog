package com.funnycode.myblog.pojo.VO;

/**
 * @author gaoshucc
 * @create 2018-12-26 20:27
 */
public class AuthorVO {
    private String authorId;
    private String nickname;
    private String position;
    private String profilePath;
    private Integer noteCount;
    private Integer answerCount;

    public AuthorVO(String userId, String nickname, String position, String profilePath, Integer noteCount, Integer answerCount) {
        this.authorId = userId;
        this.nickname = nickname;
        this.position = position;
        this.profilePath = profilePath;
        this.noteCount = noteCount;
        this.answerCount = answerCount;
    }

    public Integer getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(Integer noteCount) {
        this.noteCount = noteCount;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public String toString() {
        return "AuthorVO{" +
                "authorId='" + authorId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", position='" + position + '\'' +
                ", profilePath='" + profilePath + '\'' +
                ", noteCount=" + noteCount +
                ", answerCount=" + answerCount +
                '}';
    }
}
