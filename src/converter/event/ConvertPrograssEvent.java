package converter.event;
import java.util.EventObject;

import converter.ConvertState;
import loader.Video;

/**
 * 변환 진행상황 이벤트 객체 입니다.
 * @author admin
 *
 */
public class ConvertPrograssEvent extends EventObject {
	
	private static final long serialVersionUID = -481148025712265272L;

	/** 진행 상황 */
	public enum Mode{ Divide, Zoom, Merge, Complete }

	/** 비디오 */
	public Video video;
	/** 현재 상태 */
	public ConvertState currentState;
	/** 전체 프레임 */
	public int totalFrame;
	/** 작업 소요 시간 */
	public long workingTime;
	/** 진행 상황 */
	public Mode mode;
	/** 이전 작업 프레임 */
	public int prevWorkFrame;
	
	/**
	 * 생성자
	 * @param source 이벤트 호출 객체
	 * @param mode 진행 모드
	 * @param currentState 현재 상태
	 * @param totalFrame 전체 프레임
	 * @param workingTime 작업 소요 시간
	 */
	public ConvertPrograssEvent(Object source, Video video, Mode mode, ConvertState currentState, int totalFrame, long workingTime) {
		super(source);
		this.video = video;
		this.currentState = currentState;
		this.totalFrame = totalFrame;
		this.mode = mode;
		this.workingTime = workingTime / 1000;
		this.prevWorkFrame = 0;
	}
	
	/**
	 * 생성자
	 * @param source
	 * @param video
	 * @param mode
	 * @param currentState
	 * @param totalFrame
	 * @param workingTime
	 */
	public ConvertPrograssEvent(Object source, Video video, Mode mode, ConvertState currentState, int totalFrame, long workingTime, int prevWorkFrame) {
		super(source);
		this.video = video;
		this.currentState = currentState;
		this.totalFrame = totalFrame;
		this.mode = mode;
		this.workingTime = workingTime / 1000;
		this.prevWorkFrame = prevWorkFrame;
	}
	
	/**
	 * 진행율을 계산하여 반환합니다.
	 * @return 진행율
	 */
	public double getPrograssPercentage(){
		return (double)(currentState.currentFrame) / (double)(totalFrame) * 100.0;
	}
	
	/**
	 * 현재 작업 완료까지 남은 예상 소요시간을 반환합니다.
	 * @return 남은 예상 소요시간 (초)
	 */
	public double getEstimatedTime(){
		return (double)(totalFrame - currentState.currentFrame + prevWorkFrame) / (double)(currentState.currentFrame - prevWorkFrame) * (double)(workingTime);
	}
}
