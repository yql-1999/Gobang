package cn.edu.yql.gobang;

import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Time;

/**
 * �û���
 * 
 * �û��������������¼�û���Ϣ���࣬���а����û������ơ�IP��ַ�ʹ���ʱ�䡣
 * �û����ƾ����û��Լ�����سƣ��������û��б���չʾ;
 *  IP��ַ������λ�Է�:
 *  ����ʱ�������ж��û�ʹ��������ɫ�����ӣ�
 *  ���������õĹ�����:����ʱ������û�ʹ�ú��ӣ��������塣
 */
public class UserBean implements Serializable {
	protected String name = "�ο�";// �û�����
	protected InetAddress host;// �û�IP��ַ
	private Time time;// �û�����ʱ��

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
