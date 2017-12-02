package models;

import io.ebean.*;

import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

/**
 * Context entity managed by Ebean
 */
@Entity
@Table(name="context")
public class Context extends Model {
    public String previousKeyword;
    public String previousKeywordType;

    public String currentKeyword;
    public String currentKeywordType;

    public String afterKeyword;
    public String afterKeywordType;

    public Integer frequency;
    public Boolean isEnd;
    public Boolean isAbleToBegin;

    public Integer totalLength;
//    created_at
//updated_at
    public static final Finder<String,Context> find = new Finder<String,Context>(Context.class);
}

