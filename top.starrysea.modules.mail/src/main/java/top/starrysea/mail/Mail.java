package top.starrysea.mail;

import java.util.List;

public class Mail {

	private List<String> to;
	private String title;
	private String content;

	public Mail(List<String> to, String title, String content) {
		this.to = to;
		this.title = title;
		this.content = content;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
