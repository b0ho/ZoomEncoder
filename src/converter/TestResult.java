package converter;

/**
 * 테스트 결과 데이터를 저장하는 클래스 입니다.
 * @author admin
 *
 */
public class TestResult {
	private double divAvgSpeed;
	private double zoomAvgSpeed;
	private int total;
	private long time = 0;
	private double n_psnr[] = new double[5];
	private double z_psnr[] = new double[5];
	
	/**
	 * 테스트 데이터를 생성하는 생성자 입니다.
	 * @param divSpeed 분할작업의 평균 속도
	 * @param zoomSpeed 확대 작업의 평균 속도
	 * @param totalFrame 테스트에 사용한 프레임 수
	 */
	public TestResult(double divSpeed, double zoomSpeed, int totalFrame){
		divAvgSpeed = divSpeed;
		zoomAvgSpeed = zoomSpeed;
		total = totalFrame;
		time = (long) (total / divAvgSpeed);
		time = time + (long)(total / zoomAvgSpeed);
	}
	
	public TestResult(double divSpeed, double zoomSpeed, int totalFrame, double[] n_psnr, double[] z_psnr){
		divAvgSpeed = divSpeed;
		zoomAvgSpeed = zoomSpeed;
		total = totalFrame;
		time = (long) (total / divAvgSpeed);
		time = time + (long)(total / zoomAvgSpeed);
		this.n_psnr = n_psnr;
		this.z_psnr = z_psnr;
	}
	
	
	/**
	 * 5개의 샘플에 대한 psnr 값을 리턴합니다.
	 * @return PSNR 샘플 값
	 */
	
	public double[] getN_Psnr(){
		return n_psnr;
	}
	
	/**
	 * 5개의 샘플에 대한 psnr 값을 리턴합니다.
	 * @return PSNR 샘플 값
	 */
	
	public double[] getZ_Psnr(){
		return z_psnr;
	}
	
	/**
	 * 초 단위로 변환 예상 시간을 반환합니다.
	 * @return 변환 예상시간 (초단위)
	 */
	public long getEstimatedTime(){
		return time;
	}
	
	/**
	 * 일 시 분 초 로 구성된 예상 시간을 반환합니다.
	 * @return 변환 예상시간 (일 시 분 초)
	 */
	public String getEstimatedTimeFormat(){
		long sec = time % 60;
		long min = time / 60 % 60;
		long hour = time / 60 / 60 % 24;
		long day = time / 60 / 60 / 24;
		return "예상 작업 시간 : " + day + "일 " + hour + "시간 " + min + "분 " + sec + "초";
	}
}
