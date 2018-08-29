package br.com.ericsson.smartnotification.web.domain;

public class ButtonView {
	private String text;

	private String action;

	private String modal;

	public ButtonView(String text, String action, String modal) {
		super();
		this.text = text;
		this.action = action;
		this.modal = modal;
	}

	public String getText() {
		return text;
	}

	public String getAction() {
		return action;
	}

	public String getModal() {
		return modal;
	}

}
