package zx.soft.gbxm.google.api;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.gbxm.google.domain.GooglePlusStatus;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.Activities.List;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.Activity.PlusObject.Attachments;
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

				if (activity.getTitle() != null) {
					googlePlusStatus.setTitle(activity.getTitle());
				}
				googlePlusStatus.setPublished(activity.getPublished().toStringRfc3339());
				if (activity.getUpdated() != null) {
					googlePlusStatus.setUpdated(activity.getUpdated().toStringRfc3339());
				}
				googlePlusStatus.setUrl(activity.getUrl());
				googlePlusStatus.setActor_id(activity.getActor().getId());
				googlePlusStatus.setActor_display_name(activity.getActor().getDisplayName());
				if (activity.getObject() != null) {
					googlePlusStatus.setObject_id(activity.getObject().getId());
					googlePlusStatus.setObject_url(activity.getObject().getUrl());
				}
				if (activity.getObject().getActor() != null) {
					googlePlusStatus.setObject_actor_id(activity.getObject().getActor().getId());
					googlePlusStatus.setObject_actor_display_name(activity.getObject().getActor().getDisplayName());
				}
				if (activity.getObject().getOriginalContent() != null) {
					googlePlusStatus.setObject_original_content(activity.getObject().getOriginalContent());
				}
				if (activity.getObject().getReplies() != null) {
					googlePlusStatus.setObject_replies_totalitems(activity.getObject().getReplies().getTotalItems()
							.intValue());
				}
				if (activity.getObject().getPlusoners() != null) {
					googlePlusStatus.setObject_plusoners_totalitems(activity.getObject().getPlusoners().getTotalItems()
							.intValue());
				}
				if (activity.getObject().getResharers() != null) {
					googlePlusStatus.setObject_resharers_totalitems(activity.getObject().getResharers().getTotalItems()
							.intValue());
				}

				if (activity.getObject().getAttachments() != null) {
					String content = "";
					java.util.List<Attachments> attachments = activity.getObject().getAttachments();
					for (Attachments attachment : attachments) {
						content = content + "  " + attachment.getContent();
					}
					googlePlusStatus.setObject_attachments_content(content);
				}
				if (activity.getAnnotation() != null) {
					googlePlusStatus.setAnnotation(activity.getAnnotation());
				}
				if (activity.getGeocode() != null) {
					googlePlusStatus.setLatitude(Double.parseDouble(activity.getGeocode().split(" ")[0]));
					googlePlusStatus.setLongitude(Double.parseDouble(activity.getGeocode().split(" ")[1]));
				}
				if (activity.getPlaceName() != null) {
					googlePlusStatus.setPlace_name(activity.getPlaceName());
				}
				googlePlusStatuses.add(googlePlusStatus);
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
