package history;
import java.text.SimpleDateFormat;
import java.util.Date;

import converter.Converter;

/**
 * 히스토리 내역의 데이터 파일입니다.
 * @author admin
 *
 */
public class History {
	/**
	 * 지정되는 히스토리 정보의 갯수입니다.<br>
	 * 이 값은 히스토리 파일의 RW 작업에 사용되며 새로운 히스토리 정보의 추가나 기존 정보의 삭제가 있을 시 값의 변동이 필요합니다.
	 */
	public static final int historyCount = 18;
	
	private String videoName;
	private String originalVideoExtension;
	private String resizeVideoExtension;
	private long workStartTime;
	private long workEndTime;
	private long workedTime;
	private Converter.Mode convertMode;
	private long resizeVideoSize;
	private long tempFileSize;
	private String codecOption;
	private double scaleOption;
	private int noiseOption;
	private String modelOption;
	private int qualityOption;
	private String resizeVideoPath;
	private int videoWidth;
	private int videoHeight;
	private int videoBitrate;
	
	
	/**
	 * 변환한 비디오 파일의 이름입니다.
	 * @param videoName 비디오 파일 이름
	 */
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	/**
	 * 비디오 파일의 이름을 반환합니다.
	 * @return 비디오 파일 이름
	 */
	public String getVideoName() {
		return videoName;
	}
	
	/**
	 * 원본 영상의 확장자 입니다.
	 * @param originalVideoExtension 원본 영상의 확장자
	 */
	public void setOriginalVideoExtension(String originalVideoExtension) {
		this.originalVideoExtension = originalVideoExtension;
	}
	
	/**
	 * 원본 영상의 확장자를 반환합니다.
	 * @return 원본 영상의 확장자
	 */
	public String getOriginalVideoExtension() {
		return originalVideoExtension;
	}
	
	/**
	 * 변환 영상의 확장자 입니다.
	 * @param resizeVideoExtension 변환 영상의 확장자
	 */
	public void setResizeVideoExtension(String resizeVideoExtension) {
		this.resizeVideoExtension = resizeVideoExtension;
	}
	
	/**
	 * 변환 영상의 확장자를 반환합니다.
	 * @return 변환 영상의 확장자
	 */
	public String getResizeVideoExtension() {
		return resizeVideoExtension;
	}
	
	/**
	 * 작업의 시작 시간을 지정합니다.
	 * @param workStartTime 작업의 시작 시간
	 */
	public void setWorkStartTime(long workStartTime) {
		this.workStartTime = workStartTime;
	}
	
	/**
	 * 작업의 시작 시간을 반환합니다.
	 * @return 작업의 시작 시간
	 */
	public long getWorkStartTime() {
		return workStartTime;
	}
	
	/**
	 * 작업의 시작시간을 yyyy-MM-dd HH:mm:ss 형식으로 반환합니다.
	 * @return yyyy-MM-dd HH:mm:ss 형식의 작업 시작 시간
	 */
	public String getFormattedWorkStartTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date(getWorkStartTime() * 1000);
		return format.format(d);
	}
	
	/**
	 * 작업의 종료 시간을 지정합니다.
	 * @param workEndTime 작업의 종료 시간
	 */
	public void setWorkEndTime(long workEndTime) {
		this.workEndTime = workEndTime;
	}
	
	/**
	 * 작업의 종료 시간을 반환합니다. 0이 반환되는 경우 작업이 종료되지 않은 경우입니다.
	 * @return 작업의 종료 시간
	 */
	public long getWorkEndTime() {
		return workEndTime;
	}
	
	/**
	 * 작업의 종료시간을 yyyy-MM-dd HH:mm:ss 형식으로 반환합니다.
	 * @return  yyyy-MM-dd HH:mm:ss 형식의 작업 종료 시간
	 */
	public String getFormattedWorkEndTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date(getWorkEndTime() * 1000);
		return format.format(d);
	}
	
	/**
	 * 작업 소요 시간을 지정합니다.
	 * @param workedTime 작업 소요 시간
	 */
	public void setWorkedTime(long workedTime) {
		this.workedTime = workedTime;
	}
	
	/**
	 * 작업 소요 시간을 반환합니다.
	 * @return 작업 소요 시간
	 */
	public long getWorkedTime() {
		return workedTime;
	}
	
	/**
	 * 작업 소요 시간을 일 시 분 초 형식으로 반환합니다.
	 * @return 일 시 분 초 형식의 작업 소요 시간
	 */
	public String getFormattedWorkedTime(){
		long time = getWorkedTime();
		long sec = time % 60;
		long min = time / 60 % 60;
		long hour = time / 60 / 60 % 24;
		long day = time / 60 / 60 / 24;
		return day + "일 " + hour + "시간 " + min + "분 " + sec + "초";
	}
	
	/**
	 * 실 변환/테스트 변환 여부를 지정합니다.
	 * @param convertMode 변환 모드
	 */
	public void setConvertMode(Converter.Mode convertMode) {
		this.convertMode = convertMode;
	}
	
	/**
	 * 변환 모드를 반환합니다. null이 반환되는 경우 에러입니다.
	 * @return 변환 모드
	 */
	public String getConvertMode() {
		if(convertMode == null) return "null";
		return convertMode.toString();
	}
	
	/**
	 * 변환 후 비디오 파일의 용량을 지정합니다.
	 * @param resizeVideoSize 변환 후 비디오 파일 용량
	 */
	public void setResizeVideoSize(long resizeVideoSize) {
		this.resizeVideoSize = resizeVideoSize;
	}
	
	/**
	 * 변환 후 비디오 파일의 용량을 반환합니다.
	 * @return 변환 후 비디오 파일 용량
	 */
	public long getResizeVideoSize() {
		return resizeVideoSize;
	}
	
	/**
	 * 임시 데이터의 총 룡량을 지정합니다.
	 * @param tempFileSize 임시 데이터의 총 용량
	 */
	public void setTempFileSize(long tempFileSize) {
		this.tempFileSize = tempFileSize;
	}
	
	/**
	 * 임시 데이터의 총 용량을 반환합니다.
	 * @return 임시 데이터의 총 용량
	 */
	public long getTempFileSize() {
		return tempFileSize;
	}

	/**
	 * 코덱 옵션을 지정합니다.
	 * @param codecOption 코덱 옵션
	 */
	public void setCodecOption(String codecOption) {
		this.codecOption = codecOption;
	}
	
	/**
	 * 코덱 옵션을 반환합니다. null이 반환되는 경우 테스트 변환입니다.
	 * @return 코덱 옵션
	 */
	public String getCodecOption() {
		if(codecOption == null) return "null";
		return codecOption.toString();
	}

	/**
	 * 확대 옵션을 지정합니다.
	 * @param scaleOption 확대 옵션
	 */
	public void setScaleOption(double scaleOption) {
		this.scaleOption = scaleOption;
	}
	
	/**
	 * 확대 옵션을 반환합니다.
	 * @return 확대 옵션
	 */
	public double getScaleOption() {
		return scaleOption;
	}

	/**
	 * 노이즈 옵션을 지정합니다.
	 * @param noiseOption 노이즈 옵션
	 */
	public void setNoiseOption(int noiseOption) {
		this.noiseOption = noiseOption;
	}
	
	/**
	 * 노이즈 옵션을 반환합니다.
	 * @return 노이즈 옵션
	 */
	public int getNoiseOption() {
		return noiseOption;
	}

	/**
	 * 변환 모델 옵션을 지정합니다.
	 * @param modelOption 변환 모델 옵션
	 */
	public void setModelOption(String modelOption) {
		this.modelOption = modelOption;
	}
	
	/**
	 * 변환 모델 옵션을 반환합니다. null이 반환되는 경우 테스트 변환입니다.
	 * @return 변환 모델 옵션
	 */
	public String getModelOption() {
		if(modelOption == null) return "null";
		return modelOption.toString();
	}

	/**
	 * 영상 품질 옵션을 지정합니다.
	 * @param qualityOption 영상 품질 옵션
	 */
	public void setQualityOption(int qualityOption) {
		this.qualityOption = qualityOption;
	}
	
	/**
	 * 영상 품질 옵션을 반환합니다.
	 * @return
	 */
	public int getQualityOption() {
		return this.qualityOption;
	}
	
	/**
	 * 변환된 비디오의 저장 경로를 지정합니다.
	 * @param resizeVideoPath 비디오 저장 경로
	 */
	public void setResizeVideoPath(String resizeVideoPath) {
		this.resizeVideoPath = resizeVideoPath;
	}
	/**
	 * 변환된 비디오의 저장 경로를 반환합니다.
	 * @return 비디오 저장 경로
	 */
	public String getResizeVideoPath() {
		return resizeVideoPath;
	}

	/**
	 * 변환된 비디오 가로 크기를 지정합니다.
	 * @param videoWidth 비디오 가로 크기
	 */
	public void setVideoWidth(int videoWidth) {
		this.videoWidth = videoWidth;
	}
	/**
	 * 변환된 비디오 가로 크기를 반환합니다. 0이 반환되면 테스트 모드입니다.
	 * @return 비디오 가로 크기
	 */
	public int getVideoWidth() {
		return videoWidth;
	}

	/**
	 * 변환된 비디오 세로 크기를 지정합니다.
	 * @param videoHeight 비디오 세로 크기
	 */
	public void setVideoHeight(int videoHeight) {
		this.videoHeight = videoHeight;
	}
	
	/**
	 * 변환된 비디오 세로 크기를 반환합니다. 0이 반환되면 테스트 모드입니다.
	 * @return 비디오 세로 크기
	 */
	public int getVideoHeight() {
		return videoHeight;
	}

	/**
	 * 변환된 영상의 비트레이트를 지정합니다.
	 * @param videoBitrate 비트레이트
	 */
	public void setVideoBitrate(int videoBitrate) {
		this.videoBitrate = videoBitrate;
	}
	
	/**
	 * 변환된 영상의 비트레이트를 반환합니다. 0이 반환되면 테스트 모드입니다.
	 * @return 비트레이트
	 */
	public int getVideoBitrate() {
		return videoBitrate;
	}
}
