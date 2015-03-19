package zx.soft.gbxm.google.domain;

import java.sql.Timestamp;

public class StatusInfo {

	private String statusId;//状态id
	private Timestamp published;//状态发布时间
	private String url;//状态对应的url
	private String content;//具体状态内容
	private String source;//标记该状态的来源即发布该状态的用户id

	@Override
	public String toString() {
		return "StatusInfo [statusId=" + statusId + ", published=" + published + ", url=" + url + ", content="
				+ content + ", source=" + source + "]";
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public Timestamp getPublished() {
		return published;
	}

	public void setPublished(Timestamp dateTime) {
		this.published = dateTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
