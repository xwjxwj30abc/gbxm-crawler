package zx.soft.gbxm.twitter.domain;

public class StatusInfo {

	private long id;
	private long userId;
	private String username;
	private long retweetedStatusId;
	private long retweetedUserId;
	private String retweetedUserName = "";
	private String text = "";
	private int favoriteCount;
	private int retweetCount;
	private String createdAt = "";
	private String placename = "";

	@Override
	public String toString() {
		return "StatusInfo [statusId=" + id + ", userId=" + userId + ", username=" + username + ", retweetedStatusId="
				+ retweetedStatusId + ", retweetedUserId=" + retweetedUserId + ", retweetedUserName="
				+ retweetedUserName + ", text=" + text + ", favoriteCount=" + favoriteCount + ", retweetCount="
				+ retweetCount + ", createdAt=" + createdAt + ", placename=" + placename + "]";
	}

	public StatusInfo() {
		//
	}

	public long getStatusId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getRetweetedStatusId() {
		return retweetedStatusId;
	}

	public void setRetweetedStatusId(long retweetedStatusId) {
		this.retweetedStatusId = retweetedStatusId;
	}

	public long getRetweetedUserId() {
		return retweetedUserId;
	}

	public void setRetweetedUserId(long retweetedUserId) {
		this.retweetedUserId = retweetedUserId;
	}

	public String getRetweetedUserName() {
		return retweetedUserName;
	}

	public void setRetweetedUserName(String retweetedUserName) {
		this.retweetedUserName = retweetedUserName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getPlacename() {
		return placename;
	}

	public void setPlacename(String placename) {
		this.placename = placename;
	}

	/*	private Date createdAt;//该微博创建时间
		private long statusId;//该微博id
		private String text;//该微博的文本信息
		private String source;//该微博的来源
		private int favoriteCount = 0;//该微博被收藏总数
		private String address = "";//地址
		private String countryCode = "";//国家编码
		private long retweetCount = 0;//该微博总的转发数量
		private long retweetedStatusId = 0L;//返回转发的微博id
		private long userId;//发表该状态的用户id

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
	 */

}
