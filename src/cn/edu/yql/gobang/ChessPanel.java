package cn.edu.yql.gobang;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Arrays;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

/**
 * �������
 * 
 */
public class ChessPanel extends javax.swing.JPanel {
	static ImageIcon WHITE_CHESS_ICON;// ��ɫ����ͼƬ
	static ImageIcon BLACK_CHESS_ICON;// ��ɫ����ͼƬ
	final static int OPRATION_REPENT = 0xEF; // ��������
	final static int OPRATION_NODE_REPENT = 0xCF; // ���ܻ�������
	final static int OPRATION_DRAW = 0xFE; // ��������
	final static int OPRATION_NODE_DRAW = 0xEE; // ���ܺ�������
	final static int OPRATION_START = 0xFd; // ��ʼ����
	final static int OPRATION_ALL_START = 0xEd; // ���ܿ�ʼ����
	final static int OPRATION_GIVEUP = 0xFc; // ��������
	final static int OPRATION_START_MACHINE = 0xBd; // ��ʼ�˻���ս����
	final static int WIN = 88; // ʤ������
	private boolean towardsStart = false;// �Է��Ƿ�ʼ
	private Image backImg;// ����ͼƬ
	protected JButton backButton;// ���尴ť
	private JToggleButton backplayToggleButton;// �طŰ�ť
	private JButton giveupButton;// �����ǩ
	private GobangPanel gobangPanel1;// ����������
	private JButton heqiButton;// ���尴ť
	private JLabel jLabel5;// ��������ռλ��ǩ
	private JLabel jLabel6;// ��������ռλ��ǩ
	private JPanel jPanel1;// �·���ť���
	private JPanel jPanel2;// ����ҵ���Ϣ���
	private JPanel jPanel3;// �Ҳ�Է���Ϣ���
	private JPanel jPanel4;// �Ϸ�������
	protected JLabel leftInfoLabel;// ���ͷ��
	protected JLabel myChessColorLabel;// �ҵ�������ɫͼƬ
	protected JLabel rightInfoLabel;// �Ҳ�ͷ��
	private JButton startButton;// ��ʼ��ť
	protected JLabel towardsChessColorLabel;// �Է�������ɫͼƬ
	int backIndex = 1;// ����ͼƬ����

	/**
	 * �������Ĺ��췽��
	 */
	public ChessPanel() {
		WHITE_CHESS_ICON = new javax.swing.ImageIcon(getClass().getResource(
				"/res/whiteChess.png")); // ��ʼ���������ͼƬ
		BLACK_CHESS_ICON = new javax.swing.ImageIcon(getClass().getResource(
				"/res/blackChess.png")); // ��ʼ���������ͼƬ
		URL url = getClass().getResource("/res/bg/1.jpg");// ��ȡsrc·����ͼƬ
		backImg = new ImageIcon(url).getImage(); // ��ʼ������ͼƬ
		initComponents(); // ���ó�ʼ������ķ���
	}

	/**
	 * ��дpaintComponent���������Ʊ���ͼƬ
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// ���Ʊ���ͼƬ
		g.drawImage(backImg, 0, 0, getWidth(), getHeight(), null);
	}

	/**
	 * ����������ɫ�ķ������������ɫΪ��
	 * 
	 * @param color
	 *            - ָ����ɫ�����ͼƬ
	 */
	public void setChessColor(ImageIcon color) {
		myChessColorLabel.setIcon(color); // ���ñ����û������ͼ��
		if (color.equals(WHITE_CHESS_ICON)) { // �������Ϊ����
			gobangPanel1.setMyColor(GobangModel.WHITE_CHESSMAN);// ��ʹ�ð���
			towardsChessColorLabel.setIcon(BLACK_CHESS_ICON);// ����ʹ�ú���
		} else if (color.equals(BLACK_CHESS_ICON)) {// //�������Ϊ����
			gobangPanel1.setMyColor(GobangModel.BLACK_CHESSMAN);// ��ʹ�ú���
			towardsChessColorLabel.setIcon(WHITE_CHESS_ICON);// ����ʹ�ð���
		}
		revalidate();// �Զ���֤������������
	}

	/**
	 * �����ֻ�״̬�ķ���
	 * 
	 * @param turn
	 *            - �Ƿ�������Ȩ��
	 */
	public void setTurn(boolean turn) {
		if (turn) { // ����������Ȩ��
			myChessColorLabel.setVisible(true); // ��ʾ���
			towardsChessColorLabel.setVisible(false); // ���ضԼ����
		} else {// ����
			myChessColorLabel.setVisible(false); // �����Լ������
			towardsChessColorLabel.setVisible(true); // ��ʾ�Լҵ����
		}
	}

	/**
	 * �����ҵ������
	 * 
	 * �÷����ڽ��ջ���ָ���ִ�����
	 * �����ж������¼�������Ƿ������Ч��¼��
	 * �������˫���������¼����ϳ�������ε��������ݣ�
	 * Ȼ�󽫴����е��������ݻ��ˡ�
	 * 
	 */
	public synchronized void repentOperation() {
		// ��ȡ�������
		Deque<byte[][]> chessQueue = gobangPanel1.getChessQueue();
		if (chessQueue.isEmpty()) {// �����������ֵ
			return;
		}
		// ��ǰ�����ǰ��2��
		for (int i = 0; i < 2 && !chessQueue.isEmpty(); i++) {
			chessQueue.pop(); // �������岽��
		}
		if (chessQueue.size() < 1) {// ���������û���κ�ֵ��
			chessQueue.push(new byte[15][15]);// ��һ���µ����ݣ������̣����������
		}
		byte[][] pop = chessQueue.peek();// ��ȡ�����������ϵ���������
		GobangModel.getInstance().setChessmanArray(pop);// �������̵����Ӳ���
		repaint();// ���»��ƽ���
	}

	/**
	 * ��������
	 * 
	 * @param opration
	 *            -Ҫ���͵�����
	 */
	public void send(Object opration) {
		MainFrame mainFrame = (MainFrame) getRootPane().getParent();// ��ȡ���Ĵ������
		mainFrame.send(opration); // ��������

	}

	/**
	 * ���³�ʼ����Ϸ״̬
	 */
	void reInit() {
		gobangPanel1.oldRec();// ��¼��Ϸ����ǰ�����̼�¼
		startButton.setEnabled(true);// ��ʼ��ť������
		giveupButton.setEnabled(false);// ���䲻����
		heqiButton.setEnabled(false);// ���尴ť������
		backButton.setEnabled(false);// ���尴ť������
		gobangPanel1.setStart(false);// ������ϷΪδ��ʼ״̬
		setTowardsStart(false);// ���ƶԷ�Ϊδ��ʼ״̬
	}

	/**
	 * Ϊ˫����ҷ������ӵķ���
	 */
	private void fenqi() {
		MainFrame frame = (MainFrame) getRootPane().getParent(); // ��ȡ���������

		if (LoginPanel.isManMachineWar) {// ������˻���ս״̬
			frame.getChessPanel1().setChessColor(ChessPanel.WHITE_CHESS_ICON);// ��ҹ̶�ʹ�ð���
			frame.getChessPanel1().getGobangPanel1().setTurn(true);// �������
		} else {// �������Ҷ�ս״̬
			// ��ȡ�Լҿ�ʼ��Ϸ��ʱ��
			long towardsTime = frame.getTowardsUser().getTime().getTime();
			// ��ȡ�Լ���ʼ��Ϸ��ʱ��
			long meTime = frame.getUser().getTime().getTime();
			// ����ҿ�ʼ���¼����ڵ��ڶ���
			if (meTime >= towardsTime) {
				frame.getChessPanel1().setChessColor(
						ChessPanel.WHITE_CHESS_ICON);// ��ʹ�ð���
				frame.getChessPanel1().getGobangPanel1().setTurn(true);// �ֵ���������
			} else {
				frame.getChessPanel1().setChessColor(
						ChessPanel.BLACK_CHESS_ICON);// ��ʹ�ú���
				frame.getChessPanel1().getGobangPanel1().setTurn(false);// û�ֵ�������
			}
		}
	}

	/**
	 * ���� ������̵ķ���������ʹ��1��-1�ƶ�������̵����ӣ�ʹ��0�������
	 * 
	 * ��������ʼ����ť֮�����֡�����������Ļ����ʧ���Ķ�����
	 * ��ζ�������ChessPanel(�������)���е�fillChessBoard()����ʵ�ֵģ�
	 * �˷�������ÿ��10����������������������ӡ�
	 * fillChessBoard()�����еĲ���chessman��ʾ�������ӳ�����
	 * -1��ʾ���壬0��ʾ�����ӣ�1��ʾ���塣
	 * 
	 * @param chessman
	 *            - ������̵����ӵ���ɫ����
	 */
	private void fillChessBoard(final byte chessman) {
		try {
			Runnable runnable = new Runnable() { // ���������Ķ����߳�
				/**
				 * �̵߳����巽��
				 * 
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					byte[][] chessmanArray = GobangModel.getInstance()
							.getChessmanArray(); // ��ȡ��������
					for (int i = 0; i < chessmanArray.length; i += 2) {
						try {
							Thread.sleep(10); // �������ʱ��
						} catch (InterruptedException ex) {
							Logger.getLogger(ChessPanel.class.getName()).log(
									Level.SEVERE, null, ex);
						}
						// ʹ��ָ����ɫ��������������һ��
						Arrays.fill(chessmanArray[i], chessman);// ���ż����
						Arrays.fill(chessmanArray[(i + 1) % 15], chessman);// ���������
						GobangModel.getInstance().setChessmanArray(
								chessmanArray); // ���������ϵ�����
						gobangPanel1.paintImmediately(0, 0, getWidth(),
								getHeight()); // �����ػ�ָ�����������
					}
				}
			};
			// // ���¼�������ִ������
			if (SwingUtilities.isEventDispatchThread()) {// ����ǵ�ǰ������߳�
				runnable.run();// �߳�ֱ��ִ��
			} else {
				SwingUtilities.invokeAndWait(runnable);// ָ�ɸ��̵߳ȴ�ִ��
			}
		} catch (Exception ex) {
			Logger.getLogger(ChessPanel.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	/**
	 * ��ʼ���������ķ�����Ϊ�������ʵ����������ӽ���Ĳ��ֵ�Ч��
	 */
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		jPanel1.setOpaque(false);
		backButton = new javax.swing.JButton();
		heqiButton = new javax.swing.JButton();
		startButton = new javax.swing.JButton();
		giveupButton = new javax.swing.JButton();
		backplayToggleButton = new javax.swing.JToggleButton();
		jPanel2 = new javax.swing.JPanel();
		jPanel2.setOpaque(false);
		jLabel5 = new javax.swing.JLabel();
		leftInfoLabel = new javax.swing.JLabel();
		leftInfoLabel.setForeground(new Color(0, 255, 0));
		leftInfoLabel.setFont(new Font("����", Font.PLAIN, 22));
		myChessColorLabel = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		jPanel3.setOpaque(false);
		jLabel6 = new javax.swing.JLabel();
		rightInfoLabel = new javax.swing.JLabel();
		rightInfoLabel.setForeground(Color.GREEN);
		rightInfoLabel.setFont(new Font("����", Font.PLAIN, 22));
		towardsChessColorLabel = new javax.swing.JLabel();
		jPanel4 = new javax.swing.JPanel();
		jPanel4.setOpaque(false);
		gobangPanel1 = new cn.edu.yql.gobang.GobangPanel();

		setLayout(new java.awt.BorderLayout());
		setOpaque(false);

		backButton.setText("����");
		backButton.setEnabled(false);
		backButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				backButtonActionPerformed(evt);
			}
		});
		jPanel1.add(backButton);

		heqiButton.setText("����");
		heqiButton.setEnabled(false);
		heqiButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				heqiButtonActionPerformed(evt);
			}
		});
		jPanel1.add(heqiButton);

		startButton.setText("��ʼ");
		startButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				startButtonActionPerformed(evt);
			}
		});
		jPanel1.add(startButton);

		giveupButton.setText("����");
		giveupButton.setEnabled(false);
		giveupButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				giveupButtonActionPerformed(evt);
			}
		});
		jPanel1.add(giveupButton);

		backplayToggleButton.setText("��Ϸ�ط�");
		backplayToggleButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						backplayToggleButtonActionPerformed(evt);
					}
				});
		jPanel1.add(backplayToggleButton);

		add(jPanel1, java.awt.BorderLayout.PAGE_END);

		final JButton button = new JButton();
		button.addActionListener(new ButtonActionListener());
		button.setText("��������");
		jPanel1.add(button);

		jPanel2.setPreferredSize(new java.awt.Dimension(110, 100));
		jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
				50, 60));

		jLabel5.setPreferredSize(new java.awt.Dimension(42, 55));
		jPanel2.add(jLabel5);

		leftInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/res/infoPanelLeft.png")));
		leftInfoLabel
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		leftInfoLabel
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jPanel2.add(leftInfoLabel);

		myChessColorLabel.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/res/whiteChess.png")));
		jPanel2.add(myChessColorLabel);

		add(jPanel2, java.awt.BorderLayout.LINE_START);

		jPanel3.setPreferredSize(new java.awt.Dimension(110, 100));
		jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
				50, 60));

		jLabel6.setPreferredSize(new java.awt.Dimension(42, 55));
		jPanel3.add(jLabel6);

		rightInfoLabel.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/res/infoPanel.png")));
		rightInfoLabel
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		rightInfoLabel
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jPanel3.add(rightInfoLabel);

		towardsChessColorLabel.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/res/blackChess.png"))); // NOI18N
		jPanel3.add(towardsChessColorLabel);

		add(jPanel3, java.awt.BorderLayout.LINE_END);

		jPanel4.setLayout(new java.awt.BorderLayout());

		add(jPanel4, java.awt.BorderLayout.PAGE_START);

		add(gobangPanel1, java.awt.BorderLayout.CENTER);

		javax.swing.GroupLayout gobangPanel1Layout = new javax.swing.GroupLayout(
				gobangPanel1);
		gobangPanel1Layout.setHorizontalGroup(gobangPanel1Layout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0,
						280, Short.MAX_VALUE));
		gobangPanel1Layout.setVerticalGroup(gobangPanel1Layout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0,
						248, Short.MAX_VALUE));
		gobangPanel1.setLayout(gobangPanel1Layout);
	}

	/**
	 * ��ʼ��ť���¼�������
	 * 
	 * @param evt
	 *            - �¼�����
	 */
	private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
		MainFrame mainFrame = (MainFrame) getRootPane().getParent();
		if (mainFrame.serverSocket == null) {// ����������׽����������ӵ�
			JOptionPane.showMessageDialog(this, "��ȴ��Է����ӡ�");
			return;
		}
		if (gobangPanel1.isStart()) {// ���Ѿ���ʼ��Ϸ��
			return;
		}
		// ���ø�����ť�Ŀ���״̬
		startButton.setEnabled(false);// ��ʼ��ť������
		gobangPanel1.setStart(true); // ������Ϸ�Ŀ�ʼ״̬Ϊ��ʼ
		gobangPanel1.setTowardsWin(false); // ���öԼ�ʤ��״̬
		gobangPanel1.setWin(false); // �����Լ�ʤ��״̬
		gobangPanel1.setDraw(false); // ���ú���״̬
		if (LoginPanel.isManMachineWar) {
			send(OPRATION_START_MACHINE);// ���Ϳ�ʼ�˻���սָ��
			giveupButton.setEnabled(false);// �˻���ս����ʹ�����䰴ť
			heqiButton.setEnabled(false);// �˻���ս����ʹ�ú��尴ť
			backButton.setEnabled(false);// �˻���ս����ʹ�û��尴ť
		} else {
			send(OPRATION_START);// ���Ϳ�ʼָ��
			giveupButton.setEnabled(true);// ��Ҷ�ս��ʹ�����䰴ť
			heqiButton.setEnabled(true);// ��Ҷ�ս��ʹ�ú��尴ť
			backButton.setEnabled(true);// ��Ҷ�ս��ʹ�û��尴ť
		}
		fenqi(); // ����˫������
		
		fillChessBoard(gobangPanel1.getMyColor());// ʹ���Լ���������ɫ����
		fillChessBoard((byte) 0); // ʹ�ÿ���������
		byte[][] data = new byte[15][15]; // ����һ���յ����̲���
		GobangModel.getInstance().setChessmanArray(data);// ��������ʹ�ÿղ���
	}

	/**
	 * ���䰴ť���¼�������
	 * 
	 * @param evt
	 *            - ��ť���¼�����
	 */
	private void giveupButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// ���û���Լ����壬��ʾ�û��ȴ�
		if (!gobangPanel1.isTurn()) {
			JOptionPane.showMessageDialog(this, "û���������ء�", "��ȴ�...",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		send(OPRATION_GIVEUP);// ��������ָ��
		// ����һ���µ��߳�ʹ���䰴ť5�벻����
		new Thread() {
			@Override
			public void run() {
				try {
					giveupButton.setEnabled(false);// ������
					sleep(5000);
					giveupButton.setEnabled(true);// ����
				} catch (InterruptedException ex) {
					Logger.getLogger(ChessPanel.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}.start();// �߳�����
	}

	/**
	 * ���尴ť���¼�������
	 * 
	 * @param evt
	 */
	private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// ���û���Լ�����
		if (!gobangPanel1.isTurn()) {
			JOptionPane.showMessageDialog(this, "û���������ء�", "��ȴ�...",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		send(OPRATION_REPENT);// ���ͻ�������
		new Thread() { // �����µ��̣߳�ʹ���尴ť����5��
			@Override
			public void run() {
				try {
					backButton.setEnabled(false);// ������
					sleep(5000);
					backButton.setEnabled(true);// ����
				} catch (InterruptedException ex) {
					Logger.getLogger(ChessPanel.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}.start();// �����߳�
	}

	/**
	 * ���尴ť���¼�������
	 * 
	 * @param evt
	 *            - ��ť��action�¼�����
	 */
	private void heqiButtonActionPerformed(java.awt.event.ActionEvent evt) {
		send(OPRATION_DRAW);// ���ͺ���ָ��
		new Thread() { // �����µ��߳�ʹ���尴ť5�벻����
			public void run() {
				try {
					heqiButton.setEnabled(false);// ������
					sleep(5000);
					heqiButton.setEnabled(true);// ����
				} catch (InterruptedException ex) {
					Logger.getLogger(ChessPanel.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}.start();// �����߳�
	}

	/**
	 * ��Ϸ�طŰ�ť���¼�������
	 * 
	 * ������ս��������Ե�������Ϸ�طš���ť��
	 * �鿴��һ�ֵĻطŶ�����
	 * ��ʱ�����ChessPanel(�������)���е�backplayToggleButtonActionPerformed0������
	 * �÷�����ȡGobangPanel (�������)���б������̼�¼�Ķ�������oldRec,
	 * Ȼ�����ν���¼�е���ְ���һ��ˢ��һ�ε�Ƶ��չ���������ϡ�

	 * 
	 * @param evt
	 *            - �¼�����
	 */
	private void backplayToggleButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		if (gobangPanel1.isStart()) {// �����Ϸ�����У���ʾ�û���Ϸ�������ڹۿ���Ϸ�ط�
			// �����Ի���
			JOptionPane.showMessageDialog(this, "������Ϸ�����󣬹ۿ���Ϸ�طš�");
			backplayToggleButton.setSelected(false);// ȡ����ťѡ��״̬
			return;
		}
		if (LoginPanel.isManMachineWar) {// ������˻���սģʽ
			// �����Ի���
			JOptionPane.showMessageDialog(this, "�˻�ģʽ�ݲ�֧�ֻطš�");
			backplayToggleButton.setSelected(false);// ȡ����ťѡ��״̬
			return;
		}
		if (!backplayToggleButton.isSelected()) {// �����ťû�б�ѡ��
			backplayToggleButton.setText("��Ϸ�ط�");// ���İ�ť��ʾ�ı�
		} else {
			backplayToggleButton.setText("��ֹ�ط�");// ���İ�ť��ʾ�ı�
			new Thread() { // �����µ��̲߳�����Ϸ��¼
				public void run() {// �߳����з���
					Object[] toArray = gobangPanel1.getOldRec();// ��ȡ���̼�¼
					if (toArray == null) {// ������������̼�¼
						// ������ʾ��
						JOptionPane.showMessageDialog(ChessPanel.this,
								"û����Ϸ��¼", "��Ϸ�ط�", JOptionPane.WARNING_MESSAGE);
						backplayToggleButton.setText("��Ϸ�ط�");// ���İ�ť��ʾ�ı�
						backplayToggleButton.setSelected(false);// ȡ����ťѡ��״̬
						return;// ���������߳�
					}
					// �������Ľ�����֣������Է�ʤ������ʤ���ˡ���սƽ��
					gobangPanel1.setTowardsWin(false);// �Է�ʤ��
					gobangPanel1.setWin(false);// ��ʤ����״̬
					gobangPanel1.setDraw(false);// ����״̬
					// ������û��ʼ��Ϸ�����һطŰ�ť��ѡ�еģ�����������̼�¼����
					for (int i = toArray.length - 1; !gobangPanel1.isStart()
							&& backplayToggleButton.isSelected() && i >= 0; i--) {
						try {
							Thread.sleep(1000); // �߳�����1��
						} catch (InterruptedException ex) {
							//��¼��־
							Logger.getLogger(ChessPanel.class.getName()).log(
									Level.SEVERE, null, ex);
						}
						// ������Ϸ��¼����ÿһ����Ϸ������
						GobangModel.getInstance().setChessmanArray((byte[][]) toArray[i]); 
						gobangPanel1.repaint(); // �ػ�����
					}
					backplayToggleButton.setSelected(false);// ȡ����ťѡ��״̬
					backplayToggleButton.setText("��Ϸ�ط�");// ���İ�ť����
				}
			}.start();// �����߳�
		}
	}

	/**
	 * ��������ͼƬ�İ�ť�¼�������
	 * 
	 * ��Ҷ�ս���˻���ս���и��������Ĺ��ܣ�
	 * ������������������ť��
	 * ����ChessPanel (�������)���е�ButtonActionListener�Զ��尴ť����¼�������
	 * �ü�����ʹ�������㷨��
	 * ��backIndex��ֵ��1һ9�ķ�Χ��ѭ����
	 * Ȼ����Ŀ�еġ�/res/bg/�����¶�ȡ��Ӧ�������Ƶ�ͼƬ��
	 * ���ͼƬ���¼��ص����̽����С�
	 * 
	 * @author Li Zhong Wei
	 */
	private class ButtonActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			backIndex = backIndex % 9 + 1; // ��ȡ9�ű���ͼƬ�������ĵ���
			URL url = getClass().getResource("/res/bg/" + backIndex + ".jpg");
			backImg = new ImageIcon(url).getImage(); // ��ʼ������ͼƬ
			repaint(); // ���»����������
		}
	}

	/**
	 * �Է��Ƿ��ѿ�ʼ��Ϸ
	 * 
	 * @return
	 */
	public boolean isTowardsStart() {
		return towardsStart;
	}

	/**
	 * ���öԷ���ʼ״̬
	 * 
	 * @param towardsStart
	 */
	public void setTowardsStart(boolean towardsStart) {
		this.towardsStart = towardsStart;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return
	 */
	public GobangPanel getGobangPanel1() {
		return gobangPanel1;
	}
}
