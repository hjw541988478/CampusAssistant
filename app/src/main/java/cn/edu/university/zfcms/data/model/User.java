package cn.edu.university.zfcms.data.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by hjw on 16/4/15.
 */
public class User extends BmobUser{
    public String userId;
    public String userName;
    public String userPswd;

    public String userCollege;
    public String userMajor;
    public String userClass;

    public String checkCode;

    public User(){}
    public User(User user){
        this.userId = user.userId;
        this.userName = user.userName;
        this.userPswd = user.userPswd;
    }

    public boolean isEmpty(){
        if (userId == null || userName == null || userPswd == null)
            return true;
        return userId.isEmpty()
                && userName.isEmpty()
                && userPswd.isEmpty();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPswd='" + userPswd + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if ( o == this)
            return true;
        if ( o instanceof User) {
            User anotherUser = (User) o;
            return anotherUser.userId.equals(this.userId)
                    && anotherUser.userName.equals(this.userName)
                    && anotherUser.userCollege.equals(this.userCollege)
                    && anotherUser.userMajor.equals(this.userMajor)
                    && anotherUser.userClass.equals(this.userClass);
        }
        return false;
    }
}
