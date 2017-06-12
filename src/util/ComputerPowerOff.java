package util;
import java.io.*;

/**
 * 컴퓨터의 전원을 종료하는 프로세스를 호출하는 클래스 입니다.
 * @author admin
 *
 */
public class ComputerPowerOff {
	
	/**
	 * 컴퓨터의 전원을 종료합니다.
	 * @param time 초 뒤 종료
	 */
	public static void powerOff(int time){
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec("C:/WINDOWS/system32/cmd.exe");
			OutputStream os = process.getOutputStream();
			String option = "shutdown -s -f -t " + time + " -c \"Zoom Encoder User Option\" \n\r";
			os.write(option.getBytes());
			os.close();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				return;
			}
		} catch (IOException e) {
			return;
		}
	}
	
	/**
	 * 컴퓨터 전원의 종료를 취소합니다.
	 */
	public static void powerOffCancel(){
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec("C:/WINDOWS/system32/cmd.exe");
			OutputStream os = process.getOutputStream();
			String option = "shutdown -a \n\r";
			os.write(option.getBytes());
			os.close();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				return;
			}
		} catch (IOException e) {
			return;
		}
	}
}
