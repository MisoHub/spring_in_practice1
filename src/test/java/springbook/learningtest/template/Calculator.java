package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

	public Integer calcSum(String filepath) throws IOException {
		BufferedReaderCallback sumCallback = new BufferedReaderCallback() {

			@Override
			public Integer doSomethingWithReader(BufferedReader br) throws IOException {
				// TODO Auto-generated method stub
				int ret = lineReadTemplate(filepath, new LineCallback() {
					@Override
					public Integer doSomethinWithLine(String line, Integer value) {
						// TODO Auto-generated method stub
						return value + Integer.valueOf(line);
					}

				}, 0);

				return ret;
			}
		};
		return fileReadTemplate(filepath, sumCallback);
	}

	public Integer calcMul(String filepath) throws IOException {
		BufferedReaderCallback mulCallback = new BufferedReaderCallback() {

			@Override
			public Integer doSomethingWithReader(BufferedReader br) throws IOException {
				// TODO Auto-generated method stub
				
				return lineReadTemplate(filepath, new LineCallback(){

					@Override
					public Integer doSomethinWithLine(String line, Integer value) {
						// TODO Auto-generated method stub
						return value*Integer.valueOf(line);
					}

				},1);
			}
		};

		return fileReadTemplate(filepath, mulCallback);
	}

	public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(filepath));
			int ret = callback.doSomethingWithReader(br);

			return ret;
		} catch (IOException e) {

			System.out.println(e.getMessage());
			throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	public Integer lineReadTemplate(String filepath, LineCallback callback, Integer initVal) throws IOException {
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(filepath));
		Integer res = initVal;
		String line = null;

		while ((line = br.readLine()) != null) {
			res = callback.doSomethinWithLine(line, res);
		}

		return res;

	}

}
