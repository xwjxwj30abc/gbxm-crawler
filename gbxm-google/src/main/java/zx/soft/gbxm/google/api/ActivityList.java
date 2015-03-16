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

	public ActivityList(Plus plus) {
		this.plus = plus;
	}

	public ArrayList<StatusInfo> getActivitiesByUserId(String userId, long lastUpdatedTime) throws IOException {
		ArrayList<StatusInfo> statusInfos = new ArrayList<>();
		List listActivities = plus.activities().list(userId, "public");
		listActivities.setMaxResults(2L);//最大可设置为100
		listActivities.setFields("nextPageToken,items(published,id,url,object/content)");
		ActivityFeed feed = listActivities.execute();
		int currentPageNumber = 0;

		while (feed.getItems() != null && !feed.getItems().isEmpty() && ++currentPageNumber <= 2) {
			if (feed.getItems().get(0).getPublished().getValue() <= lastUpdatedTime) {
				break;
			}
			for (Activity activity : feed.getItems()) {
				logger.info(activity.getId());
				StatusInfo statusInfo = new StatusInfo();
				statusInfo.setStatusId(activity.getId());
				statusInfo.setPublished(new Timestamp(activity.getPublished().getValue()));
				statusInfo.setContent(activity.getObject().getContent());
				statusInfo.setUrl(activity.getUrl());
				statusInfo.setSource(userId);
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
