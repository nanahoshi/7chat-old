package models;

import io.ebean.*;

import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

/**
 * Conversation entity managed by Ebean
 */
@Entity
@Table(name="conversations")
public class Conversation extends Model {
    public String channel;
    public String userId;

    public String conversationId;

    public String content;
    public String reply;

    public static final Finder<String,Conversation> find = new Finder<String,Conversation>(Conversation.class);
}

