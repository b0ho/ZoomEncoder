package converter;
import java.util.ArrayList;

/**
 * <b>FFMpeg</b>의 커멘드를 생성하는 클래스입니다.
 * @author admin
 */
public class FFMpeg {
	
	private static String ffmpegPath = "ffmpeg.exe";
	private static String ffprobePath = "ffprobe.exe";
	
	/**
	 * <b>[FFProbe Path] -hide_banner -loglevel -8 -show_streams -select_streams v:0 [VideoFilePath]</b><p>
	 * 형식의 질의를 반환합니다. 
	 * @return 질의 문자열 리스트
	 */
	public static ArrayList<String> videoInfoCommand(String videoPath){
		ArrayList<String> str = new ArrayList<String>();
		str.add(ffprobePath);
		str.add("-hide_banner");
		str.add("-loglevel");
		str.add("-8");
		str.add("-show_streams");
		str.add("-select_streams");
		str.add("v:0");
		str.add(videoPath);
		return str;
	}
	
	/**
	 * <b>[FFMpeg Path] -hide_banner -i [VideoFilePath] -q:v [Quality] [OutputImagePath]</b><p>
	 * 형식의 질의를 반환합니다. 
	 * @return 질의 문자열 리스트
	 */
	public static ArrayList<String> imageOutputCommand(String originalVideoPath, String originalImagePath){
		ArrayList<String> str = new ArrayList<String>();
		str.add(ffmpegPath);
		str.add("-hide_banner");
		str.add("-i");
		str.add(originalVideoPath);
		str.add("-q:v");
		str.add("1");
		str.add(originalImagePath + "/img%09d.jpg");
		return str;
	}

	/**
	 * <b>[FFMpeg Path] -hide_banner -ss [StartTime] -i [VideoFilePath] -q:v [Quality] -t [WorkTime] [OutputImagePath]</b><p>
	 * 형식의 질의를 반환합니다.<b> [Debug용]</b>
	 * @return 질의 문자열 리스트
	 */
	public static ArrayList<String> imageOutputCommand(String originalVideoPath, String originalImagePath, int startTime, int workTime){
		ArrayList<String> str = new ArrayList<String>();
		str.add(ffmpegPath);
		str.add("-hide_banner");
		str.add("-ss");
		str.add(String.valueOf(startTime));
		str.add("-i");
		str.add(originalVideoPath);
		str.add("-q:v");
		str.add("1");
		str.add("-t");
		str.add(String.valueOf(workTime));
		str.add(originalImagePath + "/img%09d.jpg");
		return str;
	}

	/**
	 * <b>[FFMpeg Path] -hide_banner -i [VideoFilePath] -q:v [Quality] -vf fps=[framerate/thumbnailRange] [OutputImagePath]</b><p>
	 * 형식의 질의를 반환합니다. 
	 * @return 질의 문자열 리스트
	 */
	public static ArrayList<String> randomThumbnailTestCommand(String originalVideoPath, String originalImagePath, double framerate, int thumbnailRange){
		ArrayList<String> str = new ArrayList<String>();
		str.add(ffmpegPath);
		str.add("-hide_banner");
		str.add("-i");
		str.add(originalVideoPath);
		str.add("-q:v");
		str.add("1");
		str.add("-vf");
		str.add("fps="+String.valueOf((double)(framerate/thumbnailRange)));
		str.add(originalImagePath + "/img%09d.jpg");
		return str;
	}

	/**
	 * <b>[FFMpeg Path] -hide_banner -ss [StartTime] -i [VideoFilePath] -q:v [Quality] -vframes [framerate*WorkTime] [OutputImagePath]</b><p>
	 * 형식의 질의를 반환합니다. 
	 * @return 질의 문자열 리스트
	 */
	public static ArrayList<String> timelineTestCommand(String originalVideoPath, String originalImagePath, int startTime, int frame){
		ArrayList<String> str = new ArrayList<String>();
		str.add(ffmpegPath);
		str.add("-hide_banner");
		str.add("-ss");
		str.add(String.valueOf(startTime));
		str.add("-i");
		str.add(originalVideoPath);
		str.add("-q:v");
		str.add("1");
		str.add("-vframes");
		str.add(String.valueOf(frame));
		str.add(originalImagePath + "/img%09d.jpg");
		return str;
	}

	/**
	 * <b>[FFMpeg Path] -hide_banner -framerate [framerate] -i [ResizeImagePath] -i [OriginalVideoPath] -c:v [codec] -preset [preset] -g [keyFrame] -crf [crf] -pix_fmt yuv420p -c:a copy [ResizeVideoPath]</b><p>
	 * 형식의 질의를 반환합니다. 
	 * @return 질의 문자열 리스트
	 */
	public static ArrayList<String> imageMergeCommand(String originalVideoPath, String resizeImagePath, String resizeVideoPath, double framerate, int keyFrame, String codec, String preset, int crf){
		ArrayList<String> str = new ArrayList<String>();
		str.add(ffmpegPath);
		str.add("-hide_banner");
		str.add("-framerate");
		str.add(String.valueOf(framerate));
		str.add("-i");
		str.add(resizeImagePath + "/img%09d.png");
		str.add("-i");
		str.add(originalVideoPath);
		str.add("-c:v");
		str.add(codec);
		str.add("-preset");
		str.add(preset);
		str.add("-g");
		str.add(String.valueOf((int)(framerate * keyFrame)));
		str.add("-crf");
		str.add(String.valueOf(crf));
		str.add("-pix_fmt");
		str.add("yuv420p");
		str.add("-c:a");
		str.add("copy");
		str.add("-n");
		str.add(resizeVideoPath);
		return str;
	}
	
	//PSNR 측정을 위해 원본 영상을 사이즈 다운한 이미지를 생성
	/**
	 * <b>[FFMpeg Path] -hide_banner -i [OutputImagePath] -vf scale=width:height [TestOutputImagePath]</b><p>
	 * 형식의 질의를 반환합니다. 
	 * @return 질의 문자열 리스트
	 */
	public static ArrayList<String> imageSizeDownCommand(String originalImagePath, String videoName, int resizeWidth, int resizeHeight){
		ArrayList<String> str = new ArrayList<String>();
		str.add(ffmpegPath);
		str.add("-hide_banner");
		str.add("-i");
		str.add(originalImagePath + videoName + "/img%09d.jpg");
		str.add("-vf");
		str.add("scale=" + String.valueOf(resizeWidth) + ":" + String.valueOf(resizeHeight));
		str.add(originalImagePath + "/n_down/" + videoName + "/img%09d.jpg");
		return str;
	}
	
	//PSNR 측정을 위해 원본 영상 사이즈로 확대한 이미지를 생성
	/**
	 * <b>[FFMpeg Path] -hide_banner -i [OutputImagePath] -vf scale=width:height [TestOutputImagePath]</b><p>
	 * 형식의 질의를 반환합니다. 
	 * @return 질의 문자열 리스트
	 */
	public static ArrayList<String> imageSizeUpCommand(String originalImagePath, String resizeImagePath, String videoName, int resizeWidth, int resizeHeight){
		ArrayList<String> str = new ArrayList<String>();
		str.add(ffmpegPath);
		str.add("-hide_banner");
		str.add("-i");
		str.add(originalImagePath + "/n_down/" + videoName + "/img%09d.jpg");
		str.add("-vf");
		str.add("scale=" + String.valueOf(resizeWidth) + ":" + String.valueOf(resizeHeight));
		str.add(resizeImagePath + "n_up/" + videoName + "/img%09d.jpg");
		return str;
	}
}
