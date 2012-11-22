package edu.clarkson.gdc.simulator.framework.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.Validate;
import org.slf4j.LoggerFactory;

public class FileCursor<T> {

	public static interface LineProcessor<T> {
		public T process(String line);
	}

	public static class StringProcessor implements LineProcessor<String> {
		public String process(String line) {
			return line;
		}
	}

	private String fileName;

	private int bufferSize;

	private LineProcessor<T> lineProcessor = null;

	public FileCursor(String fileName) {
		this(fileName, 500);
	}

	public FileCursor(String fileName, int bufferSize) {
		super();
		this.fileName = fileName;
		this.bufferSize = bufferSize;
		this.buffer = new LinkedBlockingQueue<T>();
	}

	public LineProcessor<T> getLineProcessor() {
		return lineProcessor;
	}

	public void setLineProcessor(LineProcessor<T> lineProcessor) {
		Validate.notNull(lineProcessor);
		this.lineProcessor = lineProcessor;
	}

	private Queue<T> buffer;

	public T next() {
		if (buffer.isEmpty()) {
			load();
		}
		return buffer.poll();
	}

	private BufferedReader br = null;

	private boolean init = false;

	protected void load() {
		try {
			if (null == br) {
				if (!init) {
					br = new BufferedReader(new InputStreamReader(Thread
							.currentThread().getContextClassLoader()
							.getResourceAsStream(fileName)));
					init = true;
				} else {
					return;
				}
			}
			int i = 0;
			String line = null;
			try {
				for (i = 0; i < bufferSize; i++) {
					if ((line = br.readLine()) == null) {
						br.close();
						br = null;
						break;
					}
					T output = getLineProcessor().process(line);
					if (output != null)
						buffer.offer(output);
				}
			} catch (Exception e) {
				LoggerFactory
						.getLogger(getClass())
						.error(MessageFormat.format(
								"Error while invoking processor on content:{1}, return null",
								line), e);
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error(
					"Error while loading file", e);
		}
	}
}
