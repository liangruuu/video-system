package cn.edu.zucc.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author liangruuu
 */
@Table(name = "users_collect_videos")
public class UsersCollectVideos {
    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "video_id")
    private String videoId;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return video_id
     */
    public String getVideoId() {
        return videoId;
    }

    /**
     * @param videoId
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}