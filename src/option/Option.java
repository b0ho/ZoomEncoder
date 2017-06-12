package option;
import java.io.File;
import java.io.IOException;

/**
 * 시스템에 사용하는 다양한 옵션을 지정하는 클래스 입니다.
 * @author admin
 *
 */
public class Option {

	/** Mode : <b>Noise | Scale | Noise_Scale</b> */
	public static enum Mode{ NOISE, SCALE, NOISE_SCALE }
	/** Process : <b>CPU | GPU | CUDNN</b> */
	public static enum Process{ CPU, GPU, CUDNN }
	/** Model : <b>Photo | Anime</b> */
	public static enum Model{ PHOTO, ANIME }
	/** Codec : <b>x264 | xvid</b> */
	public static enum Codec{ X264, XVID }
	/** Preset : <b>Slow | Slower | Very_Slow</b> */
	public static enum Preset{ SLOW, SLOWER, VERY_SLOW }
	/** Extension : <b>avi | mp4 | mkv</b> */
	public static enum Extension{ AVI, MP4, MKV }
	/** Quality : <b>Lossless | highest | High | Medium</b> */
	public static enum Quality{ LOSSLESS, HIGHEST, HIGH, MEDIUM }
	/** Test : <b>Random | Time</b> */
	public static enum TestOption{ RANDOM, TIME }
	
	/**
	 * 지정되는 옵션의 갯수입니다.<br>
	 * 이 값은 옵션 파일의 RW 작업에 사용되며 새로운 옵션의 추가나 기존 옵션의 삭제가 있을 시 값의 변동이 필요합니다.
	 */
	public static final int optionCount = 24;
	
	private File resizeVideoFilePath;
	private File temporaryOriginalImagePath;
	private File temporaryResizeImagePath;
	private Codec codec = Codec.X264;
	private Preset preset = Preset.SLOW;
	private Extension ext = Extension.MP4;
	private int keyFrame = 1;
	private int startTime = 0;
	private int workTime = 0;
	private Process process = Process.CUDNN;
	private Mode mode = Mode.NOISE_SCALE;
	private double scale = 2.0;
	private int noise = 3;
	private Model model = Model.PHOTO;
	private Quality quality = Quality.HIGH;
	
	private boolean usePreview = true;
	private boolean usePoweroff = false;
	private boolean removeOriginalVideoFile = false;
	private boolean removeTemporaryOriginalFile = false;
	private boolean removeTemporaryResizeFile = false;
	private TestOption testOption = TestOption.RANDOM;
	private int thumbnailRange;
	private int testFrame = 60;
	private boolean testAllNoiseOption = false;
	
	
	/**
	 * 변환된 비디오 파일이 저장될 폴더를 지정합니다.
	 * @param f 비디오 파일이 저장될 폴더
	 */
	public void setResizeVideoFile(File f){
		if(f.isDirectory()){
			resizeVideoFilePath = f;
		}else{
			if(f.mkdirs()){
				resizeVideoFilePath = f;
			}
		}
	}
	
	/**
	 * 변환될 비디오 파일이 저장되는 폴더를 반환합니다.
	 * @return 변환된 비디오 파일이 저장되는 폴더
	 */
	public File getResizeVideoFile(){
		return resizeVideoFilePath;
	}
	
	/**
	 * 변환될 비디오 파일이 저장되는 폴더의 경로를 반환합니다.
	 * @return 변환된 비디오 파일이 저장되는 폴더의 경로
	 */
	public String getResizeVideoFilePath(){
		try {
			return resizeVideoFilePath.getCanonicalPath();
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * 변환 전 임시파일이 저장되는 폴더를 지정합니다.
	 * @param f 변환 전 임시파일이 저장될 폴더
	 */
	public void setTemporaryOriginalImage(File f){
		if(f.isDirectory()){
			temporaryOriginalImagePath = f;
		}else{
			if(f.mkdirs()){
				temporaryOriginalImagePath = f;
			}
		}
	}
	
	/**
	 * 변환 전 임시파일이 저장되는 폴더를 반환합니다.
	 * @return 변환 전 임시파일이 저장되는 폴더
	 */
	public File getTemporaryOriginalImage(){
		return temporaryOriginalImagePath;
	}
	
	/**
	 * 변환 전 임시파일이 저장되는 폴더의 경로를 반환합니다.
	 * @return 변환 전 임시파일이 저장되는 폴더의 경로
	 */
	public String getTemporaryOriginalImagePath(){
		try {
			return temporaryOriginalImagePath.getCanonicalPath();
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * 변환 후 임시파일이 저장되는 폴더를 지정합니다.
	 * @param f 변환 후 임시파일이 저장되는 폴더
	 */
	public void setTemporaryResizeImage(File f){
		if(f.isDirectory()){
			temporaryResizeImagePath = f;
		}else{
			if(f.mkdirs()){
				temporaryResizeImagePath = f;
			}
		}
	}
	
	/**
	 * 변환 후 임시파일이 저장되는 폴더를 반환합니다.
	 * @return 변환 후 임시파일이 저장되는 폴더
	 */
	public File getTemporaryResizeImage(){
		return temporaryResizeImagePath;
	}
	
	/**
	 * 변환 후 임시파일이 저장되는 폴더의 경로를 반환합니다.
	 * @return 변환 후 임시파일이 저장되는 폴더의 경로
	 */
	public String getTemporaryResizeImagePath(){
		try {
			return temporaryResizeImagePath.getCanonicalPath();
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * 영상의 코덱을 설정합니다.
	 * @param c -c:v x264, xvid 중 선택
	 */
	public void setCodec(Codec c){
		codec = c;
	}
	
	/**
	 * 코덱 설정 값을 반환합니다.
	 * @return 코덱 설정 값의 실제 입력값
	 */
	public String getCodec(){
		switch(codec){
		case X264:
			return "libx264";
		case XVID:
			return "libxvid";
		default:
			return "libx264";
		}
	}
	
	/**
	 * 영상 변환의 프리셋을 설정합니다.
	 * @param p -preset slow, slower, veryslow 중 선택
	 */
	public void setPreset(Preset p){
		preset = p;
	}
	
	/**
	 * 프리셋 설정 값을 반환합니다.
	 * @return 프리셋 설정 값의 실제 입력값
	 */
	public String getPreset(){
		switch(preset){
		case SLOW:
			return "slow";
		case SLOWER:
			return "slower";
		case VERY_SLOW:
			return "veryslow";
		default:
			return "slow";
		}
	}
	
	/**
	 * 변환될 영상의 확장자를 지정합니다.
	 * @param extension 확장자 mp4, avi, mkv 중 선택
	 */
	public void setExtension(Extension extension){
		ext = extension;
	}
	
	/**
	 * 확장자 값을 반환합니다.
	 * @return .avi .mp4 .mkv 형태로 반환됩니다.
	 */
	public String getExtension(){
		switch(ext){
		case AVI:
			return ".avi";
		case MP4:
			return ".mp4";
		case MKV:
			return ".mkv";
		default:
			return ".mp4";
		}
	}
	
	/**
	 * 키프레임 삽입 간격을 지정합니다. 낮을수록 많은 키프레임이 삽입됨. 초단위로 입력합니다.
	 * @param g -g 옵션
	 */
	public void setKeyFrame(int g){
		//1초당 1키프레임 ~ 30초당 1키프레임
		if(g < 1) g = 1;
		if(g > 30) g = 30;
		keyFrame = Math.abs(g);
	}
	
	/**
	 * 키프레임 간격을 반환합니다.
	 * @return 키 프레임 간격
	 */
	public int getKeyFrame(){
		return keyFrame;
	}
	
	/**
	 * 영상 처리의 시작 구간을 지정할 수 있습니다. (초단위)
	 * @param ss -ss 옵션 0~동영상 길이까지
	 */
	public void setStartTime(int ss){
		//음수값 사용 불가
		startTime = Math.abs(ss);
	}
	
	/**
	 * 시작구간을 반환합니다.
	 * @return 시작구간(초)
	 */
	public int getStartTime(){
		return startTime;
	}
	
	/**
	 * 영상 길이를 지정합니다.
	 * @param t -t 옵션
	 */
	public void setWorkTime(int t){
		//음수값 사용 불가
		workTime = Math.abs(t);
	}
	
	/**
	 * 영상 길이를 반환합니다.
	 * @return 영상길이(초)
	 */
	public int getWorkTime(){
		return workTime;
	}

	/**
	 * 동영상 섬네일을 추출하는 프레임 단위의 간격을 지정합니다.
	 * @param thumb -vf fps=n 옵션. 100인경우 100프레임당 한장. (framerate / range)
	 */
	public void setThumbnailRange(int thumb){
		//2프레임당 1장 ~ 10000프레임당 1장.
		if(thumb < 1) thumb = 1;
		if(thumb > 10000) thumb = 10000;
		thumbnailRange = thumb;
	}
	
	/**
	 * 섬네일 간격 값을 반환합니다.
	 * @return 섬네일 간격
	 */
	public int getThumbnailRange(){
		return thumbnailRange;
	}
	

	/**
	 * 변환 모드를 설정합니다.
	 * @param m -m 노이즈만 제거(NOISE), 확대만 (SCALE), 둘 다 진행(NOISE_SCALE)
	 */
	public void setMode(Option.Mode m){
		mode = m;
	}
	
	/**
	 * 변환 모드 값을 반환합니다.
	 * @return 변환 모드 값의 실제 입력값
	 */
	public String getMode(){
		switch(mode){
		case NOISE:
			return "noise";
		case SCALE:
			return "scale";
		case NOISE_SCALE:
			return "noise_scale";
		default:
			return "noise_scale";
		}
	}
	
	/**
	 * 연산 프로세스를 선택합니다.
	 * @param p -p CPU연산(CPU), CUDA연산(GPU), cuDNN연산(CUDNN)
	 */
	public void setProcess(Option.Process p){
		process = p;
	}
	
	/**
	 * 연산 프로세스를 반환합니다.
	 * @return 연산 프로세스의 실제 입렧값
	 */
	public String getProcess(){
		switch(process){
		case CPU:
			return "cpu";
		case GPU:
			return "gpu";
		case CUDNN:
			return "cudnn";
		default:
			return "cudnn";
		}
	}
	
	/**
	 * 변환 모델 종류를 선택합니다.
	 * @param m --model_dir 2차원 이미지 특화(ANIME), 실 영상(PHOTO)
	 */
	public void setModel(Option.Model m){
		model = m;
	}
	
	/**
	 * 변환 모델 종류를 반환합니다.
	 * @return 모델
	 */
	public String getModel(){
		switch(model){
		case ANIME:
			return "waifu2x/models/upconv_7_anime_style_art_rgb";
		case PHOTO:
			return "waifu2x/models/upconv_7_photo";
		default:
			return "waifu2x/models/upconv_7_anime_style_art_rgb";
		}
	}
	
	/**
	 * 확대 배율을 설정합니다.
	 * @param s -s 0.5배 ~ 16배. 0.1배 단위
	 */
	public void setScale(double s){
		if(s < 0.5) s = 0.5;
		if(s > 16.0) s = 16.0;
		scale = Math.abs(Math.round(s * 1000) / 1000.0);
	}
	
	/**
	 * 확대 배율을 반환합니다.
	 * @return 확대 배율
	 */
	public double getScale(){
		return scale;
	}
	
	/**
	 * 노이즈 제거 정도를 설정합니다.
	 * @param n -n 0~3사이중 선택</br>
	 * 0 : 노이즈 제거 없음</br>
	 * 1 : 노이즈 제거 최소 (애니메이션 권장)</br>
	 * 2 : 노이즈 제거 보통</br>
	 * 3 : 노이즈 제거 최대 (실영상 원장)</br>
	 */
	public void setNoise(int n){
		if(n < 0 || n > 3) n = 0;
		noise = Math.abs(n);
	}
	
	/**
	 * 노이즈 제거 정도를 반환합니다.
	 * @return 노이즈 제거 정도
	 */
	public int getNoise(){
		return noise;
	}
	
	/**
	 * 가변 비트레이트 비디오 품질 지정
	 * @param crf -crf 옵션
	 */
	public void setQuality(Quality q){
		quality = q;
	}
	
	/**
	 * CRF 값을 반환합니다.
	 * @return CRF값
	 */
	public int getQuality(){
		switch(quality){
		case LOSSLESS:
			return 0;
		case HIGHEST:
			return 15;
		case HIGH:
			return 19;
		case MEDIUM:
			return 23;
		default:
			return 19;
		}
	}
	
	/**
	 * 영상 변환 작업 중 화면 미리보기를 할 것인지를 선택합니다.
	 * @param op 미리보기 설정 값. true면 미리보기 표시
	 */
	public void setUsePreview(boolean op){
		usePreview = op;
	}
	
	/**
	 * 미리보기 설정값을 반환합니다.
	 * @return 미리보기 설정 값
	 */
	public boolean getUsePreview(){
		return usePreview;
	}
	
	/**
	 * 변환 작업 완료 후 컴퓨터 종료를 할 것인지를 선택합니다.
	 * @param op 컴퓨터 종료 설정 값. true이면 완료 후 컴퓨터 종료
	 */
	public void setUsePoweroff(boolean op){
		usePoweroff = op;
	}
	
	/**
	 * 컴퓨터 종료 설정 값을 반환합니다.
	 * @return 컴퓨터 종료 설정 값
	 */
	public boolean getUsePoweroff(){
		return usePoweroff;
	}
	
	/**
	 * 변환 작업 완료 후 원본 영상 파일을 삭제할 것인지를 선택합니다.
	 * @param op 원본 영상 파일 삭제 설정 값. true이면 완료 후 원본 영상 삭제
	 */
	public void setRemoveOriginalVideoFile(boolean op){
		removeOriginalVideoFile = op;
	}
	
	/**
	 * 원본 영상 파일 삭제 설정 값을 반환합니다.
	 * @return 원본 영상 파일 삭제 설정 값
	 */
	public boolean getRemoveOriginalVideoFile(){
		return removeOriginalVideoFile;
	}
	
	/**
	 * 변환 작업 완료 후 개선 전 임시 이미지 파일을 삭제할 것인지를 선택합니다.
	 * @param op 개선 전 임시 이미지 파일 삭제 설정 값. true이면 완료 후 개선 전 임시 이미지 파일 삭제
	 */
	public void setRemoveTemporaryOriginalFile(boolean op){
		removeTemporaryOriginalFile = op;
	}
	
	/**
	 * 개선 전 임시 이미지 파일 삭제 설정 값을 반환합니다.
	 * @return 개선 전 임시 이미지 파일 삭제 설정 값
	 */
	public boolean getRemoveTemporaryOriginalFile(){
		return removeTemporaryOriginalFile;
	}

	/**
	 * 변환 작업 완료 후 개선 후 임시 이미지 파일을 삭제할 것인지를 선택합니다.
	 * @param op 개선 후 임시 이미지 파일 삭제 설정 값. true이면 완료 후 개선 후 임시 이미지 파일 삭제
	 */
	public void setRemoveTemporaryResizeFile(boolean op){
		removeTemporaryResizeFile = op;
	}
	
	/**
	 * 개선 후 임시 이미지 파일 삭제 설정 값을 반환합니다.
	 * @return 개선 후 임시 이미지 파일 삭제 설정 값
	 */
	public boolean getRemoveTemporaryResizeFile(){
		return removeTemporaryResizeFile;
	}

	/**
	 * 테스트 옵션을 지정합니다.
	 * @param op 특정 시간대 영상 테스트, 혹은 랜덤 프레임 테스트를 선택할 수 있습니다.
	 */
	public void setTestOption(TestOption op){
		testOption = op;
	}
	
	/**
	 * 테스트 옵션을 반환합니다.
	 * @return 테스트 옵션
	 */
	public TestOption getTestOption(){
		return testOption;
	}
	
	/**
	 * 테스트 할 프레임 수를 지정합니다. 
	 * @param op 테스트 할 프레임 수 15~120 사이 값
	 */
	public void setTestFrame(int op){
		if(op == 5){
			testFrame = 5;
			return;
		}
		if(op < 15) op = 15;
		if(op > 120) op = 120;
		testFrame = Math.abs(op);
	}
	
	/**
	 * 테스트 할 프레임 수를 반환합니다.
	 * @return 테스트할 프레임 수
	 */
	public int getTestFrame(){
		return testFrame;
	}
	
	/**
	 * 테스트 시 0~3단계의 노이즈 옵션에 대해 모두 적용하도록 테스트를 진행합니다.
	 * @param op 노이즈 옵션 적용. true면 0~3단계 까지 총 4회 테스트 진행. false면 선택된 옵션에 대해서만 진행
	 */
	public void setUseAllNoiseOption(boolean op){
		testAllNoiseOption = op;
	}
	
	/**
	 * 테스트 시 노이즈 옵션의 설정 값을 반환합니다.
	 * @return 노이즈 옵션 적용 값
	 */
	public boolean getUseAllNoiseOption(){
		return testAllNoiseOption;
	}
}
