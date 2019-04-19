package top.starrysea.object.dto;

import java.io.Serializable;

import top.starrysea.kql.entity.Entity;
import top.starrysea.kql.entity.IBuilder;
import top.starrysea.object.view.out.UserInfo;

public class User implements Entity, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6755911268936269135L;
	private String userId;
	private String userEmail;
	private String userPassword;
	private String username;
	private short osuPerson;
	private short osuTeam;
	private short osuGrade;
	private short isDD;

	public User() {

	}

	private User(Builder builder) {
		this.userId = builder.userId;
		this.userEmail = builder.userEmail;
		this.userPassword = builder.userPassword;
		this.username = builder.username;
		this.osuPerson = builder.osuPerson;
		this.osuTeam = builder.osuTeam;
		this.osuGrade = builder.osuGrade;
		this.isDD = builder.isDD;
	}

	public static class Builder implements IBuilder<User> {
		private String userId;
		private String userEmail;
		private String userPassword;
		private String username;
		private short osuPerson;
		private short osuTeam;
		private short osuGrade;
		private short isDD;

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder userEmail(String userEmail) {
			this.userEmail = userEmail;
			return this;
		}

		public Builder userPassword(String userPassword) {
			this.userPassword = userPassword;
			return this;
		}

		public Builder username(String username) {
			this.username = username;
			return this;
		}

		public Builder osuPerson(short osuPerson) {
			this.osuPerson = osuPerson;
			return this;
		}

		public Builder osuTeam(short osuTeam) {
			this.osuTeam = osuTeam;
			return this;
		}

		public Builder osuGrade(short osuGrade) {
			this.osuGrade = osuGrade;
			return this;
		}

		public Builder isDD(short isDD) {
			this.isDD = isDD;
			return this;
		}

		@Override
		public User build() {
			return new User(this);
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public short getOsuPerson() {
		return osuPerson;
	}

	public void setOsuPerson(short osuPerson) {
		this.osuPerson = osuPerson;
	}

	public short getOsuTeam() {
		return osuTeam;
	}

	public void setOsuTeam(short osuTeam) {
		this.osuTeam = osuTeam;
	}

	public short getOsuGrade() {
		return osuGrade;
	}

	public void setOsuGrade(short osuGrade) {
		this.osuGrade = osuGrade;
	}

	public short getIsDD() {
		return isDD;
	}

	public void setIsDD(short isDD) {
		this.isDD = isDD;
	}

	public UserInfo toVO() {
		return new UserInfo(this);
	}
}
