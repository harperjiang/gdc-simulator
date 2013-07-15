package edu.clarkson.gdc.simulator.module.network.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AdjMatrixTest {

	@Test
	public void testMakeMatrix() {
		AdjMatrix matrix = new AdjMatrix();
		matrix.addVertex("A", 1);
		matrix.addVertex("B", 1);
		matrix.addVertex("C", 1);
		matrix.addVertex("D", 1);
		matrix.addVertex("E", 1);
		matrix.addEdge("A", "B", 1);
		matrix.addEdge("B", "C", 1);
		matrix.addEdge("C", "E", 1);
		matrix.addEdge("A", "D", 1);
		matrix.addEdge("D", "E", 1);

		matrix.makeMatrix();

		assertEquals(5, matrix.matrix.length);
		for (int i = 0; i < 5; i++) {
			assertEquals(1, matrix.matrix[i][i]);
		}
		assertEquals(1, matrix.matrix[0][1]);
		assertEquals(1, matrix.matrix[1][2]);
		assertEquals(1, matrix.matrix[2][4]);
		assertEquals(1, matrix.matrix[0][3]);
		assertEquals(1, matrix.matrix[3][4]);
		assertEquals(1, matrix.matrix[1][0]);
		assertEquals(1, matrix.matrix[2][1]);
		assertEquals(1, matrix.matrix[4][2]);
		assertEquals(1, matrix.matrix[3][0]);
		assertEquals(1, matrix.matrix[4][3]);
	}

	@Test
	public void testDijkstra() {
		AdjMatrix matrix = new AdjMatrix();
		matrix.addVertex("A", 1);
		matrix.addVertex("B", 1);
		matrix.addVertex("C", 1);
		matrix.addVertex("D", 1);
		matrix.addVertex("E", 1);
		matrix.addEdge("A", "B", 1);
		matrix.addEdge("B", "C", 1);
		matrix.addEdge("C", "E", 1);
		matrix.addEdge("A", "D", 1);
		matrix.addEdge("D", "E", 1);

		assertEquals(5, matrix.latency("A", "E"));
	}
}
