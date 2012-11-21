package edu.clarkson.gdc.simulator.framework.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
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

	private long position = 0;

	private long lineCounter = 0;

	private LineProcessor<T> lineProcessor = null;

	public FileCursor(String fileName) {
		this(fileName, 5000);
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

	protected void load() {
		try {
			URL fileUrl = Thread.currentThread().getContextClassLoader()
					.getResource(fileName);
			FileInputStream fis = new FileInputStream(new File(new URI(
					fileUrl.getProtocol(), fileUrl.getHost(),
					fileUrl.getPath(), null)));
			fis.getChannel().position(position);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			int i = 0;
			try {
				for (i = 0; i < bufferSize; i++) {
					String line = null;
					if ((line = br.readLine()) == null)
						break;
					T output = getLineProcessor().process(line);
					if (output != null)
						buffer.offer(output);
				}
			} catch (Exception e) {
				LoggerFactory
						.getLogger(getClass())
						.error(MessageFormat.format(
								"Error while invoking processor on line:{0}, return null",
								lineCounter + i), e);
			}
			this.position = fis.getChannel().position();
			this.lineCounter += buffer.size();
			br.close();
			fis.close();
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error(
					"Error while loading file", e);
		}
	}
}
