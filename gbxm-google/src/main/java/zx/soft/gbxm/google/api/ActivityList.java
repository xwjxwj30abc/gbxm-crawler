package zx.soft.gbxm.google.api;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.gbxm.google.domain.StatusInfo;
import zx.soft.model.status.GooglePlusStatus;

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
	public ArrayList<GooglePlusStatus> getActivitiesByUserId(String userId, long lastUpdatedTime) throws IOException {
		ArrayList<GooglePlusStatus> googlePlusStatuses = new ArrayList<>();
		List listActivities = plus.activities().list(userId, "public");
		listActivities.setMaxResults(100L);//最大可设置为100
		listActivities.setFields("nextPageToken,items(id,title,published,updated,url,actor/id,actor/displayName,"
				+ "object/id,object/actor/id,object/actor/displayName,object/originalContent,object/url,"
				+ "object/replies/totalItems,object/plusoners/totalItems,object/resharers/totalItems,"
				+ "object/attachments,annotation,geocode,placeName)");
		ActivityFeed feed = listActivities.execute();
		int currentPageNumber = 0;
		while (feed.getItems() != null && !feed.getItems().isEmpty() && ++currentPageNumber < PAGE_COUNT) {
			logger.info("currentPageCount=" + currentPageNumber);
			if (feed.getItems().get(0).getPublished().getValue() <= lastUpdatedTime) {
				break;
			}
			for (Activity activity : feed.getItems()) {
				logger.info(activity.getId());
				GooglePlusStatus googlePlusStatus = new GooglePlusStatus();
				googlePlusStatus.setId(activity.getId());
				googlePlusStatus.setTitle(activity.getTitle());
				googlePlusStatus.setPublished(activity.getPublished().toStringRfc3339());
				googlePlusStatus.setUpdated(activity.getUpdated().toStringRfc3339());
				googlePlusStatus.setUrl(activity.getUrl());
				googlePlusStatus.setActor_id(activity.getActor().getId());
				googlePlusStatus.setActor_display_name(activity.getActor().getDisplayName());
				googlePlusStatus.setObject_id(activity.getObject().getId());
				googlePlusStatus.setObject_actor_id(activity.getObject().getActor().getId());
				googlePlusStatus.setObject_actor_display_name(activity.getObject().getActor().getDisplayName());
				googlePlusStatus.setObject_original_content(activity.getObject().getOriginalContent());
				googlePlusStatus.setObject_url(activity.getObject().getUrl());
				googlePlusStatus.setObject_replies_totalitems(activity.getObject().getReplies().getTotalItems()
						.intValue());
				googlePlusStatus.setObject_plusoners_totalitems(activity.getObject().getPlusoners().getTotalItems()
						.intValue());
				googlePlusStatus.setObject_resharers_totalitems(activity.getObject().getResharers().getTotalItems()
						.intValue());
				googlePlusStatus.setObject_attachments_content(activity.getObject().getAttachments().get(0)
						.getContent());
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
				googlePlusStatuses.add(statusInfo);
			}

			String nextPageToken = feed.getNextPageToken();
			if (nextPageToken == null) {
				break;
			}
			listActivities.setPageToken(nextPageToken);
			feed = listActivities.execute();
		}
		return googlePlusStatuses;
	}

	public Plus getPlus() {
		return plus;
	}

	public void setPlus(Plus plus) {
		this.plus = plus;
	}

}
