package cn.edu.zucc.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author liangruuu
 */
@Table(name = "report_videos")
public class ReportVideos {
    @Id
    private String id;

    @Column(name = "report_user_id")
    private Integer reportUserId;

    @Column(name = "report_video_id")
    private Integer reportVideoId;

    /**
     * 举报类型（字符串数组）
     */
    private String title;

    private String content;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "create_time")
    private Date createTime;

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
     * @return report_user_id
     */
    public Integer getReportUserId() {
        return reportUserId;
    }

    /**
     * @param reportUserId
     */
    public void setReportUserId(Integer reportUserId) {
        this.reportUserId = reportUserId;
    }

    /**
     * @return report_video_id
     */
    public Integer getReportVideoId() {
        return reportVideoId;
    }

    /**
     * @param reportVideoId
     */
    public void setReportVideoId(Integer reportVideoId) {
        this.reportVideoId = reportVideoId;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}