package zx.soft.snd.web.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import zx.soft.snd.web.domain.Token;

public class Store2DBTest {

	@Test
	public void testinsertNames() {
		DaoImpl store2DB = new DaoImpl();
		List<String> names = new ArrayList<>();

		names.add("scavin");
		names.add("jimbo2527");
		names.add("wgybzb");
		names.add("xwj_xwj");
		store2DB.insertNames(names);
	}

	@Test
	public void testinsertToken() {
		DaoImpl store2DB = new DaoImpl();
		List<Token> tokens = new ArrayList<>();
		//hjb token : 2879495431-gxnoteJes2wH3UbfGImDrGS9C2xfFAQEeRxgk6E
		//access-secret : 6kwmsLvARCGGkvYFqY7noGbeo6gv2CruUoNQYt0ymjQ99

		//	xwj	token : 2922413937-khq0Hh3Otv6AItaFNCtnWRVWWRjVnT4BoBjIxoI
		//tokenSecret : q4nWHrNsAMcwIjwiuQbJnpKpBo11fFk0lTg3zS1mQWFpe
		Token token1 = new Token("2922413937-khq0Hh3Otv6AItaFNCtnWRVWWRjVnT4BoBjIxoI",
				"q4nWHrNsAMcwIjwiuQbJnpKpBo11fFk0lTg3zS1mQWFpe");
		Token token2 = new Token("2879495431-gxnoteJes2wH3UbfGImDrGS9C2xfFAQEeRxgk6E",
				"6kwmsLvARCGGkvYFqY7noGbeo6gv2CruUoNQYt0ymjQ99");
		tokens.add(token1);
		tokens.add(token2);
		store2DB.insertToken(tokens);
	}
}
