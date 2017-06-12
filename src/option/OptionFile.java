package option;
import java.io.*;

/**
 * 옵션을 파일로 만들거나 파일에서 옵션을 읽어오는 클래스입니다.
 * @author admin
 *
 */
public class OptionFile {
	
	/**
	 * 지정된 경로의 옵션 파일을 읽어들입니다.
	 * @return Read 된 옵션
	 */
	public static Option readOptionFile(){
		Option option = new Option();

		//파일 Read를 위한 버퍼 스트림 설정
		BufferedReader br = null;
		
		try{
			br = new BufferedReader(new FileReader("option.ini")); //option.ini 파일을 읽어들입니다.
			String[] optionBuffer = new String[Option.optionCount];
			
			// 정확하게 optionCount 만큼의 반복을 함으로써 옵션 정보가 빠져있는 등의 비정상 적인 상황의 경우
			// 예외를 발생시키므로 대처가 가능합니다.
			for(int i = 0 ; i < Option.optionCount ; i++){
				optionBuffer[i] = br.readLine();
			}
			
			//optionCount 보다 저장되어있는 옵션 정보가 부족한 경우 기본값으로 초기화
			if(optionBuffer[Option.optionCount - 1] == null){
				option = OptionDefault.getDefaultOption();
				writeOptionFile(option);
				br.close();
				return option;
			}
			
			//읽어들인 옵션을 지정합니다.
			try{
				option.setResizeVideoFile(new File(optionBuffer[0]));
				option.setTemporaryOriginalImage(new File(optionBuffer[1]));
				option.setTemporaryResizeImage(new File(optionBuffer[2]));
				option.setCodec(convertCodec(optionBuffer[3]));
				option.setPreset(convertPreset(optionBuffer[4]));
				option.setExtension(convertExtension(optionBuffer[5]));
				option.setKeyFrame(Integer.valueOf(optionBuffer[6]));
				option.setStartTime(Integer.valueOf(optionBuffer[7]));
				option.setWorkTime(Integer.valueOf(optionBuffer[8]));
				option.setThumbnailRange(Integer.valueOf(optionBuffer[9]));
				option.setMode(convertMode(optionBuffer[10]));
				option.setProcess(convertProcess(optionBuffer[11]));
				option.setModel(convertModel(optionBuffer[12]));
				option.setScale(Double.valueOf(optionBuffer[13]));
				option.setNoise(Integer.valueOf(optionBuffer[14]));
				option.setQuality(convertQuality(optionBuffer[15]));
				option.setUsePreview(Boolean.valueOf(optionBuffer[16]));
				option.setUsePoweroff(Boolean.valueOf(optionBuffer[17]));
				option.setRemoveOriginalVideoFile(Boolean.valueOf(optionBuffer[18]));
				option.setRemoveTemporaryOriginalFile(Boolean.valueOf(optionBuffer[19]));
				option.setRemoveTemporaryResizeFile(Boolean.valueOf(optionBuffer[20]));
				option.setTestOption(convertTestOption(optionBuffer[21]));
				option.setTestFrame(Integer.valueOf(optionBuffer[22]));
				option.setUseAllNoiseOption(Boolean.valueOf(optionBuffer[23]));
			}catch(NumberFormatException e){
				//숫자로 저장된 값이 읽히지 않을때 등 옵션 파일에 변형이 가해진 경우 조치사항
				option = OptionDefault.getDefaultOption();
				writeOptionFile(option);
				br.close();
				return option;
			}
			br.close();
		} catch(IOException e){
			// 만약 파일이 없거나 읽어들이는 경과값이 정상적이지 않을경우 기본값으로 지정된 옵션을 생성하고 그 정보를 파일에 기록합니다.
			option = OptionDefault.getDefaultOption();
			writeOptionFile(option);
		}
		return option;
	}
	
	/**
	 * 지정된 경로로 옵션 파일을 기록합니다.
	 * @param option 기록 할 옵션 인스턴스
	 */
	public static void writeOptionFile(Option option){
		BufferedWriter bw = null;
		
		try{
			bw = new BufferedWriter(new FileWriter("option.ini"));
			
			// 각 변수 뒤에 ""을 붙여서 문자열화
			bw.write(option.getResizeVideoFilePath() + "");
			bw.newLine();
			bw.write(option.getTemporaryOriginalImagePath() + "");
			bw.newLine();
			bw.write(option.getTemporaryResizeImagePath() + "");
			bw.newLine();
			bw.write(option.getCodec() + "");
			bw.newLine();
			bw.write(option.getPreset() + "");
			bw.newLine();
			bw.write(option.getExtension() + "");
			bw.newLine();
			bw.write(option.getKeyFrame() + "");
			bw.newLine();
			bw.write(option.getStartTime() + "");
			bw.newLine();
			bw.write(option.getWorkTime() + "");
			bw.newLine();
			bw.write(option.getThumbnailRange() + "");
			bw.newLine();
			bw.write(option.getMode() + "");
			bw.newLine();
			bw.write(option.getProcess() + "");
			bw.newLine();
			bw.write(option.getModel() + "");
			bw.newLine();
			bw.write(option.getScale() + "");
			bw.newLine();
			bw.write(option.getNoise() + "");
			bw.newLine();
			bw.write(option.getQuality() + "");
			bw.newLine();
			bw.write(option.getUsePreview() + "");
			bw.newLine();
			bw.write(option.getUsePoweroff() + "");
			bw.newLine();
			bw.write(option.getRemoveOriginalVideoFile() + "");
			bw.newLine();
			bw.write(option.getRemoveTemporaryOriginalFile() + "");
			bw.newLine();
			bw.write(option.getRemoveTemporaryResizeFile() + "");
			bw.newLine();
			bw.write(option.getTestOption() + "");
			bw.newLine();
			bw.write(option.getTestFrame() + "");
			bw.newLine();
			bw.write(option.getUseAllNoiseOption() + "");
			
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 파일에서 읽어들인 문자열을 코덱 타입으로 변환합니다.
	 * @param arg 읽어들인 코덱과 관련된 문자열입니다.
	 * @return 치환된 코덱 타입입니다.
	 */
	private static Option.Codec convertCodec(String arg){
		if(arg.equals("libx264")){
			return Option.Codec.X264;
		}else if(arg.equals("libxvid")){
			return Option.Codec.XVID;
		}else{
			return Option.Codec.X264;
		}
	}
	
	/**
	 * 파일에서 읽어들인 문자열을 프리셋 타입으로 변환합니다.
	 * @param arg 읽어들인 프리셋과 관련된 문자열입니다.
	 * @return 치환된 프리셋 타입입니다.
	 */
	private static Option.Preset convertPreset(String arg){
		if(arg.equals("slow")){
			return Option.Preset.SLOW;
		}else if(arg.equals("slower")){
			return Option.Preset.SLOWER;
		}else if(arg.equals("veryslow")){
			return Option.Preset.VERY_SLOW;
		}else{
			return Option.Preset.SLOW;
		}
	}

	/**
	 * 파일에서 읽어들인 문자열을 확장자 타입으로 변환합니다.
	 * @param arg 읽어들인 확장자와 관련된 문자열입니다.
	 * @return 치환된 확장자 타입입니다.
	 */
	private static Option.Extension convertExtension(String arg){
		if(arg.equals(".avi")){
			return Option.Extension.AVI;
		}else if(arg.equals(".mp4")){
			return Option.Extension.MP4;
		}else if(arg.equals(".mkv")){
			return Option.Extension.MKV;
		}else{
			return Option.Extension.MP4;
		}
	}

	/**
	 * 파일에서 읽어들인 문자열을 모드 타입으로 변환합니다.
	 * @param arg 읽어들인 모드와 관련된 문자열입니다.
	 * @return 치환된 모드 타입입니다.
	 */
	private static Option.Mode convertMode(String arg){
		if(arg.equals("noise")){
			return Option.Mode.NOISE;
		}else if(arg.equals("scale")){
			return Option.Mode.SCALE;
		}else if(arg.equals("noise_scale")){
			return Option.Mode.NOISE_SCALE;
		}else{
			return Option.Mode.NOISE_SCALE;
		}
	}

	/**
	 * 파일에서 읽어들인 문자열을 프로세스 타입으로 변환합니다.
	 * @param arg 읽어들인 프로세스와 관련된 문자열입니다.
	 * @return 치환된 프로세스 타입입니다.
	 */
	private static Option.Process convertProcess(String arg){
		if(arg.equals("cpu")){
			return Option.Process.CPU;
		}else if(arg.equals("gpu")){
			return Option.Process.GPU;
		}else if(arg.equals("cudnn")){
			return Option.Process.CUDNN;
		}else{
			return Option.Process.CUDNN;
		}
	}

	/**
	 * 파일에서 읽어들인 문자열을 모델 타입으로 변환합니다.
	 * @param arg 읽어들인 모델과 관련된 문자열입니다.
	 * @return 치환된 모델 타입입니다.
	 */
	private static Option.Model convertModel(String arg){
		if(arg.equals("models/upconv_7_anime_style_art_rgb")){
			return Option.Model.ANIME;
		}else if(arg.equals("models/upconv_7_photo")){
			return Option.Model.PHOTO;
		}else{
			return Option.Model.ANIME;
		}
	}

	/**
	 * 파일에서 읽어들인 문자열을 퀄리티 타입으로 변환합니다.
	 * @param arg 읽어들인 퀄리티와 관련된 문자열입니다.
	 * @return 치환된 퀄리티 타입입니다.
	 */
	private static Option.Quality convertQuality(String arg){
		if(arg.equals("0")){
			return Option.Quality.LOSSLESS;
		}else if(arg.equals("15")){
			return Option.Quality.HIGHEST;
		}else if(arg.equals("19")){
			return Option.Quality.HIGH;
		}else if(arg.equals("23")){
			return Option.Quality.MEDIUM;
		}else{
			return Option.Quality.HIGH;
		}
	}

	/**
	 * 파일에서 읽어들인 문자열을 테스트옵션 타입으로 변환합니다.
	 * @param arg 읽어들인 테스트옵션과 관련된 문자열입니다.
	 * @return 치환된 테스트옵션 타입입니다.
	 */
	private static Option.TestOption convertTestOption(String arg){
		if(arg.equals("RANDOM")){
			return Option.TestOption.RANDOM;
		}else if(arg.equals("TIME")){
			return Option.TestOption.TIME;
		}else{
			return Option.TestOption.RANDOM;
		}
	}
}
