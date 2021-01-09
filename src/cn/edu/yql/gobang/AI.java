package cn.edu.yql.gobang;

import java.util.Arrays;

/**
 * �������ܶ�ս�㷨
 * 
 * @author shenxiaoqi
 *
 */
public class AI {
	MainFrame frame;// ���������
	private GobangModel gobangModel1;// ����ģ����
	private GobangPanel gobangPanel1;// ���������
	private final int boundary = 8;// ���̱߽�ֵ���������ڲ�׽����ʱ���߽�

	public AI(MainFrame outer) {
		frame = outer;// �ⲿ�������
		gobangModel1 = GobangModel.getInstance();// ��ȡ����ģ��
		gobangPanel1 = frame.getChessPanel1().getGobangPanel1();// ��õ�ǰʹ�õ��������
	}

	/**
	 * AI�����������ķ���
	 * 
	 * �÷���ר�����������������Է�����ָ�
	 * ���ڱ�����ȡ�����˻���ս�еġ����䡱�����塱�����塱���
	 * ����ֻ������ҵġ���ʼ������ɡ�
	 * 
	 * @param messageObj
	 *            - �������
	 */
	public void oprationHandler(Object messageObj) {
		int code = (Integer) messageObj; // ��ȡ�������
		switch (code) {// �ж�����
		case ChessPanel.OPRATION_START_MACHINE: // ������������ʼ��Ϸ
			frame.getChessPanel1().setTowardsStart(true); // ����AI����Ϸ��ʼ״̬Ϊtrue
			break;
		default:
			// System.out.println("�������룺" + code);
		}
	}

	/**
	 * ��������
	 * 
	 * ������жϳ�����λ��֮������GobangPanel (�������)���е�chessForMachine()������
	 * ����������굱���������뷽���У�
	 * ����ͻ��ڶ�Ӧ����λ�û���һö���壬��������Ȩ�޽�����ҡ�
	 */
	public void chess() {
		int chessIndex[] = forEach();// ��ȡAI�жϵ�����λ��
		gobangPanel1.chessForMachine(chessIndex[0], chessIndex[1]);// �����ӷ�������ָ��λ��
	}

	/**
	 * ������������λ�ã�Ѱ������㡣 �鿴ÿһ�����������γɵ����ͣ��ҳ�������в���ģ��������Ϳ�������ƽ�λ���ҳ�����㡣
	 * 
	 * @return �������������
	 */
	private int[] forEach() {
		int x = -1, y = -1;// ��Ҫ�µ���������
		int threat = 0;// �����ϳ��ֵ������вֵ
		byte[][] chessmanArray = gobangModel1.getChessmanArray();// �������
		for (int i = 0; i < 15; i++) {// ����������
			for (int j = 0; j < 15; j++) {// ����������
				if (chessmanArray[i][j] > 0) {// ����˴��а�����
					int tmp[] = catchChessModle(i, j, chessmanArray);// ��׽ÿ�������γɵ�����
					if (tmp[0] > threat) {// ������ڱȵ�ǰ�����вֵ��Ҫ�����вֵ�����¼�˴���������
						threat = tmp[0];// ���������вֵ
						x = tmp[1];// �������Ӻ�����
						y = tmp[2];// ��������������
					}
				}
			}
		}
		return new int[] { x, y };// ���غ���������ɵ�һά����
	}

	/**
	 * ��׽������ĳһ���������ܲ����������вֵ���ƽ��·���
	 * ��һö����Ϊԭ�㣬��ȡ���ĸ���������������е����ӣ��ֱ��ж����������Ͽ��ܲ���������в�����ͣ�
	 * �������Ϳ�������ƽ�λ�ã�����������ϵ�λ�ã������سɽ�����顣
	 * 
	 * @param x
	 *            -����׽�����Ӻ�����
	 * @param y
	 *            -����׽������������
	 * @param chessmanArray
	 *            - ��������
	 * @return ����һά���飬��ֵ�ֱ����{�����вֵ,�ƽ����ӵĺ�����,�ƽ����ӵ�������}
	 */
	private int[] catchChessModle(int x, int y, byte[][] chessmanArray) {

		// ����0����¼��λ�õ����ӿɲ����������вֵ������ֵ
		// ����1����Ӧ���������
		// ����2����Ӧ���������
		int position[] = new int[3];

		// �����Ա���׽����Ϊ���ĵ��ĸ������γɵ�����
		// �Բ�����Ϊ���ĵ㣬�����ĸ���������ͣ�����ֱ�Ϊ �� | \ /
		int model[][] = new int[4][11];
		for (int tmp[] : model) {// ��������
			Arrays.fill(tmp, boundary);// ���������Ϊ�߽糣��
		}
		// �Ѳ��������ÿ�е����Ĳ�λ
		model[0][5] = model[1][5] = model[2][5] = model[3][5] = chessmanArray[x][y];
		// �Ը�����Ϊ���ģ���������5��
		for (int i = 1; i <= 5; i++) {
			// ˮƽ��������
			if (x - i >= 0) {// ���û���߳��߽�
				model[0][5 - i] = chessmanArray[x - i][y];// ��������Ӽ�¼��ˮƽ���͵���
			}
			if (x + i <= 14) {// ���û���߳��߽�
				model[0][5 + i] = chessmanArray[x + i][y];// ���Ҳ����Ӽ�¼��ˮƽ���͵���
			}

			// ��ֱ��������
			if (y - i >= 0) {// ���û���߳��߽�
				model[1][5 - i] = chessmanArray[x][y - i];// ���Ϸ����Ӽ�¼����ֱ���͵���
			}
			if (y + i <= 14) {// ���û���߳��߽�
				model[1][5 + i] = chessmanArray[x][y + i];// ���·����Ӽ�¼����ֱ���͵���
			}

			// ��б�ܷ�������
			if (x - i >= 0 && y + i <= 14) {// ���û���߳��߽�
				model[2][5 - i] = chessmanArray[x - i][y + i];// �����·����Ӽ�¼����б���͵���
			}
			if (x + i <= 14 && y - i >= 0) {// ���û���߳��߽�
				model[2][5 + i] = chessmanArray[x + i][y - i];// �����Ϸ����Ӽ�¼����б���͵���
			}

			// ��б�ܷ�������
			if (x - i >= 0 && y - i >= 0) {// ���û���߳��߽�
				model[3][5 - i] = chessmanArray[x - i][y - i];// �����Ϸ����Ӽ�¼����б���͵���
			}
			if (x + i <= 14 && y + i <= 14) {// ���û���߳��߽�
				model[3][5 + i] = chessmanArray[x + i][y + i];// �����·����Ӽ�¼����б���͵���
			}
		}
		int score = 0;// ��¼������֣���вֵ��
		int direction = -1;// ��¼������ֵķ���model����һά�±꣩
		int index = 0;// ��¼����ƫ����(judgeModle()����������ƽ�λ��)
		for (int i = 0; i < model.length; i++) {// ������������
			int getResult[] = judgeModle(model[i]);// ��Դ˷������ͣ������ƽⷽ��
			if (score < getResult[1]) {// ������ֱȵ�ǰ�����вֵ��Ҫ�����в
				score = getResult[1];// �������֣���вֵ��
				// ����׽��������ģ���е�����Ϊ5��getResult[0]Ϊ�ƽⷽ���е���������λ��
				// getResult[0] - 5 = �ƽ�λ�þ��뱻��׽�����ӵ�����λ��
				index = getResult[0] - 5;//
				direction = i;// ��¼�����͵ķ���
			}
		}
		switch (direction) {// �ж������вֵ���ڵķ���
		case 0:// �����ˮƽ����
			x += index;// �����λ����ԭλ�����ң�������ƫ��index��ֵ
			break;
		case 1:// ����Ǵ�ֱ����
			y += index;// �����λ����ԭλ�����£������ϣ�ƫ��index��ֵ
			break;
		case 2:// ����Ƿ�б����
			x += index;// �����λ����ԭλ�����ң�������ƫ��index��ֵ
			y -= index;// �����λ����ԭλ�����ϣ������£�ƫ��index��ֵ
			break;
		case 3:// �������б����
			x += index;// �����λ����ԭλ�����ң�������ƫ��index��ֵ
			y += index;// �����λ����ԭλ�����£������ϣ�ƫ��index��ֵ
			break;
		}
		position[0] = score;// ��¼�����ӵ�������֣���вֵ��
		position[1] = x;// ��¼��Ӧ���������
		position[2] = y;// ��¼��Ӧ����������

		return position;// ���ؽ������
	}

	/**
	 * �ж�ĳһ�����͵���вֵ����Ӧ����λ�á�
	 * ���Ƚ���һ������ת��Ϊ�ַ�����Ȼ������ַ������Ƿ���������Ϳ��е�����в�����͡��Ա�������в���ͣ��ҳ�������вֵ����
	 * ����¼�����͵���вֵ������㣬���س����顣
	 * 
	 * @param model
	 *            -һ����������
	 * @return
	 */
	public int[] judgeModle(int model[]) {
		int piont[] = new int[2];// ��ʼ�����ؽ������
		int score = 0;// ��¼�������
		StringBuffer sb = new StringBuffer();// ׼�����������鱣��Ϊ�ַ�����StringBuffer
		for (int num : model) {// �������飬���������ַ���
			if (num == GobangModel.BLACK_CHESSMAN) {// ����Ǻ���
				num = 4;// ��Ϊ�������֣����⸺�Ż�ռ�ַ�
			}
			sb.append(num);// �ַ�����Ӵ�����
		}
		Object library[][] = getModelLibrary();// ��ȡ���Ϳ����������ͼ���������
		for (int i = 0; i < library.length; i++) {// �������Ϳ�
			String chessModel = (String) library[i][0];// ��ȡ��������
			int modelIndex = -1;// ��ʱ���������ڱ���ĳ�������ַ����г��ֵ�����λ��
			if ((modelIndex = sb.indexOf(chessModel)) != -1) {// ������ڴ����ͣ������ͳ��ֵ�λ�ø���modelIndex
				int scoreInLib = (int) library[i][1];// ��ȡ��������
				int stepIndex = (int) library[i][2];// ��ȡ��Ӧ������λ��
				if (score < scoreInLib) {// ������ָ��ߵ�����
					score = scoreInLib;// ��������
					// ��¼Ӧ��(����һ����)�����ʵ������λ�á�
					// �������ַ����е�����λ�� + ���͸����Ľ��λ�� = �ַ����еĽ��λ��
					piont[0] = modelIndex + stepIndex;
					piont[1] = score;// ��¼������
				}
			}
		}
		return piont;// ���ؽ������
	}

	/**
	 * ��ȡ���Ϳ⡣ ���Ϳ�ÿһ�����Ͷ������������ݣ���һ�����������ͣ�1��ʾ���ӣ�0��ʾ��λ�ã�����"11101"���ڶ��������Ǵ����͵����֣�
	 * ����������������ж�Ҫ�����ƽ���������
	 * ��������ֵ���ƽ�����͵�λ�ã���λ���Ǵ������ַ���������λ�ã�����"11101"���ƽ�λ��Ϊ3��������0��λ������
	 * �����е����Ͷ�ָһ���г��ֵ����ͣ�ֻ���жϽ�Ϊ�򵥵����ͣ������жϸ��ӵĶ��и������͡�
	 * 
	 * @return ��������ƥ��⣬�������͹�ֵ��������
	 */
	private Object[][] getModelLibrary() {
		// ���Ϳ����飬��һ�б��������ַ������ڶ��б������͹�ֵ,�����б�����Դ����͵��·�
		Object[][] library = new Object[28][3];
		// һ�п��ܳ��ֵ����ͣ�1��ʾ���ӣ�0��ʾ��λ��
		String livefour = "011110";// ��������
		String deadfour1a = "01111";// ��������1
		String deadfour1b = "11110";
		String deadfour2a = "11101";// ��������2
		String deadfour2b = "10111";
		String deadfour3 = "11011";// ��������3
		String livethree = "01110";// ��������
		String deadthree1a = "11100";// ��������1
		String deadthree1b = "00111";
		String deadthree2a = "01011";// ��������2
		String deadthree2b = "10110";
		String deadthree2c = "01101";
		String deadthree2d = "11010";
		String deadthree3a = "10011";// ��������3
		String deadthree3b = "11001";
		String deadthree4 = "10101";// ��������4
		String livetwo = "00011000";// �������
		String deadtwo1a = "11000";// ��������1
		String deadtwo1b = "01100";
		String deadtwo1c = "00110";
		String deadtwo1d = "00011";
		String deadtwo2a = "00101";// ��������2
		String deadtwo2b = "10100";
		String deadtwo2c = "01010";
		String deadtwo3a = "01001";// ��������3
		String deadtwo3b = "10010";
		String deadone1 = "00001";// ��һ����
		String deadone2 = "10000";
		library[0][0] = livefour;// ���ͷ���������
		library[0][1] = 100000;// �����Ͳ�������вֵ��������ֵ��
		library[0][2] = 0;// �ڴ�����0���������ӿ��ƽ�
		library[1][0] = deadfour1a;
		library[1][1] = 2500;
		library[1][2] = 0;
		library[2][0] = deadfour1b;
		library[2][1] = 2500;
		library[2][2] = 4;
		library[3][0] = deadfour2a;
		library[3][1] = 3300;
		library[3][2] = 3;
		library[4][0] = deadfour2b;
		library[4][1] = 3300;
		library[4][2] = 1;
		library[5][0] = deadfour3;
		library[5][1] = 2600;
		library[5][2] = 2;
		library[6][0] = livethree;
		library[6][1] = 3000;
		library[6][2] = 0;
		library[7][0] = deadthree1a;
		library[7][1] = 500;
		library[7][2] = 3;
		library[8][0] = deadthree1b;
		library[8][1] = 500;
		library[8][2] = 1;
		library[9][0] = deadthree2a;
		library[9][1] = 800;
		library[9][2] = 2;
		library[10][0] = deadthree2b;
		library[10][1] = 800;
		library[10][2] = 1;
		library[11][0] = deadthree2c;
		library[11][1] = 800;
		library[11][2] = 3;
		library[12][0] = deadthree2d;
		library[12][1] = 800;
		library[12][2] = 2;
		library[13][0] = deadthree3a;
		library[13][1] = 600;
		library[13][2] = 2;
		library[14][0] = deadthree3b;
		library[14][1] = 600;
		library[14][2] = 2;
		library[15][0] = deadthree4;
		library[15][1] = 550;
		library[15][2] = 1;
		library[16][0] = livetwo;
		library[16][1] = 650;
		library[16][2] = 2;
		library[17][0] = deadtwo1a;
		library[17][1] = 150;
		library[17][2] = 2;
		library[18][0] = deadtwo1b;
		library[18][1] = 150;
		library[18][2] = 3;
		library[19][0] = deadtwo1c;
		library[19][1] = 150;
		library[19][2] = 1;
		library[20][0] = deadtwo1d;
		library[20][1] = 150;
		library[20][2] = 2;
		library[21][0] = deadtwo2a;
		library[21][1] = 250;
		library[21][2] = 1;
		library[22][0] = deadtwo2b;
		library[22][1] = 250;
		library[22][2] = 3;
		library[23][0] = deadtwo2c;
		library[23][1] = 250;
		library[23][2] = 2;
		library[24][0] = deadtwo3a;
		library[24][1] = 200;
		library[24][2] = 2;
		library[25][0] = deadtwo3b;
		library[25][1] = 200;
		library[25][2] = 2;
		library[26][0] = deadone1;
		library[26][1] = 100;
		library[26][2] = 3;
		library[27][0] = deadone2;
		library[27][1] = 100;
		library[27][2] = 1;

		/*----- �����Ϳ����鰴�����͹�ֵ����вֵ������ֵ���������У�ȷ������ж�������в������---- */
		int index;// ���������������
		for (int i = 1; i < library.length; i++) {// ����������
			index = 0;// ������ǹ�0
			for (int j = 1; j <= library.length - i; j++) {// ����������
				int valueA = (int) library[j][1];// ��¼��ǰ�еĹ�ֵ
				int valueB = (int) library[index][1];// ��¼�����еĹ�ֵ
				if (valueA > valueB) {// �����ǰ�б����������ָ�
					index = j;// ����ǰ����¼Ϊ������
				}
			}
			// �����������зŵ��������ĩβ
			// ������ֵ
			Object value = library[library.length - i][1]; // �ѵ�һ��Ԫ��ֵ���浽��ʱ������
			library[library.length - i][1] = library[index][1]; // �ѵڶ���Ԫ��ֵ���浽��һ��Ԫ�ص�Ԫ��
			library[index][1] = value; // ����ʱ����Ҳ���ǵ�һ��Ԫ��ԭֵ���浽�ڶ���Ԫ����
			// ��������
			Object key = library[library.length - i][0];
			library[library.length - i][0] = library[index][0];
			library[index][0] = key;
			// �����ƽ�λ��
			Object step = library[library.length - i][2];
			library[library.length - i][2] = library[index][2];
			library[index][2] = step;
		}
		/*------------------------�����������-----------------*/
		return library;
	}
}
