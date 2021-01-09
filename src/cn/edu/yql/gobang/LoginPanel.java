package cn.edu.yql.gobang;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import java.awt.Font;

/**
 * ��¼���
 * 
 * LoginPanel (��¼���)���Ǳ�����ĵ�¼���棬
 * ��̳���JPanel����࣬
 * ������¼��Ϣ�ı���͡���¼����ť�����˻���ս ����ť�������
 * ��¼�������isManMachineWar���Կ������ڼ�¼������Ϸ�Ƿ����˻���սģʽ
 * ��¼���治��Ҫ�����û��������룬
 * ����Ҫ��������ǳƺͶԷ�������IP��ַ��
 * �ǳƽ���ʾ����Ϸ�����У������ҷ��ͶԷ����سơ�
 * IP��ַ��ȷ���Է���ҵ�Ψһ������
 * ֻ��ȷ��˫����IP��ַ����˫����ͬ������֮����ܿ�ʼ��Ϸ��
 * 
 */
public class LoginPanel extends javax.swing.JPanel {

	private Socket socket;// �ͻ����׽���
	private UserBean user;// ���ش������û�
	private javax.swing.JButton closeButton;// �رհ�ť
	private javax.swing.JTextField ipTextField;// IP�����
	private javax.swing.JLabel nameLabel;// �ǳƱ�ǩ
	private javax.swing.JLabel ipLabel;// IP��ǩ
	private javax.swing.JButton loginButton;// ��½��ť
	private javax.swing.JTextField nameTextField;// �û��������
	private javax.swing.JButton machineButton;// �˻���ս��ť
	public static boolean isManMachineWar;// �Ƿ�Ϊ�˻���ս

	/**
	 * ���췽��
	 */
	public LoginPanel() {
		initComponents(); // ���ó�ʼ������ķ���
	}

	/**
	 * ��ʼ����¼����ķ���
	 */
	private void initComponents() {
		// ������������Լ����������΢������ڱ������ֵ�λ��
		java.awt.GridBagConstraints gridBagConstraints;
		nameLabel = new javax.swing.JLabel();// ��ʼ���ǳƱ�ǩ
		nameTextField = new javax.swing.JTextField();// ��ʼ���ǳ������
		ipLabel = new javax.swing.JLabel();// ��ʼ�����Է�IP����ǩ
		ipTextField = new javax.swing.JTextField();// ��ʼ��ip�����
		loginButton = new javax.swing.JButton();// ��ʼ����½��ť
		closeButton = new javax.swing.JButton();// ��ʼ���رհ�ť
		machineButton = new javax.swing.JButton();// ��ʼ���˻���ս��ť
		setForeground(java.awt.Color.gray);// ǰ��ɫΪ��ɫ
		setOpaque(false);// ������͸����
		setLayout(new java.awt.GridBagLayout());// ʹ�����������

		nameLabel.setFont(new Font("����", Font.ITALIC, 24));// �趨����
		nameLabel.setForeground(new java.awt.Color(255, 255, 255));// �趨��ɫ
		nameLabel.setText("��   �ƣ�");// �趨��������
		gridBagConstraints = new java.awt.GridBagConstraints();// ��ʼ����������Լ������
		gridBagConstraints.gridx = 0;// ���ڵ�һ��
		gridBagConstraints.gridy = 0;// ���ڵ�һ��
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;// �Ҷ���
		add(nameLabel, gridBagConstraints);// �������ӵ�ָ��λ��

		gridBagConstraints = new java.awt.GridBagConstraints();// ��ʼ����������Լ������
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;// ˮƽ������С
		gridBagConstraints.ipady = -5;// ����������
		gridBagConstraints.gridwidth = 2;// һ��ռ����������ô��
		// ����࣬�������3����߼��0���ײ����3���ұ߼��0
		gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
		add(nameTextField, gridBagConstraints);// �������ӵ�ָ��λ��

		ipLabel.setFont(new Font("����", Font.ITALIC, 24));
		ipLabel.setForeground(java.awt.Color.white);
		ipLabel.setText("�Է� IP��");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		add(ipLabel, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.ipady = -5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
		add(ipTextField, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 0, 0, 40);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		add(machineButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 0, 0, 40);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		add(loginButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 0, 0, 55);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		add(closeButton, gridBagConstraints);

		addMouseListener(new java.awt.event.MouseAdapter() {// �������¼�
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				formMouseClicked(evt);// ������λ�õ����
			}
		});

		machineButton.setText("�˻���ս");
		machineButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				machinButtonActionPerformed();
			}
		});

		loginButton.setText("��¼");
		loginButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loginButtonActionPerformed(evt);
			}
		});
		closeButton.setText("�ر�");
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeButtonActionPerformed(evt);// �رհ�ť��������ر�
			}
		});
	}

	/**
	 * �رհ�ť��������ر�
	 * 
	 * @param evt
	 */
	private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);// ����������ֹ����
	}

	/**
	 * ��������հ�λ��ʱ�����ķ���
	 * 
	 * @param evt
	 */
	private void formMouseClicked(java.awt.event.MouseEvent evt) {
		JOptionPane.showMessageDialog(this, "��û��¼�أ����ĵ㣿");// ������ʾ��
	}

	/**
	 * ��¼��ť���¼�������
	 * 
	 * @param evt
	 *            - ��ť���¼�����
	 */
	private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			// ��ȡ�������ʵ������
			MainFrame mainFrame = (MainFrame) getParent().getParent();
			String name = nameTextField.getText(); // ��ȡ�û��ǳ�
			if (name.trim().isEmpty()) {// �������û���κο���ʾ���ַ�
				JOptionPane.showMessageDialog(this, "�������ǳ�");
				return;
			}
			String ipText = ipTextField.getText(); // ��ȡ�Լ�IP��ַ
			if (ipText == null || ipText.isEmpty()) {// ���û�������κ�IP��ַ
				JOptionPane.showMessageDialog(this, "������Լ�IP��ַ");
				return;
			}
			ipTextField.setEditable(true);// IP��ַ�����ɱ��༭
			InetAddress ip = InetAddress.getByName(ipText);// ��ȡ�ĵ�ַ����
			if (ip.equals(InetAddress.getLocalHost())) {// ��������ַ���ұ�����ַ��ͬ
				JOptionPane.showMessageDialog(this, "���������Լ���IP��ַ");// ��ʾ�����������Լ���IP��ַ
				return;// ������ֹ
			}
			socket = new Socket(ip, 9527); // ����Socket���ӶԼ�����
			if (socket.isConnected()) { // ������ӳɹ�
				user = new UserBean(); // �����û�����
				Time time = new Time(System.currentTimeMillis()); // ��ȡ��ǰʱ�����
				user.setName(name); // ��ʼ���û��ǳ�
				user.setHost(InetAddress.getLocalHost()); // ��ʼ���û�IP
				user.setTime(time); // ��ʼ���û���¼ʱ��
				socket.setOOBInline(true); // ���ý������ݵĽ���
				mainFrame.setSocket(socket); // �����������Socket���Ӷ���
				mainFrame.setUser(user); // ��ӱ����û��������������
				mainFrame.send(user); // ���ͱ����û����󵽶Լ�����
				isManMachineWar = false;// ��Ǵ˾�Ϊ��Ҷ�ս
				setVisible(false); // ���ص�¼����
			}
		} catch (UnknownHostException ex) {
			// ����ǰ�쳣��¼ΪLevel.SEVERE����߼�����־����־����LoginPanel.class.getName()
			Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE,
					null, ex);
			JOptionPane.showMessageDialog(this, "�����IP����ȷ");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "�Է������޷�����");
			e.printStackTrace();
		}
	}

	/**
	 * �˻���ս�¼�������
	 */
	private void machinButtonActionPerformed() {
		MainFrame mainFrame = (MainFrame) getParent().getParent();// ��ȡ�������ʵ������
		String name = nameTextField.getText(); // ��ȡ�û��ǳ�
		if (name.trim().isEmpty()) {// ����û�û����������
			JOptionPane.showMessageDialog(this, "�������ǳ�");// �����Ի���
			return;
		}
		try {
			socket = new Socket("127.0.0.1", 9527);// �������ӱ��ص��׽���
			if (socket.isConnected()) { // ������ӳɹ�
				user = new UserBean(); // �����û�����
				// ��ȡ��ǰʱ�����
				Time time = new Time(System.currentTimeMillis());
				user.setName(name); // ��ʼ���û��ǳ�
				user.setHost(InetAddress.getLocalHost()); // ��ʼ���û�IP
				user.setTime(time); // ��ʼ���û���¼ʱ��
				socket.setOOBInline(true); // ���ý������ݵĽ���
				mainFrame.setSocket(socket); // �����������Socket���Ӷ���
				UserBean machine = new UserBean();// ����AI������
				machine.setName("������");// Ϊ��������������
				machine.setHost(InetAddress.getLocalHost()); // ��ʼ��������IP
				machine.setTime(new Time(0));// ����ʱ����Ϊһ���ϴ�ģ�ȷ��������ѡ����
				mainFrame.setUser(user); // ����ҷ��ڴ������
				mainFrame.setTowardsUser(machine); // �������˷����������ұ�
				mainFrame.setSendButtonEnable(false);// �˻���սȡ��������Ϣ����
				isManMachineWar = true;// ��Ǵ˾�Ϊ�˻���ս
				setVisible(false); // ���ص�¼����
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����������ķ���
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g; // ��ȡ2D��ͼ������
		Composite composite = g2.getComposite(); // ���ݺϳ�ģʽ
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.8f)); // ���û�ͼʹ��͸���ϳɹ���
		g2.fillRect(0, 0, getWidth(), getHeight()); // ʹ�õ�ǰ��ɫ�����οռ�
		g2.setComposite(composite); // �ָ�ԭ�кϳ�ģʽ
		super.paintComponent(g2); // ִ�г����������Ʒ���
	}

	void setLinkIp(String ip) {
		ipTextField.setText(ip);// IP�����д��ָ������
		ipTextField.setEditable(false);// IP����򲻿ɱ༭
		nameTextField.requestFocus();// ����������üǵý���
	}
}
