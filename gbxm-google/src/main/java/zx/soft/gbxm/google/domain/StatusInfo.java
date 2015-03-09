package zx.soft.gbxm.google.domain;

import java.util.Date;

public class StatusInfo {

	private String statusId;
	private Date published;
	private Date updated;
	private String url;
	private String content;

	@Override
	public String toString() {
		return "StatusInfo [statusId=" + statusId + ", published=" + published + ", updated=" + updated + ", url="
				+ url + ", content=" + content + "]";
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
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

}
