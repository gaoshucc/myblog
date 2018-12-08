package com.funnycode.myblog.pojo.VO;

import com.funnycode.myblog.pojo.PO.Position;
import com.funnycode.myblog.pojo.PO.User;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-12-08 17:24
 */
public class EditableUserInfoVO {
    private User user;
    private List<Position> positions;

    public EditableUserInfoVO(User editableUserInfo, List<Position> positions) {
        this.user = editableUserInfo;
        this.positions = positions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}
