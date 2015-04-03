package zx.soft.gbxm.facebook.common;

import org.springframework.social.facebook.api.Post;

import zx.soft.gbxm.facebook.domain.FacebookStatus;
import zx.soft.gbxm.facebook.domain.RecordInfo;
import zx.soft.utils.checksum.CheckSumUtils;

public class Convert {

	public static FacebookStatus convertPost2FacebookStatus(Post post) {

		FacebookStatus facebookStatus = new FacebookStatus();
		facebookStatus.setId(post.getId());
		facebookStatus.setFrom_id(post.getFrom().getId());
		facebookStatus.setFrom_name(post.getFrom().getName());
		facebookStatus.setMessage(post.getMessage());
		facebookStatus.setCreatedTime(post.getUpdatedTime());
		facebookStatus.setUpdatedTime(post.getUpdatedTime());
		facebookStatus.setSharesCount(post.getSharesCount());
		facebookStatus.setCommentsCount(post.getCommentCount());
		facebookStatus.setLikesCount(post.getLikeCount());
		return facebookStatus;
	}

	public static RecordInfo convertFacebookStatus2RecordInfo(FacebookStatus facebookStatus, long until) {

		RecordInfo record = new RecordInfo();
		String url = "www.facebook.com/" + facebookStatus.getId().split("_")[0] + "/posts/"
				+ facebookStatus.getId().split("_")[1];
		record.setId(CheckSumUtils.getMD5(url).toUpperCase());
		record.setMid(facebookStatus.getId());
		record.setUsername(facebookStatus.getFrom_id());
		record.setNickname(facebookStatus.getFrom_name());
		record.setContent(facebookStatus.getMessage());
		record.setUrl(url);
		record.setTimestamp(facebookStatus.getCreatedTime().getTime());
		record.setUpdate_time(facebookStatus.getUpdatedTime().getTime());
		record.setComment_count(facebookStatus.getCommentsCount());
		record.setRepost_count(facebookStatus.getSharesCount());
		record.setFavorite_count(facebookStatus.getLikesCount());
		record.setLasttime(until);
		return record;
	}
}
