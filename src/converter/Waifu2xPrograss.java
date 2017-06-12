package converter;
import java.io.File;
import java.util.EventListener;

import javax.swing.event.EventListenerList;

import converter.event.Waifu2xPrograssEvent;
import converter.event.Waifu2xPrograssListener;
import loader.Video;

/**
 * Waifu2x 작업의 별도 변환상태 처리 클래스
 * @author admin
 *
 */
public class Waifu2xPrograss extends Thread{
	
	private int fileLength = 0;
	private int resizeLength = 0;
	private File resizeDir;
	private EventListenerList listenerList = new EventListenerList();
	private Video video;
	private int oldLen = 0;
	
	/**
	 * 생성자.
	 * @param originalPath 원본 경로
	 * @param resizePath 확대 작업 후 저장될 경로
	 * @param fLen 원본 경로에 있는 이미지 개수
	 */
	public Waifu2xPrograss(String originalPath, String resizePath, Video video, int fLen){
		oldLen = fLen;
		this.video = video;
		fileLength = new File(originalPath).listFiles().length;
		resizeDir = new File(resizePath);
	}
	
	public Video getVideo(){
		return video;
	}
	
	/**
	 * 이벤트 리스너 추가 
	 * @param listener
	 */
	public void addWaifu2xPrograssListener(Waifu2xPrograssListener listener){
		listenerList.add(Waifu2xPrograssListener.class, listener);
	}
	
	/**
	 * 이벤트 리스너 제거
	 * @param listener
	 */
	public void removeWaifu2xPrograssListener(Waifu2xPrograssListener listener){
		listenerList.remove(Waifu2xPrograssListener.class, listener);
	}
	
	/**
	 * 이벤트 콜
	 * @param event
	 */
	private void fireConvertPrograssEvent(Waifu2xPrograssEvent event){
		EventListener[] listeners = listenerList.getListeners(Waifu2xPrograssListener.class);
		for(int i = 0 ; i < listeners.length ; i++){
			((Waifu2xPrograssListener)listeners[i]).waifu2xConvertPrograss((Waifu2xPrograssEvent)event);
		}
	}
	
	@Override
	/**
	 * 5초 단위로 스레드를 돌면서 변환 진행 상태를 체크
	 */
	public void run(){
		long workStartTime = System.currentTimeMillis();
		while(resizeLength < fileLength + oldLen){
			resizeLength = resizeDir.listFiles().length;
			try {
				Waifu2xPrograssEvent evt = new Waifu2xPrograssEvent(this, resizeLength, fileLength + oldLen, System.currentTimeMillis() - workStartTime, oldLen);
				fireConvertPrograssEvent(evt);
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
