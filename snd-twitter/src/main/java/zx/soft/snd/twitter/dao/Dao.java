package zx.soft.snd.twitter.dao;

import zx.soft.snd.twitter.domain.StatusInfo;
import zx.soft.snd.twitter.domain.UserInfo;

public interface Dao {

	public void insertUserInfo(UserInfo userInfo);

	public void insertStatusInfo(StatusInfo statusInfo);

	public void updateUserInfo(UserInfo userInfo);

	public void updateStatusInfo(StatusInfo statusInfo);
}
