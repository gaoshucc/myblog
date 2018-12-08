package com.funnycode.myblog.pojo.PO;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-12-03 12:47
 */
public class Position {
    private Integer positionId;
    private String position;
    private List<User> users;

    public Position(Integer i) {
        this.positionId = 1;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Position(Integer positionId, String position) {
        this.positionId = positionId;
        this.position = position;
    }
}

