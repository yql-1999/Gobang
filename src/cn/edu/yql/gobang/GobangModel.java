package cn.edu.yql.gobang;

import java.io.Serializable;
import java.util.Arrays;

/**
 * ����ģ��
 * 
 * ����ģ�������һ��15��15�еĶ�ά������������ӵĳ�����
 * ����ģ�Ͳ��õ���ģʽ����֤�������ݵ�Ψһ�ԡ�
 * ����Ҳ�ṩ �˿�¡���� ����ķ������÷�������ʵ�ֻ������Ϸ�طŹ��ܡ�
 */
public class GobangModel implements Serializable {
	
	static private GobangModel model; // ����������󣬵���ģʽ�ɱ������е�GobangModel����ͬһ������
	static private byte[][] chessmanArray = new byte[15][15]; // ������������
	public final static byte WHITE_CHESSMAN = 1;// �����ֵ
	public final static byte BLACK_CHESSMAN = -1;// �����ֵ

	/**
	 * ��ȡ����ʵ���ķ���
	 * 
	 * @return �������
	 */
	public static GobangModel getInstance() {
		if (model == null) {// ���model��null
			model = new GobangModel();// �򴴽��¶���
		}
		return model;
	}

	/**
	 * ����ģ�͵Ĺ��췽��   private���췽��
	 */
	private GobangModel() {
		model = this;
	}

	/**
	 * ��ȡ���̵���������ķ���
	 * 
	 * @return - �������ӵ�����
	 */
	public byte[][] getChessmanArray() {
		return chessmanArray; // ������������
	}

	/**
	 * ������������ķ���
	 * 
	 * @param chessmanArray
	 *            - һ�������������ӵĶ�ά����
	 */
	public void setChessmanArray(byte[][] chessmanArray) {
		// �����������鷽����ʼִ��
		this.chessmanArray = chessmanArray;// ����������������������Ϊ�������������
	}

	/**
	 * ��ȡ��������������Ŀ���
	 * 
	 * @return - ��������
	 */
	byte[][] getChessmanArrayCopy() {
		byte[][] newArray = new byte[15][15]; // ����һ����ά����
		for (int i = 0; i < newArray.length; i++) {
			// ��������
			newArray[i] = Arrays.copyOf(chessmanArray[i], newArray[i].length);
		}
		return newArray;
	}
}
