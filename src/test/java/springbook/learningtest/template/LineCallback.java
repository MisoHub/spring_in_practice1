package springbook.learningtest.template;

public interface LineCallback<T> {

	T doSomethinWithLine(String line, T value);

}
