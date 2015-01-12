package zx.soft.snd.twitter.domain;

import java.util.Date;

import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.Scopes;
import twitter4j.Status;
import twitter4j.User;

public class StatusInfo {
	private Date createdAt;
	private long id;
	private String text;
	private String source;
	private boolean isTruncated;
	private long inReplyToStatusId;
	private long inReplyToUserId;
	private boolean isFavorited;
	private boolean isRetweeted;
	private int favoriteCount;
	private String inReplyToScreenName;
	private GeoLocation geoLocation = null;
	private Place place = null;
	private long retweetCount;
	private boolean isPossiblySensitive;
	private String lang;
	private Status retweetedStatus;
	private long currentUserRetweetId = -1L;
	private Scopes scopes;
	private User user = null;
}
