package zx.soft.snd.web.domain;

public class Token {

	private String tokenkey;
	private String tokensecret;

	public String getTokenkey() {
		return tokenkey;
	}

	public void setTokenkey(String tokenkey) {
		this.tokenkey = tokenkey;
	}

	public String getTokensecret() {
		return tokensecret;
	}

	public void setTokensecret(String tokensecret) {
		this.tokensecret = tokensecret;
	}

	@Override
	public String toString() {
		return "Token [tokenkey=" + tokenkey + ", tokensecret=" + tokensecret + "]";
	}

	public Token(String tokenkey, String tokensecret) {
		this.tokenkey = tokenkey;
		this.tokensecret = tokensecret;
	}

}
