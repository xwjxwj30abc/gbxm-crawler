package zx.soft.gbxm.google.domain;

import java.sql.Timestamp;

public class StatusInfo {

	private String titile = "";//此动态的标题
	private Timestamp published;//此动态最初发布的时间。格式采用 RFC 3339 时间戳
	private Timestamp updated;//此动态最后一次更新的时间，格式采用 RFC 3339 时间戳
	private String id = "";//此动态的 ID
	private String url = "";//指向此动态的链接
	private String actorId = "";//执行人的个人资源 ID
	private String actorDisplayName = "";//执行人姓名（适合用于显示）
	private String actorFamilyNamegivenName = "";//	执行人的姓氏+名字
	private String actorUrl = "";//指向执行人的 Google 个人资料的链接
	private String objectId = "";//对象的 ID。转发动态时，它就是转发的动态的 ID
	private String objectActorId = "";//原始执行人的 ID
	private String objectActorDisplayName = "";//原始执行人姓名（适合用于显示）
	private String objectActorUrl = "";//指向原始执行人的 Google 个人资料的链接
	private String objectContent = "";//HTML 格式内容（适合用于显示）
	private String objectUrl = "";//指向链接资源的网址
	private int repliesTotalItems;//此动态的评论总数
	private int resharersTotalItems;//转发此动态的人员总数
	private String placeName = "";//此动态发生地点的名称

	@Override
	public String toString() {
		return "StatusInfo [titile=" + titile + ", published=" + published + ", updated=" + updated + ", id=" + id
				+ ", url=" + url + ", actorId=" + actorId + ", actorDisplayName=" + actorDisplayName
				+ ", actorFamilyNamegivenName=" + actorFamilyNamegivenName + ", actorUrl=" + actorUrl + ", objectId="
				+ objectId + ", objectActorId=" + objectActorId + ", objectActorDisplayName=" + objectActorDisplayName
				+ ", objectActorUrl=" + objectActorUrl + ", objectContent=" + objectContent + ", objectUrl="
				+ objectUrl + ", repliesTotalItems=" + repliesTotalItems + ", resharersTotalItems="
				+ resharersTotalItems + ", placeName=" + placeName + "]";
	}

	public String getTitile() {
		return titile;
	}

	public void setTitile(String titile) {
		this.titile = titile;
	}

	public Timestamp getPublished() {
		return published;
	}

	public void setPublished(Timestamp published) {
		this.published = published;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String getActorDisplayName() {
		return actorDisplayName;
	}

	public void setActorDisplayName(String actorDisplayName) {
		this.actorDisplayName = actorDisplayName;
	}

	public String getActorFamilyNamegivenName() {
		return actorFamilyNamegivenName;
	}

	public void setActorFamilyNamegivenName(String actorFamilyNamegivenName) {
		this.actorFamilyNamegivenName = actorFamilyNamegivenName;
	}

	public String getActorUrl() {
		return actorUrl;
	}

	public void setActorUrl(String actorUrl) {
		this.actorUrl = actorUrl;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectActorId() {
		return objectActorId;
	}

	public void setObjectActorId(String objectActorId) {
		this.objectActorId = objectActorId;
	}

	public String getObjectActorDisplayName() {
		return objectActorDisplayName;
	}

	public void setObjectActorDisplayName(String objectActorDisplayName) {
		this.objectActorDisplayName = objectActorDisplayName;
	}

	public String getObjectActorUrl() {
		return objectActorUrl;
	}

	public void setObjectActorUrl(String objectActorUrl) {
		this.objectActorUrl = objectActorUrl;
	}

	public String getObjectContent() {
		return objectContent;
	}

	public void setObjectContent(String objectContent) {
		this.objectContent = objectContent;
	}

	public String getObjectUrl() {
		return objectUrl;
	}

	public void setObjectUrl(String objectUrl) {
		this.objectUrl = objectUrl;
	}

	public int getRepliesTotalItems() {
		return repliesTotalItems;
	}

	public void setRepliesTotalItems(int repliesTotalItems) {
		this.repliesTotalItems = repliesTotalItems;
	}

	public int getResharersTotalItems() {
		return resharersTotalItems;
	}

	public void setResharersTotalItems(int resharersTotalItems) {
		this.resharersTotalItems = resharersTotalItems;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	/*
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
	}*/

}
