package com.msh.restdemo.exception;

public class FailureAfterPersistException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2094373798085272456L;
	private final Long identifier;

	public FailureAfterPersistException(Long identifier) {
		this.identifier = identifier;
	}

	public Long getIdentifier() {
		return identifier;
	}
}
