package zx.soft.snd.web.domain;

import java.util.List;

public class PostData {

	private List<String> nameList;
	private int num;

	public PostData(List<String> nameList, int num) {
		super();
		this.nameList = nameList;
		this.num = num;
	}

	@Override
	public String toString() {
		return "PostData [nameList=" + nameList + ", num=" + num + "]";
	}

	public List<String> getNameList() {
		return nameList;
	}

	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
