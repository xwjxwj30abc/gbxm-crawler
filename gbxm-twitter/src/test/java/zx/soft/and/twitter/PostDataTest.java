package zx.soft.and.twitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import zx.soft.gbxm.twitter.domain.PostData;
import zx.soft.gbxm.twitter.domain.RecordInfo;
import zx.soft.utils.http.HttpClientDaoImpl;
import zx.soft.utils.json.JsonUtils;

public class PostDataTest {

	@Test
	public void testPostData() throws IOException {

		String url = "http://192.168.31.12:8900/sentiment/index";
		HttpClientDaoImpl client = new HttpClientDaoImpl();

		ClientResource clientResource = new ClientResource(url);
		RecordInfo recordInfo = new RecordInfo();
		recordInfo.setId("sentiment");
		recordInfo.setPlatform(11);
		recordInfo.setMid("123456789987654321");
		recordInfo.setUsername("zxsoft");
		recordInfo.setNickname("中新舆情");
		recordInfo.setOriginal_id("original_sentiment");
		recordInfo.setOriginal_uid("original_zxsoft");
		recordInfo.setOriginal_name("original_中新软件");
		recordInfo.setOriginal_title("original_标题");
		recordInfo.setOriginal_url("http://www.orignal_url.com");
		recordInfo.setUrl("http://www.url.com");
		recordInfo.setHome_url("http://www.home_url.com");
		recordInfo.setTitle("标题");
		recordInfo.setType("所属类型");
		recordInfo.setIsharmful(Boolean.TRUE);
		recordInfo.setContent("测试内容");
		recordInfo.setComment_count(10);
		recordInfo.setRead_count(20);
		recordInfo.setFavorite_count(30);
		recordInfo.setAttitude_count(40);
		recordInfo.setRepost_count(50);
		recordInfo.setVideo_url("http://www.video_url.com");
		recordInfo.setPic_url("htpp://www.pic_url.com");
		recordInfo.setVoice_url("http://www.voice_url.com");
		recordInfo.setTimestamp(1419755627695L);
		recordInfo.setSource_id(70);
		recordInfo.setLasttime(1419755627695L + 86400_000L);
		recordInfo.setServer_id(90);
		recordInfo.setIdentify_id(100);
		recordInfo.setIdentify_md5("abcdefg123456789");
		recordInfo.setKeyword("关键词");
		recordInfo.setFirst_time(1419755627695L + 86400_000L * 2);
		recordInfo.setUpdate_time(1419755627695L + 86400_000L * 3);
		recordInfo.setIp("192.168.32.45");
		recordInfo.setLocation("安徽省合肥市");
		recordInfo.setGeo("经纬度信息");
		recordInfo.setReceive_addr("receive@gmail.com");
		recordInfo.setAppend_addr("append@gmail.com");
		recordInfo.setSend_addr("send@gmail.com");
		recordInfo.setSource_name("新浪微博");
		recordInfo.setSource_type(121);
		recordInfo.setCountry_code(122);
		recordInfo.setLocation_code(123);
		recordInfo.setProvince_code(124);
		recordInfo.setCity_code(125);
		List<RecordInfo> records = new ArrayList<>();
		records.add(recordInfo);
		PostData data = new PostData();
		data.setNum(1);
		data.setRecords(records);
		Representation entity = new StringRepresentation(JsonUtils.toJsonWithoutPretty(data));
		entity.setMediaType(MediaType.APPLICATION_JSON);
		Representation representation = clientResource.post(entity);
		Response response = clientResource.getResponse();
		System.out.println(response.getEntity().getText());
		//client.doPost(url, JsonUtils.toJsonWithoutPretty(data));
	}
}
