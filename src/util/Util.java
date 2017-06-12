package util;
import java.io.File;

/**
 * 유틸 메소드 모음 클래스
 * @author admin
 *
 */
public class Util {
	
	/**
	 * 재귀적으로 내부의 파일과 디렉토리를 모두 제거한 뒤 자기 자신을 제거하는 메서드
	 * @param f 제거할 폴더
	 * @return 제거 완료 여부
	 */
	public static boolean deleteDirectory(File f){
		if(!f.exists()) return false;
		
		File[] files = f.listFiles();
		for(File file : files){
			if(file.isDirectory()) deleteDirectory(file);
			else file.delete();
		}
		
		return f.delete();
	}
	
	/**
	 * 재귀적으로 폴더 내부의 용량을 확인합니다.
	 * @param f 용량을 확인할 폴더
	 * @return 폴더의 용량
	 */
	public static long getFolderSize(File f){
		if(!f.exists()) return 0;
		
		long size = 0;
		File[] files = f.listFiles();
		for(File file : files){
			if(file.isDirectory()) size = size + getFolderSize(file);
			else size = size + file.length();
		}
		
		return size;
	}
	
	/**
	 * 초 단위의 시간을 HH:mm:ss 형태의 문자열로 바꿉니다.
	 * @param time 초 단위 시간
	 * @return HH:mm:ss 형태의 문자열
	 */
	public static String timeFormat(int time){
		int sec = time % 60;
		int min = time / 60 % 60;
		int hour = time / 60 / 60 % 24;
		StringBuffer str = new StringBuffer();
		if(hour >= 10){
			str.append(hour + ":");
		}else{
			str.append("0" + hour + ":");
		}
		if(min >= 10){
			str.append(min + ":");
		}else{
			str.append("0" + min + ":");
		}
		if(sec >= 10){
			str.append(sec);
		}else{
			str.append("0" + sec);
		}
		return str.toString();
	}
}
