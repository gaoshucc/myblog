package com.funnycode.myblog.pojo.VO;

/**
 * @author gaoshucc
 * @create 2019-01-06 16:19
 */
public class FavoriteVO {
    private String articleId;
    private String articleTitle;
    private String authorNickname;

    @Override
    public String toString() {
        return "FavoriteVO{" +
                "articleId='" + articleId + '\'' +
                ", articleTitle='" + articleTitle + '\'' +
                ", authorNickname='" + authorNickname + '\'' +
                '}';
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }
}
