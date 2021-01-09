package cn.edu.yql.gobang;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import javax.swing.ImageIcon;

/**
 * �������
 * 
 * GobangPanel�������̽�����壬
 * ��Ϸ�Ŀ��ƣ� ������Ϸ�Ŀ�ʼ�� ���塢���塢���䡢������������Ϸ����ͼ�ȣ�
 * ����������Ϸ��ʼʱ�� Ϊ˫����ҷ���������ɫ��ҵ��
 * 
 */
public class GobangPanel extends javax.swing.JPanel {
	private Image backImg;// ����ͼƬ
	private Image white_chessman_img;// ����ͼƬ
	private Image black_chessman_img;// ����ͼƬ
	private Image rightTop_img;// ����ͼƬ
	int chessWidth, chessHeight; // ���ӿ����߶�
	Dimension size; // �������Ĵ�С
	private boolean start = false; // ��Ϸ��ʼ
	private Object[] oldRec;// ���̼�¼
	Deque<byte[][]> chessQueue = new LinkedList(); // ��Ϸ��ֵĶ��м�¼
	private boolean turn = false; // �Ƿ��Լ�����
	private boolean towardsWin; // �Է�ʤ��
	private boolean win; // ʤ��
	private boolean draw; // ����
	private ChessPanel chessPanel;// �������
	public byte myColor = -2;// ��ʹ�õ�������ɫ
	private GobangModel gobangModel1;// ����ģ����

	/**
	 * �������Ĺ��췽��
	 */
	public GobangPanel() {
		URL white_url = getClass().getResource("/res/whiteChessman.png");
		URL black_url = getClass().getResource("/res/blackChessman.png");
		URL rightTop_url = getClass().getResource("/res/rightTop.gif");
		white_chessman_img = new ImageIcon(white_url).getImage(); // ��ʼ������ͼƬ
		black_chessman_img = new ImageIcon(black_url).getImage(); // ��ʼ������ͼƬ
		rightTop_img = new ImageIcon(rightTop_url).getImage();// ��ʼ�������ߵ������ϵ���ͼ
		size = new Dimension(getWidth(), getHeight());// ����������
		setPreferredSize(size);// ���ô�����Ĵ�С
		initComponents();// ����ʼ��
	}

	/**
	 * ������ϷΪδ��ʼ״̬
	 * 
	 * @param start
	 */
	public void setStart(boolean start) {
		chessQueue.clear();// �����Ϸ��������������
		this.start = start;
		if (chessPanel == null) {// ��������ǿյ�
			chessPanel = (ChessPanel) getParent();// ��ȡ�ϲ��������
		}
		repaint();// �ػ����
	}

	/**
	 * ��д�����paint�����������Լ����������
	 * 
	 * ��дGobangPanel���paint()������
	 * �ڻ������̵�ͬʱ�������������е�ֵ��
	 * �������еĺ���Ͱ�������ڶ�Ӧ�����ϡ�
	 * ����Ǵ��л�ʤ��־�����ӣ����������ϸ�������ͼ��:
	 * �����Ϸ��������������������ƻ�ʤ��Ϣ���֡�
	 */
	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;// ʹ���»�ͼ��
		super.paint(g); // ���ø���Ļ�ͼ����
		if (chessPanel != null) {
			chessPanel.setTurn(turn);
		}
		Composite composite = g.getComposite(); // ���ݺϳ�ģʽ
		drawPanel(g); // ���û������̵ķ���
		g.translate(4, 4);// ��4,4λ����Ϊ����ԭ��
		size = new Dimension(getWidth(), getHeight());// �����������Ĵ�С
		chessWidth = size.width / 15; // ��ʼ�����ӿ���15�����ӣ�
		chessHeight = size.height / 15; // ��ʼ�����Ӹ�
		byte[][] chessmanArray = gobangModel1.getChessmanArrayCopy();// ��ȡ��������
		for (int i = 0; i < chessmanArray.length; i++) {// ˫forѭ��������������ģ��
			for (int j = 0; j < chessmanArray[i].length; j++) {
				byte chessman = chessmanArray[i][j];
				int x = i * chessWidth;// ��ȡ�˴��������Ͻǵĺ�����
				int y = j * chessHeight;// ��ȡ�˴��������Ͻǵ�������
				if (chessman != 0)// ����˴�������
					if (chessman == GobangModel.WHITE_CHESSMAN) { // ����ǰ���
						// ���ư���ͼƬ����ָ�����ָ꣬����ߣ�����������
						g.drawImage(white_chessman_img, x, y, chessWidth,
								chessHeight, this);
					} else if (chessman == GobangModel.BLACK_CHESSMAN) {// ����Ǻ���
						g.drawImage(black_chessman_img, x, y, chessWidth,
								chessHeight, this);// ���ƺ���
					} else if (chessman == (byte) (GobangModel.WHITE_CHESSMAN ^ 8)) {// ����ǵ���ʤ�������߰���
						g.drawImage(white_chessman_img, x, y, chessWidth,
								chessHeight, this);// ���ư���
						g.drawImage(rightTop_img, x, y, chessWidth,
								chessHeight, this);// ��������
					} else if (chessman == (byte) (GobangModel.BLACK_CHESSMAN ^ 8)) {// ���Ƶ���ʤ�������ߺ���
						g.drawImage(black_chessman_img, x, y, chessWidth,
								chessHeight, this);// ���ƺ���
						g.drawImage(rightTop_img, x, y, chessWidth,
								chessHeight, this);// ��������
					}
			}
		}
		if (!isStart()) { // �����Ϸ�����ڿ�ʼ״̬
			// ������ڶԷ�ʤ���������Լ�ʤ�����ߺ���״̬������������ʾ��Ϣ
			if (towardsWin || win || draw) {
				g.setComposite(AlphaComposite.SrcOver.derive(0.7f)); // ͸���ĺϳɹ�������70%
				String mess = "�Է�ʤ��"; // ������ʾ��Ϣ
				g.setColor(Color.RED); // ����ǰ��ɫΪ��ɫ
				if (win) { // ������Լ�ʤ��
					mess = "��ʤ����"; // ����ʤ����ʾ��Ϣ
					g.setColor(new Color(0x007700)); // ������ɫǰ��ɫ
				} else if (draw) { // ����Ǻ���״̬
					mess = "��սƽ��"; // ���������ʾ��Ϣ
					g.setColor(Color.YELLOW); // ���ú�����Ϣʹ�û�ɫ��ʾ
				}
				// ������ʾ�ı�������Ϊ���顢��б�塢��С72
				Font font = new Font("����", Font.ITALIC | Font.BOLD, 72);
				g.setFont(font);// ���������
				// ��ȡ������Ⱦ�����Ķ���
				FontRenderContext context = g.getFontRenderContext();
				// ������ʾ��Ϣ���ı���ռ�õ����ؿռ�
				Rectangle2D stringBounds = font.getStringBounds(mess, context);
				double fontWidth = stringBounds.getWidth(); // ��ȡ��ʾ�ı��Ŀ��
				g.drawString(mess, (int) ((getWidth() - fontWidth) / 2),
						getHeight() / 2); // ���л�����ʾ��Ϣ
				g.setComposite(composite); // �ָ�ԭ�кϳɹ���
			} else { // �����ǰ��������δ��ʼ��Ϸ��״̬
				String mess = "�ȴ���ʼ��"; // ���������ʾ��Ϣ
				Font font = new Font("����", Font.ITALIC | Font.BOLD, 48);
				g.setFont(font); // ����48����������
				// ��ȡ������Ⱦ�����Ķ���
				FontRenderContext context = g.getFontRenderContext();
				// ������ʾ��Ϣ���ı���ռ�õ����ؿռ�
				Rectangle2D stringBounds = font.getStringBounds(mess, context);
				double fontWidth = stringBounds.getWidth(); // ��ȡ��ʾ�ı��Ŀ��
				g.drawString(mess, (int) ((getWidth() - fontWidth) / 2),
						getHeight() / 2); // ���л�����ʾ�ı�
			}
		}
	}

	/**
	 * �������̵ķ���
	 * 
	 * @param g
	 *            - ��ͼ����
	 */
	private void drawPanel(Graphics2D g) {
		Composite composite = g.getComposite(); // ���ݺϳɹ���
		Color color = g.getColor(); // ����ǰ����ɫ
		g.setComposite(AlphaComposite.SrcOver.derive(0.6f));// ����͸���ϳ�
		g.setColor(new Color(0xAABBAA)); // ����ǰ����ɫ
		g.fill3DRect(0, 0, getWidth(), getHeight(), true); // ���ư�͸���ľ���
		g.setComposite(composite); // �ָ��ϳɹ���
		g.setColor(color); // �ָ�ԭ��ǰ��ɫ
		int w = getWidth(); // ���̿��
		int h = getHeight(); // ���̸߶�
		int chessW = w / 15, chessH = h / 15; // ���ӿ�Ⱥ͸߶�
		int left = chessW / 2 + (w % 15) / 2; // ������߽�
		int right = left + chessW * 14; // �����ұ߽�
		int top = chessH / 2 + (h % 15) / 2; // �����ϱ߽�
		int bottom = top + chessH * 14; // �����±߽�
		for (int i = 0; i < 15; i++) {
			// ��ÿ������
			g.drawLine(left, top + (i * chessH), right, top + (i * chessH));
		}
		for (int i = 0; i < 15; i++) {
			// ��ÿ������
			g.drawLine(left + (i * chessW), top, left + (i * chessW), bottom);
		}
	}

	/**
	 * ����ʼ��
	 */
	private void initComponents() {
		gobangModel1 = GobangModel.getInstance(); // ��������ģ�͵�ʵ������
		// ����������¼�
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {// �������������
				formMouseClicked(evt);// �����Զ�����¼�
			}
		});
		setOpaque(false);// ��������͸����
		setLayout(null);// ʹ�����겼��
	}

	/**
	 * ������������еķ���
	 * ����ʵ��������幦�ܡ�
	 * ��������ʼ��ʱ���ô˷�����
	 * �˷��������ж�����������ϵ���λ�õ����꣬
	 * Ȼ��������ӵĿ�͸ߣ����ɵó��������ڵ����꣬
	 * �����̶�ά�����е�����λ�á�
	 * ����ǰ�����ߵ����ӳ������浽���������У�
	 * Ȼ�󴥷����������ػ淽����
	 * �Ϳ�������Ϸ�����Ͽ������µ������ˡ�
	 * 
	 * @param evt
	 */
	private void formMouseClicked(java.awt.event.MouseEvent evt) {
		// �����Ϸû�п�ʼ�����߶Է�û�п�ʼ��������û�з��䵽���ӣ�����û���ֵ�������
		if (!start || !isTowardsStart() || myColor == 0 || !turn) {
			return;
		}
		Point point = evt.getPoint();// �������������ϵ�λ��
		int xindex = point.x / chessWidth;// ���λ�ó����ӿ�� = ����λ��
		int yindex = point.y / chessHeight;// ���λ�ó����Ӹ߶� = ����λ��
		byte[][] chessmanArray = gobangModel1.getChessmanArray();// �����������
		if (chessmanArray[xindex][yindex] == 0) {// ������λ����û������
			turn = !turn;// ��ɥʧ����Ȩ��
			chessmanArray[xindex][yindex] = (byte) myColor; // �����ӷ�������
			gobangModel1.setChessmanArray(chessmanArray);// ���������ݸ��µ�����ģ�͵���
			chessPanel.backButton.setEnabled(false);// ���尴ť������
			repaint();// �ػ����
			int winColor = arithmetic(myColor, xindex, yindex);// �ж�������֮���Ƿ���ʤ��,����ʤ����������ɫ
			pustChessQueue(gobangModel1.getChessmanArray());// ����ǰ��ֱ��浽������
			// ���ж�ʤ��������ٷ���model�е��������飬��Ϊ���������ܴ��б�ʶ���ߵ���������
			if (winColor != 0 && winColor == myColor) {// �������ʤ�������Ǻ��ҵ�������ɫһ��
				chessPanel.send(ChessPanel.WIN); // ����ʤ������
				win = true;// ����ʤ���ı�־����Ϊtrue
				chessPanel.reInit();// ���³�ʼ����Ϸ״̬
			}
			chessPanel.send(chessmanArray);// ���͵�ǰ��������
		}
	}

	/**
	 * �����˽����ӷ�������
	 * 
	 * 
	 * @param xindex
	 *            -���ӵĺ�����
	 * 
	 * @param yindex
	 *            -���ӵ�������
	 */
	public void chessForMachine(int xindex, int yindex) {
		try {
			Thread.sleep(1000);// �����������1���ӳ�
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		byte[][] chessmanArray = gobangModel1.getChessmanArray();// ��ȡ��������
		turn = !turn;// ��һָ������Ȩ��
		chessmanArray[xindex][yindex] = GobangModel.BLACK_CHESSMAN;// �ڴ˴���һö����
		gobangModel1.setChessmanArray(chessmanArray);// ������������
		repaint();// �ػ����
		int winColor = arithmetic(GobangModel.BLACK_CHESSMAN, xindex, yindex);// �жϺ����Ƿ�ʤ��
		// ���ж�ʤ��������ٷ���model�е��������飬��Ϊ���������ܴ��б�ʶ���ߵ���������
		if (winColor != 0 && winColor == GobangModel.BLACK_CHESSMAN) {
			chessPanel.send(ChessPanel.WIN); // ����ʤ������
			win = false;// ���ʤ��״̬Ϊfalse
			towardsWin = true;// �Լң�AI��ʤ��״̬Ϊtrue
			chessPanel.reInit();// ���³�ʼ����Ϸ״̬
		}
	}

	/**
	 * ��������(�ػ�����)
	 */
	public void zhengliBoard() {
		repaint();// �ػ����
	}

	/**
	 * �������㷨
	 * 
	 * arithmetic�����в���n��ʾ������ɫ��
	 * Arow��ʾ�У�Acolumn��ʾ�У�
	 *  ��󷵻�ʤ��-����������ɫ��
	 *  ���������n��ͬ�����ӣ�����Щ���ӵ�ֵ�޸�Ϊ��n^8����
	 *  ���̻���ʱ�ͻ��ڡ�n^8���������ϸ�������ͼ����
	 * 
	 * @param n
	 *            - ����������ɫ������
	 * @param Arow
	 *            - �б��
	 * @param Acolumn
	 *            - �б��
	 * @return ʤ��һ����������ɫ������
	 */
	public int arithmetic(int n, int Arow, int Acolumn) {
		byte n8 = (byte) (n ^ 8);// ����ʤ�������ӵ�ֵ
		byte[][] note = gobangModel1.getChessmanArrayCopy();// ��ȡ��������ֵ������
		int BCount = 1;// ��¼���Ӹ�����������������5������ʤ��
		// ˮƽ����
		boolean Lbol = true;// Ĭ�������߿��Ի�ʤ
		boolean Rbol = true;// Ĭ�������߿��Ի�ʤ
		for (int i = 1; i <= 5; i++) {// �Բ�������Ϊԭ�㣬����ˮƽ�����߳�5��
			if ((Acolumn + i) > 14) {// ������ӳ����������
				Rbol = false;// �����߲����ʤ
			}
			if ((Acolumn - i) < 0) {// ������ӳ�����С����
				Lbol = false;// �����߲����ʤ
			}
			if (Rbol == true) {// ��������߿��ܻ��ʤ
				// ��������ߣ������ҵ����Ӷ�������ɫ��ͬ
				if (note[Arow][Acolumn + i] == n) {
					++BCount;// ����������
					note[Arow][Acolumn + i] = n8;// ����Щ���ӱ��Ϊ���ܵ��»�ʤ������
				} else {
					Rbol = false;// �����߲����ʤ
				}
			}
			if (Lbol == true) {// ��������߿��ܻ��ʤ
				// ��������ߣ������ҵ����Ӷ�������ɫ��ͬ
				if (note[Arow][Acolumn - i] == n) {
					++BCount;// ����������
					note[Arow][Acolumn - i] = n8;// ����Щ���ӱ��Ϊ���ܵ��»�ʤ������
				} else {
					Lbol = false;// �����߲����ʤ
				}
			}
			if (BCount >= 5) {// ���ͬ���͵����������ڵ���5��
				note[Arow][Acolumn] = n8;// ����������λ���ϵ�����Ҳ��Ϊ���ܵ��»�ʤ������
				gobangModel1.setChessmanArray(note);// ������������
				repaint();// �ػ�����
				return n; // ����ʤ��һ�������ӵ�ֵ
			}
		}

		// ��ֱ����
		note = gobangModel1.getChessmanArrayCopy();// ��ȡ���Ƶ�ֵ��δ�������ĵ�ֵ��
		boolean Ubol = true;// Ĭ�������߿��Ի�ʤ
		boolean Dbol = true;// Ĭ�������߿��Ի�ʤ
		BCount = 1;// ���Ӹ�����ʼ��
		for (int i = 1; i <= 5; i++) {// �Բ�������Ϊԭ�㣬���ڴ�ֱ�����߳�5��
			if ((Arow + i) > 14) {// ����������̵��������
				Dbol = false;// �����߲����ʤ
			}
			if ((Arow - i) < 0) {// ����������̵���С����
				Ubol = false;// �����߲����ʤ
			}
			if (Dbol == true) {// ��������߿��Ի�ʤ
				// ��������ߣ������ҵ����Ӷ�������ɫ��ͬ
				if (note[Arow + i][Acolumn] == n) {
					++BCount;// ����������
					note[Arow + i][Acolumn] = n8;// ����Щ���ӱ��Ϊ���ܵ��»�ʤ������
				} else {
					Dbol = false;// �����߲����ʤ
				}
			}
			if (Ubol == true) {// ��������߿��ܻ�ʤ
				// ��������ߣ������ҵ����Ӷ�������ɫ��ͬ
				if (note[Arow - i][Acolumn] == n) {
					++BCount;// ����������
					note[Arow - i][Acolumn] = n8;// ����Щ���ӱ��Ϊ���ܵ��»�ʤ������
				} else {
					Ubol = false;// �����߲����ʤ
				}
			}
			if (BCount >= 5) { // ���ͬ���͵����Ӵ��ڵ���5��
				note[Arow][Acolumn] = n8;// ����������λ���ϵ�����Ҳ��Ϊ���ܵ��»�ʤ������
				gobangModel1.setChessmanArray(note);// ������������
				repaint();// �ػ�����
				return n; // ����ʤ��һ��������
			}
		}

		// ��б����
		note = gobangModel1.getChessmanArrayCopy();// ��ȡ���Ƶ�ֵ��δ�������ĵ�ֵ��
		boolean LUbol = true;// Ĭ���������߿��Ի�ʤ
		boolean RDbol = true;// Ĭ���������߿��Ի�ʤ
		BCount = 1;// ���Ӹ�����ʼ��
		for (int i = 1; i <= 5; i++) {// �Բ�������Ϊԭ�㣬������б�����߳�5��
			if ((Arow - i) < 0 || (Acolumn - i < 0)) {// ����������ϵ�б��
				LUbol = false;// �������߲����ʤ
			}
			if ((Arow + i) > 14 || (Acolumn + i > 14)) {// ����������µ�б��
				RDbol = false;// �������߲����ʤ
			}
			if (LUbol == true) {// ����������߿��Ի�ʤ
				// ����������ߣ������ҵ����Ӷ�������ɫ��ͬ
				if (note[Arow - i][Acolumn - i] == n) {// �������б��������ͬ���͵�����
					++BCount;// ����������
					note[Arow - i][Acolumn - i] = n8;// ����Щ���ӱ��Ϊ���ܵ��»�ʤ������
				} else {
					LUbol = false;// �������߲����ʤ
				}
			}
			if (RDbol == true) {// ����������߿��Ի�ʤ
				// ����������ߣ������ҵ����Ӷ�������ɫ��ͬ
				if (note[Arow + i][Acolumn + i] == n) {// �������б��������ͬ���͵�����
					++BCount;// ����������
					note[Arow + i][Acolumn + i] = n8;// ����Щ���ӱ��Ϊ���ܵ��»�ʤ������
				} else {
					RDbol = false;// �������߲����ʤ
				}
			}
			if (BCount >= 5) {// ���ͬ���͵����Ӵ��ڵ���5��
				note[Arow][Acolumn] = n8;// ����������λ���ϵ�����Ҳ��Ϊ���ܵ��»�ʤ������
				gobangModel1.setChessmanArray(note);// ������������
				repaint();// �ػ�����
				return n; // ����ʤ��һ��������
			}
		}
		// ��б����
		note = gobangModel1.getChessmanArrayCopy();// ��ȡ���Ƶ�ֵ��δ�������ĵ�ֵ��
		boolean RUbol = true;// Ĭ���������߿��Ի�ʤ
		boolean LDbol = true;// Ĭ���������߿��Ի�ʤ
		BCount = 1;// ���Ӹ�����ʼ��
		for (int i = 1; i <= 5; i++) {// �Բ�������Ϊԭ�㣬���ڷ�б�����߳�5��
			if ((Arow - i) < 0 || (Acolumn + i > 14)) {// ����������ϵ�б��
				RUbol = false;// �������߲����ʤ
			}
			if ((Arow + i) > 14 || (Acolumn - i < 0)) {// ����������µ�б��
				LDbol = false;// �������߲����ʤ
			}
			if (RUbol == true) {// ����������߿��Ի�ʤ
				// ����������ߣ������ҵ����Ӷ�������ɫ��ͬ
				if (note[Arow - i][Acolumn + i] == n) {
					++BCount;// ����������
					note[Arow - i][Acolumn + i] = n8;// ����Щ���ӱ��Ϊ���ܵ��»�ʤ������
				} else {
					RUbol = false;// Ĭ���������߿��Ի�ʤ
				}
			}
			if (LDbol == true) {// ����������߿��Ի�ʤ
				// ����������ߣ������ҵ����Ӷ�������ɫ��ͬ
				if (note[Arow + i][Acolumn - i] == n) {
					++BCount;// ����������
					note[Arow + i][Acolumn - i] = n8;// ����Щ���ӱ��Ϊ���ܵ��»�ʤ������
				} else {
					LDbol = false;// Ĭ���������߿��Ի�ʤ
				}
			}
			if (BCount >= 5) {// ���ͬ���͵����Ӵ��ڵ���5��
				note[Arow][Acolumn] = n8;// ����������λ���ϵ�����Ҳ��Ϊ���ܵ��»�ʤ������
				gobangModel1.setChessmanArray(note);// ������������
				repaint();// �ػ�����
				return n;// ����ʤ��һ��������
			}
		}
		return 0;
	}

	/**
	 * ��¼��Ϸ����ǰ�����̼�¼
	 */
	public void oldRec() {
		oldRec = (Object[]) chessQueue.toArray();// ����Ϸ���м�¼ת��������
	}

	/**
	 * ��ȡ�����Ƿ��ѿ�ʼ
	 * 
	 * @return
	 */
	public boolean isTowardsStart() {
		ChessPanel panel = (ChessPanel) getParent();
		return panel.isTowardsStart();
	}

	/**
	 * ��ȡ���̼�¼
	 * 
	 * @return ���̼�¼
	 */
	public Object[] getOldRec() {
		return oldRec;
	}

	/**
	 * ��ȡ�Ƿ��ֵ��Լ�����
	 * 
	 * @return
	 */
	public boolean isTurn() {
		return turn;
	}

	/**
	 * �����Ƿ��ֵ��Լ�����
	 */
	protected void setTurn(boolean turn) {
		this.turn = turn;
		chessPanel.setTurn(turn);// �������ͬ����������Ȩ��
	}

	/**
	 * �Ƿ��ѿ�ʼ��Ϸ
	 * 
	 * @return
	 */
	protected boolean isStart() {
		return start;
	}

	/**
	 * ��ȡ�ҵ�������ɫ
	 * 
	 * @return
	 */
	public byte getMyColor() {
		return myColor;
	}

	/**
	 * �����ҵ�������ɫ
	 * 
	 * @param myColor
	 */
	public void setMyColor(byte myColor) {
		this.myColor = myColor;
	}

	/**
	 * ��ȡ�����Ƿ�ʤ��
	 * 
	 * @return
	 */
	public boolean isTowardsWin() {
		return towardsWin;
	}

	/**
	 * ���ö����Ƿ�ʤ��
	 * 
	 * @param towardsWin
	 */
	public void setTowardsWin(boolean towardsWin) {
		this.towardsWin = towardsWin;
	}

	/**
	 * ��ȡ���Լ��Ƿ�ʤ��
	 * 
	 * @return
	 */
	public boolean isWin() {
		return win;
	}

	/**
	 * �������Լ��Ƿ�ʤ��
	 * 
	 * @param win
	 */
	public void setWin(boolean win) {
		this.win = win;
	}

	/**
	 * ��ȡ���Լ��Ƿ����
	 * 
	 * @return
	 */
	public boolean isDraw() {
		return draw;
	}

	/**
	 * �����Ƿ����
	 * 
	 * @param draw
	 */
	public void setDraw(boolean draw) {
		this.draw = draw;
	}

	/**
	 * ��ȡ��Ϸ���м�¼
	 * 
	 * @return
	 */
	public Deque<byte[][]> getChessQueue() {
		return chessQueue;
	}

	/**
	 * ����Ϸ���������������� �����������¡��һ�ݶ��������飬�������ݳ�ͻ
	 * 
	 * @param chessmanArray
	 *            -������������
	 */
	public synchronized void pustChessQueue(byte[][] chessmanArray) {
		byte newArray[][] = new byte[15][15];// �����µ�����
		for (int i = 0; i < newArray.length; i++) {// ѭ�������е���������
			// �����������е�ֵ���Ƹ�������
			newArray[i] = Arrays.copyOf(chessmanArray[i], newArray[i].length);
		}
		chessQueue.push(newArray);// ����ǰ��ֱ��浽������
	}
}
