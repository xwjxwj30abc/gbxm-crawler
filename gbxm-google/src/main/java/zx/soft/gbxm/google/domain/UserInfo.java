package zx.soft.gbxm.google.domain;

import java.util.Date;

public class UserInfo {

	private String userId;
	private String userName;
	private Date lastUpdateTime;

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", userName=" + userName + ", lastUpdateTime=" + lastUpdateTime + "]";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
