package loader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import converter.FFMpeg;

/**
 * 영상 파일을 로드하는 클래스 입니다.
 * @author admin
 *
 */
public class VideoLoader extends Thread{
	
	/** 로드 가능한 확장자 */
	private String[] ext = {".avi",".mp4",".mkv"};
	/** 로드 시도시 영상 파일이 아니거나 영상 로드에 실패한 횟수 */
	private int failCount = 0;
	/** 영상 목록 */
	private VideoList videoArray = new VideoList();
	
	/**
	 * 영상 파일의 로드를 진행하는 메서드
	 * @param sources 로드를 시도할 파일 목록
	 */
	public int selectVideo(File[] sources){
		//로드 시 마다 로드 실패 횟수를 체크한다.
		failCount = 0;
		//foreach 로 반복함.
		for (File file : sources) {
			if(file.isFile()){ // 파일인경우
				if(checkExtension(file)){ //확장자를 검사한다.
					Video v = new Video(file);
					Process process = null; // 프로세스 초기화
					try {
						process = new ProcessBuilder(FFMpeg.videoInfoCommand(v.getVideoFilePath())).start(); //영상 정보 가져오기 프로세스 진행
					} catch (IOException e1) {
						//프로세스 로드에 실패했을 때 발생 가능한 예외
						//아직 처리가 구현되지 않음
						e1.printStackTrace();
					}
					String str = null;
					BufferedReader stdOut = new BufferedReader( new InputStreamReader(process.getInputStream()) ); //영상 정보를 스트림으로 읽는다.
					try {
						while( (str = stdOut.readLine()) != null ) {
							if(str.startsWith("codec_name")){
								str = str.substring(str.lastIndexOf('=') + 1);
								v.setCodec(str);
							}else if(str.startsWith("width")){
								str = str.substring(str.lastIndexOf('=') + 1);
								v.setWidth(Integer.valueOf(str));
							}else if(str.startsWith("height")){
								str = str.substring(str.lastIndexOf('=') + 1);
								v.setHeight(Integer.valueOf(str));
							}else if(str.startsWith("bit_rate")){
								str = str.substring(str.lastIndexOf('=') + 1);
								if(!str.equals("N/A")){
									v.setBitrate(Integer.valueOf(str)/1024);
								}
							}else if(str.startsWith("avg_frame_rate")){
								str = str.substring(str.lastIndexOf('=') + 1);
								if(str.matches(".*/.*")){ //xxx/yy 형식의 프레임인경우 별도로 계산하여 저장
									double f1 = Double.valueOf(str.substring(0, str.lastIndexOf('/')));
									double f2 = Double.valueOf(str.substring(str.lastIndexOf('/') + 1));
									v.setFramerate(f1 / f2);
								}else{
									v.setFramerate(Double.valueOf(str));
								}
							}else if(str.startsWith("duration")){
								str = str.substring(str.lastIndexOf('=') + 1);
								if(!str.equals("N/A")){
									v.setDuration((int) Math.round(Double.valueOf(str)));
								}
							}
						}
					} catch (IOException e1) {
						//버퍼 스트림에서 읽기에 실패했을 때 발생 가능한 예외
						//아직 처리가 구현되지 않음
						e1.printStackTrace();
					}
					try {
						process.waitFor(); //끝날 때 까지 프로세스 대기
					} catch (InterruptedException e) {
						//프로세스 대기 중 인터럽트 상황에 대한 예외
						//아직 처리가 구현되지 않음
						e.printStackTrace();
					}
					
					//codec이 null이라는 뜻은 해당 영상이 FFMpeg에서 지원하지 않는 영상이므로 변환 작업이 불가능하다.
					//그 외 정지 영상으로 취급되는 이미지들도 예외 코덱 목록으로 등록한다.
					if(v.getCodec() == null || v.getCodec().equals("gif") || v.getCodec().equals("mjpeg") || v.getCodec().equals("png") || v.getCodec().equals("tiff")){
						failCount++;
					}else if(v.getBitrate() == 0 || v.getDuration() == 0){
						failCount++;
					}else{
						//최대 50개의 영상만을 저장한다.
						if(videoArray.size() >= 50) return failCount;
						videoArray.add(v);
					}
				}else{
					failCount++;
				}
			}else if(file.isDirectory()){ //로드를 시도할 파일이 디렉토리인경우 재귀를 통해 하위 폴더를 읽는다.
				failCount += selectVideo(file.listFiles());
			}
		}
		return failCount; //로드 실패 횟수를 반환
	}
	
	/**
	 * 현재 로드 되어있는 영상 목록을 반환합니다.
	 * @return 영상 목록
	 */
	public VideoList getVideoList(){
		return videoArray;
	}
	
	/**
	 * 로드에 실패한 횟수를 반환합니다.
	 * @return 로드에 실패한 횟수
	 */
	public int getFailCount(){
		return failCount;
	}
	
	/**
	 * 파일에 대한 확장자 검사를 진행하는 메서드
	 * @param source 확장자를 검사할 파일
	 * @return 확장자 일치 여부 (True:일치, False:불일치)
	 */
	public boolean checkExtension(File source){
		String name = source.getName();
		for(int i = 0 ; i < ext.length ; i++){
			if(name.endsWith(ext[i])) return true;
		}
		return false;
	}
}
