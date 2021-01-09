package cn.edu.yql.gobang;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import cn.edu.yql.gobang.Music;

/**
 * ��������
 * 
 * �������ڳ�Ա�����ж����˷������׽��֡������׽��ֺͶ���������3�����������������Ӻ��շ���Ϣ:
 * ���ҷ���ҡ� �͡��Է���ҡ��������������ڼ�¼˫�������Ϣ;
 * �����¼�ı�����������򡢷��Ͱ�ť���û���Ϣ�����û���Ϣ�ı�����5���������������������
 */
public class MainFrame extends javax.swing.JFrame {
	
	private Socket socket;// �������׽��ֽ��յ��׽���
	private ObjectOutputStream objout;// ���������������׽��ַ�������
	private UserBean towardsUser;// �Է����
	protected UserBean user;// �ҵ����
	Socket serverSocket;// �������׽���
	private javax.swing.JTextArea chatArea;// �����¼�ı���
	private javax.swing.JTextField chatTextField;// ���������
	private cn.edu.yql.gobang.ChessPanel chessPanel1;// �������
	private cn.edu.yql.gobang.LoginPanel loginPanel1;// ��½���
	private javax.swing.JButton sendButton;// ���Ͱ�ť
	protected javax.swing.JTable userInfoTable;// �û���Ϣ���
	private javax.swing.JTextArea userInfoTextArea;// �û���Ϣ�ı���

	/**
	 * ��Լҷ�����Ϣ�ķ���
	 * 
	 * �Զ���һ��send0�������÷������ڷ�����Ϣ���Է�������
	 * ��Ϊ����������Object���ͣ�
	 * ���Բ������Է����ַ�����Ϣ��
	 * �����Է����û�������Ϸָ����������ݵ����ݡ�
	 * 
	 * @param message
	 *            - Ҫ���͵��ı����������͵Ķ���
	 */
	public void send(Object message) {
		try {
			objout.writeObject(message); // ������������Ӷ���
			objout.flush();// �����ˢ��
		} catch (IOException ex) {
			Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	/**
	 * �����û���Ϣ�ķ���
	 * 
	 * @param user
	 *            - �����û�����
	 */
	public void setUser(UserBean user) {
		this.user = user;// ��user��ֵ
		// ���û���Ϣ�������ǳ�
		userInfoTextArea.setText("�ǳƣ�" + user.getName() + "\n");
		// ���IP��Ϣ
		userInfoTextArea.append("�ɣУ�" + user.getHost().getHostAddress() + "\n");
		// ��ȡ�û���Ϣ������������ģ�Ͷ���
		DefaultTableModel model = (DefaultTableModel) userInfoTable.getModel();
		Vector dataVector = model.getDataVector();// ��ȡ����е��ж���
		Vector row = new Vector(); // ʹ���û���Ϣ�����������ݵ�����
		row.add(user.getName());
		row.add(user.getHost().getHostName());
		row.add(user.getTime());
		if (!dataVector.contains(row)) {// �����������е��û���������ӵĲ�ͬ
			model.getDataVector().add(row); // ���û���Ϣ��ӵ���������
		}
		// ���ñ����û����ǳƣ����̵������Ϣ���
		chessPanel1.leftInfoLabel.setText(user.getName());
		userInfoTable.revalidate();// �Զ���֤������������
	}

	/**
	 * ����Socket���Ӻͳ�ʼ������������ķ���
	 * 
	 * �Զ���һ��setSocket()������
	 * �÷������ڵ�¼����е��ã�
	 * ��������������Socket����
	 * ����ͬʱҲ��ʼ����objout�����������
	 * �����ڷ����ַ���������������󵽶Է�������
	 * 
	 * @param chatSocketArg
	 *            - Socket����
	 */
	public void setSocket(Socket chatSocketArg) {
		try {
			socket = chatSocketArg;// ���׽��ָ�ֵ
			OutputStream os = socket.getOutputStream(); // ��ȡSocket�������
			objout = new ObjectOutputStream(os); // �������������
		} catch (IOException ex) {
			Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null,
					ex);// ������־
		}
	}

	/**
	 * ������Ĺ��췽��
	 */
	public MainFrame() {
		initComponents(); // ��ʼ���������
		
		
		/*
		 * ��¼����ʹ���˰�͸����GlassPane������壬
		 * ��λ�ڴ������㣬SwingĬ�ϸ����Ϊ����ģʽ��
		 * ���������setGlassPane()��������¼�������Ϊ������塣
		 */
		setGlassPane(loginPanel1); // ���õ�¼���Ϊ�������
		
		loginPanel1.setVisible(true); // ��ʾ��¼���
	}

	/**
	 * ���öԼ��û���Ϣ�ķ���
	 * 
	 * @param user
	 *            - �Լ�ͨ�����緢�������û�����
	 */
	public void setTowardsUser(UserBean user) {
		this.towardsUser = user; // �Լ��û�����
		// ��ȡ�û���Ϣ�б�ı������ģ��
		DefaultTableModel model = (DefaultTableModel) userInfoTable.getModel();
		Vector row = new Vector(); // �������ر�������ݵ��������϶���
		row.add(towardsUser.getName()); // ����û�����
		row.add(towardsUser.getHost().getHostName());// �����������
		row.add(towardsUser.getTime()); // ����û���¼ʱ��
		Vector dataVector = model.getDataVector();// ��ȡ����е��ж���
		if (!dataVector.contains(row)) {// �����������е��û���������ӵĲ�ͬ
			model.getDataVector().add(row); // ����û���Ϣ�������
		}
		// ���öԼ��û�ͷ����ǳƣ����̵��Ҳ���Ϣ���
		chessPanel1.rightInfoLabel.setText(towardsUser.getName());
		userInfoTable.revalidate();// �Զ���֤������������
	}

	/**
	 * ��ʼ�����������ķ���
	 */
	private void initComponents() {
		loginPanel1 = new cn.edu.yql.gobang.LoginPanel();
		chessPanel1 = new cn.edu.yql.gobang.ChessPanel();
		javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
		javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
		javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
		jLabel1.setOpaque(true);
		jLabel1.setBackground(Color.WHITE);
		javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
		userInfoTextArea = new javax.swing.JTextArea();
		javax.swing.JPanel jPanel4 = new javax.swing.JPanel();
		javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
		userInfoTable = new javax.swing.JTable();
		javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
		javax.swing.JPanel jPanel5 = new javax.swing.JPanel();
		chatTextField = new javax.swing.JTextField();
		sendButton = new javax.swing.JButton();
		javax.swing.JScrollPane jScrollPane3 = new javax.swing.JScrollPane();
		chatArea = new javax.swing.JTextArea();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("����������");
		getContentPane().add(chessPanel1, java.awt.BorderLayout.CENTER);

		jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1,
				javax.swing.BoxLayout.PAGE_AXIS));

		jPanel3.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel3.setPreferredSize(new java.awt.Dimension(225, 50));
		jPanel3.setLayout(new java.awt.BorderLayout());

		jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/res/infoPanelLeft.png"))); // NOI18N
		jPanel3.add(jLabel1, java.awt.BorderLayout.WEST);

		userInfoTextArea.setColumns(20);
		userInfoTextArea.setEditable(false);
		userInfoTextArea.setLineWrap(true);
		userInfoTextArea.setRows(5);
		jScrollPane2.setViewportView(userInfoTextArea);

		jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

		jPanel1.add(jPanel3);

		jPanel4.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel4.setPreferredSize(new java.awt.Dimension(100, 20));
		jPanel4.setLayout(new java.awt.BorderLayout());

		jScrollPane1.setMaximumSize(new java.awt.Dimension(32767, 30));
		jScrollPane1.setPreferredSize(new java.awt.Dimension(241, 30));

		// ��ʼ���û���Ϣ�������ģ�ͣ�����Ϊ�գ�����Ϊ"�ǳ�", "����", "����ʱ��"
		userInfoTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] {}, new String[] { "�ǳ�", "����", "����ʱ��" }) {
			// ��д���༭����
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;// ��񲻿ɱ༭
			}
		});
		userInfoTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jScrollPane1.setViewportView(userInfoTable);

		jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

		jPanel1.add(jPanel4);

		jPanel2.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel2.setPreferredSize(new java.awt.Dimension(100, 300));
		jPanel2.setLayout(new java.awt.BorderLayout());

		jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5,
				javax.swing.BoxLayout.LINE_AXIS));

		// ������������(�س�)�¼�
		chatTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendButton.doClick();// ִ�з��Ͱ�ť�ĵ���¼�
			}
		});

		jPanel5.add(chatTextField);

		sendButton.setText("����");
		// ���Ͱ�ť����¼�
		sendButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendButtonActionPerformed(evt);// ���Ͱ�ť�¼�������
			}
		});
		jPanel5.add(sendButton);

		jPanel2.add(jPanel5, java.awt.BorderLayout.PAGE_END);

		chatArea.setColumns(20);
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		chatArea.setTabSize(4);
		jScrollPane3.setViewportView(chatArea);

		jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

		jPanel1.add(jPanel2);

		getContentPane().add(jPanel1, java.awt.BorderLayout.EAST);
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setBounds((screenSize.width - 1000) / 2, (screenSize.height - 700) / 2,
				1000, 700);
	}

	/**
	 * ���촰��ķ��Ͱ�ť�¼�������
	 * 
	 * �������Ͱ�ť���Խ�������е���Ϣ���͸��Է���
	 * ͬʱ����������չʾ���Լ��������¼���С�
	 * 
	 * @param evt
	 *            - �¼�����
	 */
	private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {
		String message = (String) chatTextField.getText(); // ��ȡ�ı���Ϣ
		if (message == null || message.isEmpty()) {// ����ı���null���ǿ�����
			return;
		}
		chatTextField.setText(""); // ����ı�������
		appendMessage(user.getName() + "��" + message); // �����͵���Ϣ��ӵ������¼
		send(message); // ������Ϣ
	}

	/**
	 * ���������Ϣ�ķ���
	 * 
	 * @param message
	 *            - ������Ϣ�ı�
	 */
	protected void appendMessage(final String message) {
		Runnable runnable = new Runnable() { // �����̶߳���
			@Override
			public void run() {
				chatArea.append("\n" + message); // �������ı��������׷�ӻ����ı�
			}
		};
		if (SwingUtilities.isEventDispatchThread()) {// �����ǰ�߳��� AWT �¼�ָ���߳�
			runnable.run(); // ���¼������߳���ִ�и��̶߳���
		} else {
			SwingUtilities.invokeLater(runnable);// ���߳��з�������еȴ�ִ��
		}
	}

	/**
	 * ����Socket������
	 * 
	 * �Զ���һ��startServer()������
	 * �÷���������ServerSocket���ʵ������
	 * �ö������ڽ���Զ���û������ӡ�
	 */
	public void startServer() {
		try {
			// ����Socket����������
			final ServerSocket chatSocketServer = new ServerSocket(9527);
			// ����������Ϣ���߳�
			new ReceiveThread(chatSocketServer, this).start();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "�������ֹ�ظ����У�ֻ��ͬʱ����һ��ʵ����",
					"����ظ����У�", JOptionPane.ERROR_MESSAGE);// �����Ի���
			System.exit(0);// �رճ���
			Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null,
					ex);// ������־
		}
	}

	/**
	 * ������
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame frame = new MainFrame();
				frame.startServer();// ����������
				frame.setVisible(true);// ����ɼ�
				new Thread(()->{while(true) {Music.playMusic();}
				}).start();// Lambda���ʽ
			}
		});
	}

	public void setSendButtonEnable(boolean enable) {
		sendButton.setEnabled(enable);
		chatTextField.setEditable(enable);
	}

	/**
	 * ��ȡ�������׽���
	 * 
	 * @return
	 */
	public Socket getServerSocket() {
		return serverSocket;
	}

	/**
	 * ��ȡ���������ص��׽���
	 * 
	 * @return
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return
	 */
	public ChessPanel getChessPanel1() {
		return chessPanel1;
	}

	/**
	 * ��ȡ�Է������Ϣ
	 * 
	 * @return
	 */
	public UserBean getTowardsUser() {
		return towardsUser;
	}

	/**
	 * ��ȡ���Լ�����Ϣ
	 * 
	 * @return
	 */
	public UserBean getUser() {
		return user;
	}
}
