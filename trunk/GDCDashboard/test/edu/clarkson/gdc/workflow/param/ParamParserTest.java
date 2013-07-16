package edu.clarkson.gdc.workflow.param;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;

public class ParamParserTest {

	@Test
	public void testParamList() throws Exception {
		List<Param> pl = new ParamParser(new StringReader("context:abc"))
				.paramList();
		assertEquals(1, pl.size());
		assertEquals(ContextParam.class, pl.get(0).getClass());
		assertEquals("abc", ((ContextParam) pl.get(0)).key);
	}

}
