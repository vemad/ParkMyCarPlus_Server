package com.pmc.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.Entity;

/**
 * Created by Gaetan on 11/03/2015.
 */
@JsonAutoDetect
public class UserLevel {
    private int indexLevel;
    private int startScore;
    private String levelName;

    public UserLevel(int indexLevel, String levelName, int startScore) {
        this.indexLevel = indexLevel;
        this.startScore = startScore;
        this.levelName = levelName;
    }

    public int getStartScore() {
        return startScore;
    }

    public int getNextLevelScore() {
        if(this.indexLevel+1 >= levels.length){
            return 999999999;
        }
        else{
            return levels[this.indexLevel+1].getStartScore();
        }
    }

    public String getNextLevelName() {
        if(this.indexLevel+1 >= levels.length){
            return "";
        }
        else{
            return levels[this.indexLevel+1].getLevelName();
        }
    }

    public String getLevelName() {
        return levelName;
    }

    public static UserLevel getLevel(int score){
        for(UserLevel level : levels){
            if(level.startScore <= score && score < level.getNextLevelScore()){
                return level;
            }
        }
        return levels[levels.length-1];
    }

    private static UserLevel[] levels={
            new UserLevel(0, "Touriste", 0),
            new UserLevel(1, "Utilisateur confirmé", 100),
            new UserLevel(2, "Citoyen", 200),
            new UserLevel(3, "Maire", 300),
            new UserLevel(4, "Prefet", 400),
            new UserLevel(5, "Ministre", 500)
    };
}
