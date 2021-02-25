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
    private String reportUserId;

    @Column(name = "report_video_id")
    private String reportVideoId;

    /**
     * 举报类型（字符串数组）
     */
    private String title;

    private String content;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "create_time")
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportUserId() {
        return reportUserId;
    }

    public void setReportUserId(String reportUserId) {
        this.reportUserId = reportUserId;
    }

    public String getReportVideoId() {
        return reportVideoId;
    }

    public void setReportVideoId(String reportVideoId) {
        this.reportVideoId = reportVideoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}