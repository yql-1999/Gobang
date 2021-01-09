package cn.edu.yql.gobang;

import java.io.File;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

/**
 * �ܽᲽ�裺
1 ��ȡ��Ҫ���ŵ������ļ�
2������һ��AudioInputStream���ڽ����������Ƶ����
3��ʹ��AudioSystem����ȡ��Ƶ����Ƶ������(�����׳����쳣)
4��ʹ��AudioFormat����ȡAudioInputStream�ĸ�ʽ
5������һ��Դ������
6����ȡ��������֧�ֵ���Ƶ��ʽ DataLine.info �������.getSourceDataLine()��������ʡ�ԣ�
7����ȡ������������ƥ����� д��Դ�������� ��ѡһ
8���򿪾���ָ����ʽ���У���������ʹ�л����Դ�����в���
9������ĳ��������ִ������i/o
10��д����
11������Ƶ����ȡָ������������������ֽڣ����������������ֽ������С�
12����ȡ�ĸ�����
13����ȡ��֮������д���Ƶ��,��ʼ����
 * 
 */

public class Music {
	static boolean flag = true;
	
	public static void playMusic() {// �������ֲ���
		 
		try {
			//AudioInputStream ais = AudioSystem.getAudioInputStream(new File("D:\\Users\\86199\\Desktop\\������1902\\20200103_001025.wav"));
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File("C:\\Users\\86173\\Music\\20200103_001025.wav"));
			AudioFormat aif = ais.getFormat();
			final SourceDataLine sdl;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
			sdl = (SourceDataLine) AudioSystem.getLine(info);
			sdl.open(aif);
			sdl.start();
			FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
			// value��������������������0-2.0
			double value = 2;
			float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
			fc.setValue(dB);
			int nByte = 0;
			int writeByte = 0;
			final int SIZE = 1024 * 64;
			byte[] buffer = new byte[SIZE];
			while (nByte != -1) {// �ж� ����/��ͣ ״̬
				
				if(flag) {
					
					nByte = ais.read(buffer, 0, SIZE);
					
					sdl.write(buffer, 0, nByte);
					
				}else {
					
					nByte = ais.read(buffer, 0, 0);
					
				}
				
			}
			sdl.stop();
 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}