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

		pl = new ParamParser(
				new StringReader("context:abc,input:5,return:good"))
				.paramList();
		assertEquals(3, pl.size());
		assertEquals(ContextParam.class, pl.get(0).getClass());
		assertEquals("abc", ((ContextParam) pl.get(0)).key);

		assertEquals(InputParam.class, pl.get(1).getClass());
		assertEquals(5, ((InputParam) pl.get(1)).index);

		assertEquals(ReturnParam.class, pl.get(2).getClass());
		assertEquals("good", ((ReturnParam) pl.get(2)).key);
	}

	@Test
	public void testOrParam() throws Exception {
		List<Param> pl = new ParamParser(
				new StringReader("context:abc|input:5")).paramList();
		assertEquals(1, pl.size());
		assertEquals(OrParam.class, pl.get(0).getClass());
		OrParam op = (OrParam) pl.get(0);
		assertEquals(ContextParam.class, op.getFirst().getClass());
		assertEquals(InputParam.class, op.getSecond().getClass());
		assertEquals("abc", ((ContextParam) op.getFirst()).key);
		assertEquals(5, ((InputParam) op.getSecond()).index);
	}

}
