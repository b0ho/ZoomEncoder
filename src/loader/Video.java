package loader;
import java.io.File;
import java.io.IOException;

/**
 * 영상과 영상의 정보를 가지는 클래스 입니다.
 * @author admin
 *
 */
public class Video {
	
	/** Size 출력을 위한 용량 크기 : <b>Byte | KB | MB | GB</b> */
	public static enum VolUnit{ BYTE, KB, MB, GB }
	
	private File videoFile;
	private double framerate;
	private int duration = 0;
	private long videoSize;
	private int width;
	private int height;
	private String codec;
	private int bitrate = 0;
	
	/**
	 * 새로운 영상 파일을 만듭니다.
	 * @param f 영상 파일
	 */
	public Video(File f){
		setVideoFile(f);
	}
	
	/**
	 * 영상 파일을 설정합니다.
	 * @param f 영상 파일
	 */
	public void setVideoFile(File f){
		if(!f.isFile()) return;
		videoFile = f;
		setVideoSize();
	}
	
	/**
	 * 영상 파일을 반환합니다.
	 * @return 영상 파일
	 */
	public File getVideoFile(){
		return videoFile;
	}
	
	/**
	 * 영상 파일의 경로를 반환합니다.
	 * @return 영상 파일의 경로
	 */
	public String getVideoFilePath(){
		try {
			return videoFile.getCanonicalPath();
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * 영상 파일의 확장자를 반환합니다.
	 * @return 영상 파일의 확장자 (.avi 형식)
	 */
	public String getVideoExtension(){
		try {
			return videoFile.getCanonicalPath().substring(videoFile.getCanonicalPath().lastIndexOf('.'));
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * 영상 파일의 이름을 반환합니다.
	 * @return 확장자가 제거된 파일의 이름
	 */
	public String getVideoName(){
		return videoFile.getName().substring(0, videoFile.getName().lastIndexOf('.'));
	}
	
	/**
	 * 초당 프레임레이트를 설정합니다.
	 * @param fps 프레임레이트
	 */
	public void setFramerate(double fps){
		framerate = Math.abs(fps);
	}
	
	/**
	 * 초당 프레임레이트를 반환합니다.
	 * @return 프레임레이트
	 */
	public double getFramerate(){
		return framerate;
	}
	
	/**
	 * 영상의 재생 시간을 설정합니다.
	 * @param d 영상의 재생 시간 (초단위)
	 */
	public void setDuration(int d){
		duration = Math.abs(d);
	}
	
	/**
	 * 영상의 재생시간을 반환합니다.</br>
	 * 만일 이 값이 0이 반환되다면 해당 영상의 포맷이 이 값을 지원하지 않는 것입니다.
	 * @return 영상의 재생시간
	 */
	public int getDuration(){
		return duration;
	}
	
	/**
	 * 파일 용량 설정
	 */
	private void setVideoSize(){
		videoSize = videoFile.length();
	}
	
	/**
	 * 파일의 용량을 바이트 단위로 반환합니다.
	 * @return 파일의 용량
	 */
	public long getVideoSize(){
		return videoSize;
	}
	
	/**
	 * 파일의 용량을 지정된 사이즈 단위로 계산한 뒤 반환합니다.
	 * @param unit 용량 단위 (Byte, KB, MB, GB)
	 * @return 해당 단위의 파일의 용량 (소숫점 3자리까지)
	 */
	public double getVideoSize(VolUnit unit){
		switch(unit){
		case BYTE:
			return videoSize;
		case KB:
			return Math.round((videoSize / 1024.0)*1000.0)/1000.0;
		case MB:
			return Math.round((videoSize / 1024.0 / 1024.0)*1000.0)/1000.0;
		case GB:
			return Math.round((videoSize / 1024.0 / 1024.0 / 1024.0)*1000.0)/1000.0;
		default:
			return videoSize;
		}
	}
	
	/**
	 * 영상의 해상도를 지정합니다.
	 * @param w 가로 해상도
	 * @param h 세로 해상도
	 */
	public void setResolution(int w, int h){
		setWidth(w);
		setHeight(h);
	}
	
	/**
	 * 영상의 가로 해상도를 지정합니다.
	 * @param w 가로 해상도
	 */
	public void setWidth(int w){
		width = Math.abs(w);
	}
	
	/**
	 * 영상의 세로 해상도를 지정합니다.
	 * @param h 세로 해상도
	 */
	public void setHeight(int h){
		height = Math.abs(h);
	}
	
	/**
	 * 영상의 가로 해상도를 반환합니다.
	 * @return 가로 해상도
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * 영상의 세로 해상도를 반환합니다.
	 * @return 세로 해상도
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * 영상의 코덱을 지정합니다.
	 * @param c 코덱
	 */
	public void setCodec(String c){
		codec = c;
	}
	
	/**
	 * 영상의 코덱을 반환합니다.</br>
	 * 이 값이 null 이라면 FFMpeg으로 변환할 수 없는 타입입니다.
	 * @return 코덱
	 */
	public String getCodec(){
		return codec;
	}
	
	/**
	 * 영상의 비트레이트를 설정합니다.
	 * @param bit 비트레이트(kb 단위)
	 */
	public void setBitrate(int bit){
		bitrate = bit;
	}
	
	/**
	 * 영상의 비트레이트를 반환합니다.</br>
	 * 만일 이 값이 0이 반환되다면 해당 영상의 포맷이 이 값을 지원하지 않는 것입니다.
	 * @return 비트레이트 (kb 단위)
	 */
	public int getBitrate(){
		return bitrate;
	}

	/**
	 * 이름.확장자 형태의 비디오 정보를 반환합니다.
	 */
	@Override
	public String toString(){
		return getVideoName() + getVideoExtension();
	}
}
