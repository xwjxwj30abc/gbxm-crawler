package zx.soft.snd.twitter.domain;

import java.util.Date;

import twitter4j.Status;
import twitter4j.URLEntity;

public class UserInfo {
	private long id;
	private String name;
	private String screenName;
	private String location;
	private String description;
	private URLEntity urlEntity;
	private boolean isContributorsEnabled;
	private String profileImageUrl;
	private String profileImageUrlHttps;
	private boolean isDefaultProfileImage;
	private String url;
	private boolean isProtected;
	private int followersCount;

	private Status status;

	private String profileBackgroundColor;
	private String profileTextColor;
	private String profileLinkColor;
	private String profileSidebarFillColor;
	private String profileSidebarBorderColor;
	private boolean profileUseBackgroundImage;
	private boolean isDefaultProfile;
	private boolean showAllInlineMedia;
	private int friendsCount;
	private Date createdAt;
	private int favouritesCount;
	private int utcOffset;
	private String timeZone;
	private String profileBackgroundImageUrl;
	private String profileBackgroundImageUrlHttps;
	private String profileBannerImageUrl;
	private boolean profileBackgroundTiled;
	private String lang;
	private int statusesCount;
	private boolean isGeoEnabled;
	private boolean isVerified;
	private boolean translator;
	private int listedCount;
	private boolean isFollowRequestSent;

}
