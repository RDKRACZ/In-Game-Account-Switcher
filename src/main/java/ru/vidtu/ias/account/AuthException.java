package ru.vidtu.ias.account;

import net.minecraft.network.chat.Component;

public class AuthException extends Exception {
	private static final long serialVersionUID = 1L;
	private Component text;
	
	public AuthException(Component text) {
		super(text.getString());
		this.text = text;
	}
	
	public AuthException(Component text, String detailed) {
		super(text.getString() + ":" + detailed);
		this.text = text;
	}
	
	public Component getText() {
		return text;
	}
}
