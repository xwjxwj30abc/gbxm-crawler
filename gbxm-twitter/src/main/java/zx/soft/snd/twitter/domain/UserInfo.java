package zx.soft.snd.twitter.domain;

import java.util.Date;

import twitter4j.User;

public class UserInfo {

	private long userId;//用户id
	private String name = "";//用户昵称
	private String screenName = "";//用户名
	private String location = "";//用户地址
	private String description = "";//描述
	private String url = "";//用户主页地址
	private int followersCount = 0;//关注数量
	private int friendsCount = 0;//好友数量
	private Date createdAt = new Date();//用户创建时间
	private int favouritesCount = 0;//收藏数
	private int listedCount = 0;//用户关注的公共列表数
	private long lastStatusId = 0L;//用户最近一条状态的状态Id
	private int statusesCount = 0;//用户总的状态数

	public UserInfo() {
		//
	}

	public UserInfo(User user) {
		this.userId = user.getId();
		this.name = user.getName();
		this.screenName = user.getScreenName();
		this.location = user.getLocation();
		this.description = user.getDescription();
		this.url = user.getURL();
		this.followersCount = user.getFollowersCount();
		this.friendsCount = user.getFriendsCount();
		this.createdAt = user.getCreatedAt();
		this.favouritesCount = user.getFavouritesCount();
		this.listedCount = user.getListedCount();
		this.lastStatusId = user.getStatus().getId();
		this.statusesCount = user.getStatusesCount();
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", name=" + name + ", screenName=" + screenName + ", location="
				+ location + ", description=" + description + ", url=" + url + ", followersCount=" + followersCount
				+ ", friendsCount=" + friendsCount + ", createdAt=" + createdAt + ", favouritesCount="
				+ favouritesCount + ", listedCount=" + listedCount + ", lastStatusId=" + lastStatusId
				+ ", statusesCount=" + statusesCount + "]";
	}

	public UserInfo(long userId, String name, String screenName, String location, String description, String url,
			int followersCount, int friendsCount, Date createdAt, int favouritesCount, int listedCount,
			long lastStatusId, int statusesCount) {
		super();
		this.userId = userId;
		this.name = name;
		this.screenName = screenName;
		this.location = location;
		this.description = description;
		this.url = url;
		this.followersCount = followersCount;
		this.friendsCount = friendsCount;
		this.createdAt = createdAt;
		this.favouritesCount = favouritesCount;
		this.listedCount = listedCount;
		this.lastStatusId = lastStatusId;
		this.statusesCount = statusesCount;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getFavouritesCount() {
		return favouritesCount;
	}

	public void setFavouritesCount(int favouritesCount) {
		this.favouritesCount = favouritesCount;
	}

	public int getListedCount() {
		return listedCount;
	}

	public void setListedCount(int listedCount) {
		this.listedCount = listedCount;
	}

	public long getLastStatusId() {
		return lastStatusId;
	}

	public void setLastStatusId(long lastStatusId) {
		this.lastStatusId = lastStatusId;
	}

	public int getStatusesCount() {
		return statusesCount;
	}

	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}

}
