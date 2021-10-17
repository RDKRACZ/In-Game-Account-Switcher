package ru.vidtu.ias.account;

import net.minecraft.util.text.ITextComponent;

public class AuthException extends Exception {
	private static final long serialVersionUID = 1L;
	private ITextComponent text;
	
	public AuthException(ITextComponent text) {
		super(text.getString());
		this.text = text;
	}
	
	public AuthException(ITextComponent text, String detailed) {
		super(text.getString() + ":" + detailed);
		this.text = text;
	}
	
	public ITextComponent getText() {
		return text;
	}
}
