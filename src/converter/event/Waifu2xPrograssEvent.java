package converter.event;
import java.util.EventObject;

/**
 * Waifu2x 변환 진행상황 이벤트 객체 입니다.
 * @author admin
 *
 */
public class Waifu2xPrograssEvent extends EventObject {
	
	private static final long serialVersionUID = 5704163901902921081L;
	
	/** 현재 까지 완료된 프레임 */
	public int currentFrame;
	/** 전체 프레임 수 */
	public int totalFrame;
	/** 작업 소요 시간 */
	public long workingTime;
	/** 이전 작업 프레임 */
	public int prevWorkFrame;
	
	/**
	 * 생성자
	 * @param source 이벤트 호출 객체
	 * @param currentFrame 현재 진행 프레임
	 * @param totalFrame 전체 프레임
	 * @param workingTime 작업 소요 시간
	 */
	public Waifu2xPrograssEvent(Object source, int currentFrame, int totalFrame, long workingTime, int prevWorkFrame) {
		super(source);
		this.currentFrame = currentFrame;
		this.totalFrame = totalFrame;
		this.workingTime = workingTime / 1000; //초로 들어오는 데이터를 밀리초로 바꿈
		this.prevWorkFrame = prevWorkFrame;
	}
}
