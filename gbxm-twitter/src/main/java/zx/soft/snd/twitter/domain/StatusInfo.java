package zx.soft.snd.twitter.domain;

import java.util.Date;

public class StatusInfo {
	private Date createdAt;//该微博创建时间
	private long statusId;//该微博id
	private String text;//该微博的文本信息
	private String source;//该微博的来源
	private int favoriteCount = 0;//该微博被收藏总数
	private String address = "";//地址
	private String countryCode = "";//国家编码
	private long retweetCount = 0;//该微博总的转发数量
	private long retweetedStatusId = 0L;//返回转发的微博id
	private long userId;

	public StatusInfo() {
		//
	}

	public StatusInfo(Date createdAt, long statusId, String text, String source, int favoriteCount, String address,
			String countryCode, long retweetCount, long retweetedStatusId, long userId) {
		super();
		this.createdAt = createdAt;
		this.statusId = statusId;
		this.text = text;
		this.source = source;
		this.favoriteCount = favoriteCount;
		this.address = address;
		this.countryCode = countryCode;
		this.retweetCount = retweetCount;
		this.retweetedStatusId = retweetedStatusId;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "StatusInfo [createdAt=" + createdAt + ", statusId=" + statusId + ", text=" + text + ", source="
				+ source + ", favoriteCount=" + favoriteCount + ", address=" + address + ", countryCode=" + countryCode
				+ ", retweetCount=" + retweetCount + ", retweetedStatusId=" + retweetedStatusId + ", userId=" + userId
				+ "]";
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public long getStatusId() {
		return statusId;
	}

	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public long getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(long retweetCount) {
		this.retweetCount = retweetCount;
	}

	public long getRetweetedStatusId() {
		return retweetedStatusId;
	}

	public void setRetweetedStatusId(long retweetedStatusId) {
		this.retweetedStatusId = retweetedStatusId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
