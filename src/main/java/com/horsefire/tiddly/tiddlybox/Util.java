package com.horsefire.tiddly.tiddlybox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

	private static final Logger LOG = LoggerFactory.getLogger(Util.class);

	public static String extractString(InputStream in2) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(in2));

		StringBuilder wikiContents = new StringBuilder();
		String line = in.readLine();
		while (line != null) {
			wikiContents.append(line).append('\n');
			line = in.readLine();
		}
		return wikiContents.toString();
	}
}
