package cn.edu.university.zfcms.storage.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjw on 16/4/15.
 */
public class User {

    public String userId;
    public String userPswd;
    public String userRealName;
    public String userCollege;
    public String userMajor;
    public String userClass;

    public String userUniversity;
    public List<String> userYears;
    public List<String> userTerms;

    public User(){}

    public User(String userName, String userPswd, String userRealName) {
        this.userId = userName;
        this.userRealName = userRealName;
        this.userPswd = userPswd;
    }

    public void setPersonalData(String userRealName, String userCollege, String userMajor
            , String userClass) {
        this.userRealName = userRealName;
        this.userCollege = userCollege;
        this.userMajor = userMajor;
        this.userClass = userClass;
    }

    private List<String> parsePersonalYears(String userId) {
        List<String> personalYears = new ArrayList<>();

        return personalYears;
    }

    public User(User user) {
        this.userRealName = user.userRealName;
        this.userCollege = user.userCollege;
        this.userMajor = user.userMajor;
        this.userClass = user.userClass;
        this.userYears = user.userYears;
        this.userTerms = user.userTerms;
    }



    @Override
    public boolean equals(Object o) {
        if ( o == this)
            return true;
        if ( o instanceof User) {
            User anotherUser = (User) o;
            return anotherUser.userId.equals(this.userId)
                    && anotherUser.userRealName.equals(this.userRealName)
                    && anotherUser.userCollege.equals(this.userCollege)
                    && anotherUser.userMajor.equals(this.userMajor)
                    && anotherUser.userClass.equals(this.userClass);
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userPswd='" + userPswd + '\'' +
                ", userRealName='" + userRealName + '\'' +
                ", userCollege='" + userCollege + '\'' +
                ", userMajor='" + userMajor + '\'' +
                ", userClass='" + userClass + '\'' +
                ", userUniversity='" + userUniversity + '\'' +
                ", userYears=" + userYears +
                ", userTerms=" + userTerms +
                '}';
    }
}
