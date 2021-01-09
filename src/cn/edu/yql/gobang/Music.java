package cn.edu.yql.gobang;

import java.io.File;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

/**
 * 总结步骤：
1 获取你要播放的音乐文件
2、定义一个AudioInputStream用于接收输入的音频数据
3、使用AudioSystem来获取音频的音频输入流(处理（抛出）异常)
4、使用AudioFormat来获取AudioInputStream的格式
5、创建一个源数据行
6、获取受数据行支持的音频格式 DataLine.info 如果采用.getSourceDataLine()方法可以省略）
7、获取与上面类型相匹配的行 写到源数据行里 二选一
8、打开具有指定格式的行，这样可以使行获得资源并进行操作
9、允许某个数据行执行数据i/o
10、写数据
11、从音频流读取指定的最大数量的数据字节，并将其放入给定的字节数组中。
12、读取哪个数组
13、读取了之后将数据写入混频器,开始播放
 * 
 */

public class Music {
	static boolean flag = true;
	
	public static void playMusic() {// 背景音乐播放
		 
		try {
			//AudioInputStream ais = AudioSystem.getAudioInputStream(new File("D:\\Users\\86199\\Desktop\\机器人1902\\20200103_001025.wav"));
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File("C:\\Users\\86173\\Music\\20200103_001025.wav"));
			AudioFormat aif = ais.getFormat();
			final SourceDataLine sdl;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
			sdl = (SourceDataLine) AudioSystem.getLine(info);
			sdl.open(aif);
			sdl.start();
			FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
			// value可以用来设置音量，从0-2.0
			double value = 2;
			float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
			fc.setValue(dB);
			int nByte = 0;
			int writeByte = 0;
			final int SIZE = 1024 * 64;
			byte[] buffer = new byte[SIZE];
			while (nByte != -1) {// 判断 播放/暂停 状态
				
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