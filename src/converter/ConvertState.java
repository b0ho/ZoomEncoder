package converter;
/**
 * 작업의 현재 상태
 * @author admin
 *
 */
public class ConvertState {
	/** 현재까지 작업 완료된 프레임 */
	public int currentFrame;
	/** 초당 처리량 */
	public double speed;
	
	/**
	 * 생성자
	 * @param currentFrame 현재까지 완료된 프레임
	 * @param speed 작업 속도
	 */
	public ConvertState(int currentFrame, double speed){
		this.currentFrame = currentFrame;
		if(speed == Double.NaN) speed = 0.0;
		this.speed = speed;
	}
	
	/**
	 * 현재 상태를 문자열로 반환합니다.
	 */
	public String toString(){
		return "CurrentConvertFrame=" + currentFrame + ", Speed=" + speed;
	}
}
