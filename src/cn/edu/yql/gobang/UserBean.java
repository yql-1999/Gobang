package cn.edu.yql.gobang;

import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Time;

/**
 * 用户类
 * 
 * 用户类是用来保存登录用户信息的类，类中包含用户的名称、IP地址和创建时间。
 * 用户名称就是用户自己起的呢称，可以在用户列表中展示;
 *  IP地址用来定位对方:
 *  创建时间用来判断用户使用哪种颜色的棋子，
 *  本程序设置的规则是:创建时间早的用户使用黑子，优先下棋。
 */
public class UserBean implements Serializable {
	protected String name = "游客";// 用户名称
	protected InetAddress host;// 用户IP地址
	private Time time;// 用户创建时间

	public InetAddress getHost() {
		return host;
	}

	public void setHost(InetAddress host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Time getTime() {
		return time;
	}

	public String toString() {
		return getName();
	}
}
