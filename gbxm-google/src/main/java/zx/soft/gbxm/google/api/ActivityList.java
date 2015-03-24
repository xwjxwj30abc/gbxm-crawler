package zx.soft.gbxm.google.api;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.gbxm.google.domain.StatusInfo;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.Activities.List;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;

public class ActivityList {

	private static Logger logger = LoggerFactory.getLogger(ActivityList.class);
	private Plus plus;
	private static final int PAGE_COUNT = 5;

	public ActivityList(Plus plus) {
		this.plus = plus;
	}

	/**
	 * 根据用户id，获取指定截止时间lastUpdatedTime前的状态信息
	 * @param userId
	 * @param lastUpdatedTime
	 * @return
	 * @throws IOException
	 */
	public ArrayList<StatusInfo> getActivitiesByUserId(String userId, long lastUpdatedTime) throws IOException {
		ArrayList<StatusInfo> statusInfos = new ArrayList<>();
		List listActivities = plus.activities().list(userId, "public");
		listActivities.setMaxResults(100L);//最大可设置为100
		listActivities
		.setFields("nextPageToken,items(title,published,updated,id,url,actor/id,actor/displayName,actor/name/familyName"
				+ ",actor/name/givenName,actor/url,object/actor/id,object/actor/id,object/actor/displayName,object/actor/url,"
				+ "object/content,object/url,object/replies/totalItems,object/resharers/totalItems,placeName)");
		ActivityFeed feed = listActivities.execute();
		int currentPageNumber = 0;
		while (feed.getItems() != null && !feed.getItems().isEmpty() && ++currentPageNumber < PAGE_COUNT) {
			logger.info("currentPageCount=" + currentPageNumber);
			if (feed.getItems().get(0).getPublished().getValue() <= lastUpdatedTime) {
				break;
			}
			for (Activity activity : feed.getItems()) {
				logger.info(activity.getId());
				StatusInfo statusInfo = new StatusInfo();
				if (activity.getTitle() != null) {
					statusInfo.setTitile(activity.getTitle());
				}
				statusInfo.setPublished(new Timestamp(activity.getPublished().getValue()));
				if (activity.getUpdated() != null) {
					statusInfo.setUpdated(new Timestamp(activity.getUpdated().getValue()));
				}
				statusInfo.setId(activity.getId());
				statusInfo.setUrl(activity.getUrl());
				if (activity.getActor() != null) {
					statusInfo.setActorId(activity.getActor().getId());
					statusInfo.setActorDisplayName(activity.getActor().getDisplayName());
					statusInfo.setActorUrl(activity.getActor().getUrl());
				}
				if (activity.getActor().getName() != null) {
					statusInfo.setActorFamilyNamegivenName(activity.getActor().getName().getFamilyName()
							+ activity.getActor().getName().getGivenName());
				}
				if (activity.getObject().getId() != null) {
					statusInfo.setObjectId(activity.getObject().getId());
				}
				if (activity.getObject().getContent() != null) {
					statusInfo.setObjectContent(activity.getObject().getContent());

				}
				if (activity.getObject().getUrl() != null) {
					statusInfo.setObjectUrl(activity.getObject().getUrl());
				}
				if (activity.getObject().getActor() != null) {
					statusInfo.setObjectActorId(activity.getObject().getActor().getId());
					statusInfo.setObjectActorDisplayName(activity.getObject().getActor().getDisplayName());
					statusInfo.setObjectActorUrl(activity.getObject().getActor().getUrl());
				}
				if (activity.getObject().getReplies().getTotalItems() != null) {
					statusInfo.setRepliesTotalItems(activity.getObject().getReplies().getTotalItems().intValue());
				}
				if (activity.getObject().getResharers().getTotalItems() != null) {
					statusInfo.setResharersTotalItems(activity.getObject().getResharers().getTotalItems().intValue());
				}
				if (activity.getPlaceName() != null) {
					statusInfo.setPlaceName(activity.getPlaceName());
				}
				statusInfos.add(statusInfo);
			}

			String nextPageToken = feed.getNextPageToken();
			if (nextPageToken == null) {
				break;
			}
			listActivities.setPageToken(nextPageToken);
			feed = listActivities.execute();
		}
		return statusInfos;
	}

	public Plus getPlus() {
		return plus;
	}

	public void setPlus(Plus plus) {
		this.plus = plus;
	}

}
