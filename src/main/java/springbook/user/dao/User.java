package springbook.user.dao;

public class User {

	private String id;
	private String name;
	private String password;

	private Level level;
	private int login;
	private int recommend;

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public User() {
		this.id = this.name = this.password = null;
	}

	public User(String id, String name, String password, Level level, int login, int recommend) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.level = level;
		this.setLogin(login);
		this.setRecommend(recommend);
	}

	public User(User user) {
		this.id = user.id;
		this.name = user.name;
		this.password = user.password;
		this.level = user.level;
		this.login = user.login;
		this.recommend = user.recommend;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLogin() {
		return login;
	}

	public void setLogin(int login) {
		this.login = login;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public void upgradeLevel() {
		Level nextLevel = this.level.nextLevel();
		if (nextLevel == null) {
			throw new IllegalStateException(this.level + " can not be upgraded.");
		} else {
			this.level = nextLevel;
		}
	}

}
