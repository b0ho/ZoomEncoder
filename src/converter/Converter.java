package converter;
import java.io.*;
import java.util.EventListener;

import javax.swing.event.EventListenerList;

import converter.event.*;
import history.*;
import loader.*;
import option.*;
import util.*;

/**
 * 실변환 혹은 테스트 변환을 진행합니다.
 * @author admin
 *
 */
public class Converter extends Thread {
	
	/** 변환 모드 */
	public enum Mode{ Real, Test }
	public enum Tmode{ Time, PSNR }
	
	private VideoList vList;
	private Video video;
	private Option option;
	private HistoryList historyList;
	private EventListenerList listenerList = new EventListenerList();
	private int failCount = 0;
	private Mode mode;
	public Tmode tmode;
	
	private int divCnt = 0;
	private int zoomCnt = 0;
	private double divSpeed = 0.0;
	private double zoomSpeed = 0.0;
	private TestResult testResult;
	
	private Process process = null;
	private Waifu2xPrograss w2xPrograss = null;
	private boolean workEnable = true;
	
	/**
	 * 생성자. videoList가 들어가면 실변환 모드압니다.
	 * @param videoList 변환을 진행할 영상 목록
	 * @param op 옵션
	 */
	public Converter(VideoList videoList, Option op, HistoryList historys){
		vList = videoList;
		option = op;
		historyList = historys;
		mode = Mode.Real;
	}
	
	/**
	 * 생성자. video가 들어가면 테스트 변환 모드입니다.
	 * @param v 테스트 변환을 진행할 영상
	 * @param op 옵션
	 */
	public Converter(Video v, Option op, HistoryList historys){
		video = v;
		option = op;
		historyList = historys;
		mode = Mode.Test;
	}
	
	/**
	 * 실변환 진행 메서드
	 */
	private void convertReal(){
		workEnable = true;
		failCount = 0;
		for(Video v : vList){
			if(!workEnable) break;
			File f = new File(option.getTemporaryOriginalImagePath() + "/" + v.getVideoName()); //해당 비디오 명으로 폴더를 생성함
			if(!f.isDirectory()){
				try{
					f.mkdirs();
				}catch(SecurityException e){
					failCount++;
					continue;
				}
			}
			
			f = new File(option.getResizeVideoFilePath());
			if(!f.isDirectory()){
				try{
					f.mkdirs();
				}catch(SecurityException e){
					failCount++;
					continue;
				}
			}
			
			f = new File(option.getTemporaryResizeImagePath() + "/" + v.getVideoName()); //해당 비디오 명으로 폴더를 생성함
			if(!f.isDirectory()){
				try{
					f.mkdirs();
				}catch(SecurityException e){
					failCount++;
					continue;
				}
			}
			History h = new History();
			historyList.add(0, h);
			h.setVideoName(v.getVideoName());
			h.setOriginalVideoExtension(v.getVideoExtension());
			h.setResizeVideoExtension(option.getExtension());
			h.setWorkStartTime(System.currentTimeMillis() / 1000);
			h.setConvertMode(Converter.Mode.Real);
			h.setCodecOption(option.getCodec());
			h.setScaleOption(option.getScale());
			h.setNoiseOption(option.getNoise());
			h.setModelOption(option.getModel());
			h.setQualityOption(option.getQuality());
			HistoryFile.writeHistoryFile(historyList);
			
			if(workEnable){
				divideWork(v); // 영상 분할 작업 진행
			}
			
			h.setTempFileSize(Util.getFolderSize(new File(option.getTemporaryOriginalImagePath() + "/" + v.getVideoName())));
			HistoryFile.writeHistoryFile(historyList);
			
			if(workEnable){
				zoomWork(v); // 영상 확대 작업 진행
			}
			
			h.setTempFileSize(h.getTempFileSize() + Util.getFolderSize(new File(option.getTemporaryResizeImagePath() + "/" + v.getVideoName())));
			HistoryFile.writeHistoryFile(historyList);
			
			f = new File(option.getResizeVideoFilePath() + "/" + v.getVideoName() + option.getExtension());
			if(f.isFile()){
				f.delete();
			}
			
			if(workEnable){
				mergeWork(v); // 영상 병합 작업 진행
			}
			
			h.setWorkEndTime(System.currentTimeMillis() / 1000);
			if(workEnable){
				h.setWorkedTime(h.getWorkEndTime() - h.getWorkStartTime());
			}else{
				h.setWorkedTime(0);
			}
			
			if(workEnable){
				VideoLoader completeVideoLoader = new VideoLoader();
				completeVideoLoader.selectVideo(new File[]{new File(option.getResizeVideoFilePath() + "/" + v.getVideoName() + option.getExtension())});
				Video completeVideo = completeVideoLoader.getVideoList().get(0);
				
				h.setResizeVideoSize(completeVideo.getVideoSize());
				h.setResizeVideoPath(option.getResizeVideoFilePath());
				h.setVideoBitrate(completeVideo.getBitrate());
				h.setVideoWidth(completeVideo.getWidth());
				h.setVideoHeight(completeVideo.getHeight());
			}
			
			HistoryFile.writeHistoryFile(historyList);
			
			if(option.getRemoveOriginalVideoFile() && workEnable){
				v.getVideoFile().delete();
			}
			if(option.getRemoveTemporaryOriginalFile()){
				Util.deleteDirectory(new File(option.getTemporaryOriginalImagePath() + "/" + v.getVideoName()));
			}
			if(option.getRemoveTemporaryResizeFile()){
				Util.deleteDirectory(new File(option.getTemporaryResizeImagePath() + "/" + v.getVideoName()));
			}
			
			if(workEnable){
				ConvertPrograssEvent evt = new ConvertPrograssEvent(this, video, ConvertPrograssEvent.Mode.Complete, new ConvertState(0, 0), 0, 0);
				fireConvertPrograssEvent(evt);
			}
		}
	}
	
	/**
	 * 테스트 변환 진행 메서드
	 */
	private void convertTest(){
		workEnable = true;
		failCount = 0;
		File f = new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName());
		try{
			if(f.isDirectory()){
				Util.deleteDirectory(f);
			}
			f.mkdirs();
		}catch(SecurityException e){
			failCount++;
			return;
		}
		
		f = new File(option.getTemporaryResizeImagePath() + "/Test/" + video.getVideoName());
		try{
			if(f.isDirectory()){
				Util.deleteDirectory(f);
			}
			f.mkdirs();
		}catch(SecurityException e){
			failCount++;
			return;
		}
		
		f = new File(option.getTemporaryOriginalImagePath() + "/Test/psnr/" + video.getVideoName());
		try{
			if(f.isDirectory()){
				Util.deleteDirectory(f);
			}
			f.mkdirs();
		}catch(SecurityException e){
			failCount++;
			return;
		}
		
		this.addConvertPrograssListener(new ConvertPrograssListener() {
			@Override
			public void convertPrograss(ConvertPrograssEvent event) {
				if(event.mode == ConvertPrograssEvent.Mode.Divide){
					divCnt++;
					divSpeed = divSpeed + event.currentState.speed;
				}else if(event.mode == ConvertPrograssEvent.Mode.Zoom){
					zoomCnt++;
					zoomSpeed = zoomSpeed + event.currentState.speed;
				}
			}
		});
		
		History h = new History();
		historyList.add(0, h);
		h.setVideoName(video.getVideoName());
		h.setOriginalVideoExtension(video.getVideoExtension());
		h.setResizeVideoExtension(option.getExtension());
		h.setWorkStartTime(System.currentTimeMillis() / 1000);
		h.setConvertMode(Converter.Mode.Test);
		h.setScaleOption(option.getScale());
		h.setNoiseOption(option.getNoise());
		h.setModelOption(option.getModel());
		HistoryFile.writeHistoryFile(historyList);
		
		if(workEnable){
			divideWork(video);
		}
		
		h.setTempFileSize(Util.getFolderSize(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName())));
		HistoryFile.writeHistoryFile(historyList);
		
		File[] fList = new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName()).listFiles();
		for(int i = fList.length - 1 ; i >= option.getTestFrame() ; i--){
			fList[i].delete();
		}

		if(workEnable){
			zoomWork(video); // 영상 확대 작업 진행
		}

		h.setTempFileSize(h.getTempFileSize() + Util.getFolderSize(new File(option.getTemporaryResizeImagePath() + "/Test/" + video.getVideoName())));
		h.setWorkEndTime(System.currentTimeMillis() / 1000);
		if(workEnable){
			h.setWorkedTime(h.getWorkEndTime() - h.getWorkStartTime());
		}else{
			h.setWorkedTime(0);
		}
		HistoryFile.writeHistoryFile(historyList);
		
		if(workEnable){
			testResult = new TestResult(divSpeed / divCnt, zoomSpeed / zoomCnt, (int) (video.getDuration() * video.getFramerate()));
		}
		
		if(option.getRemoveTemporaryOriginalFile()){
			Util.deleteDirectory(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName()));
			Util.deleteDirectory(new File(option.getTemporaryOriginalImagePath() + "/Test/psnr/" + video.getVideoName()));			
		}
		if(option.getRemoveTemporaryResizeFile()){
			Util.deleteDirectory(new File(option.getTemporaryResizeImagePath() + "/Test/" + video.getVideoName()));
		}
		
		if(workEnable){
			ConvertPrograssEvent evt = new ConvertPrograssEvent(this, video, ConvertPrograssEvent.Mode.Complete, new ConvertState(0, 0), 0, 0);
			fireConvertPrograssEvent(evt);
		}
	}
	
	/**
	 * psnr측정 메서드
	 */
	private void psnrTest(){
		workEnable = true;
		failCount = 0;
		File f = new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName());
		try{
			if(f.isDirectory()){
				Util.deleteDirectory(f);
			}
			f.mkdirs();
		}catch(SecurityException e){
			failCount++;
			return;
		}
		
		f = new File(option.getTemporaryResizeImagePath() + "/Test/z_up/" + video.getVideoName());
		try{
			if(f.isDirectory()){
				Util.deleteDirectory(f);
			}
			f.mkdirs();
		}catch(SecurityException e){
			failCount++;
			return;
		}
		
		f = new File(option.getTemporaryOriginalImagePath() + "/Test/n_down/" + video.getVideoName());
		try{
			if(f.isDirectory()){
				Util.deleteDirectory(f);
			}
			f.mkdirs();
		}catch(SecurityException e){
			failCount++;
			return;
		}
		
		f = new File(option.getTemporaryResizeImagePath() + "/Test/n_up/" + video.getVideoName());
		try{
			if(f.isDirectory()){
				Util.deleteDirectory(f);
			}
			f.mkdirs();
		}catch(SecurityException e){
			failCount++;
			return;
		}
		
		this.addConvertPrograssListener(new ConvertPrograssListener() {
			@Override
			public void convertPrograss(ConvertPrograssEvent event) {
				if(event.mode == ConvertPrograssEvent.Mode.Divide){
					divCnt++;
					divSpeed = divSpeed + event.currentState.speed;
				}else if(event.mode == ConvertPrograssEvent.Mode.Zoom){
					zoomCnt++;
					zoomSpeed = zoomSpeed + event.currentState.speed;
				}
			}
		});
		
		double tmpScale = option.getScale();
		Option.TestOption tmpTestOption = option.getTestOption();
		int tmpTestFrame = option.getTestFrame();
		int tmpNoise = option.getNoise();
		
		option.setScale(2.0);
		option.setTestFrame(5);
		option.setNoise(3);
		option.setTestOption(Option.TestOption.RANDOM);
		
		if(workEnable){
			divideWork(video);
			nomalZoomDownWork(video);
			nomalZoomUpWork(video);
		}
		
		File[] fList = new File(option.getTemporaryOriginalImagePath() + "/Test/n_down/" + video.getVideoName()).listFiles();
		for(int i = fList.length - 1 ; i >= option.getTestFrame() ; i--){
			fList[i].delete();
		}

		if(workEnable){
			zoomWork(video); // 영상 확대 작업 진행
		}

		option.setScale(tmpScale);
		option.setTestFrame(tmpTestFrame);
		option.setNoise(tmpNoise);
		option.setTestOption(tmpTestOption);
		
		if(workEnable){
			double[] n_psnr = new double[5]; 
			double[] z_psnr = new double[5];
			
			n_psnr[0] = PSNR.mesurePSNR(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName() + "/img000000001.jpg"), new File(option.getTemporaryResizeImagePath() + "/Test/n_up/" + video.getVideoName() + "/img000000001.jpg"));
			n_psnr[1] = PSNR.mesurePSNR(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName() + "/img000000002.jpg"), new File(option.getTemporaryResizeImagePath() + "/Test/n_up/" + video.getVideoName() + "/img000000002.jpg"));
			n_psnr[2] = PSNR.mesurePSNR(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName() + "/img000000003.jpg"), new File(option.getTemporaryResizeImagePath() + "/Test/n_up/" + video.getVideoName() + "/img000000003.jpg"));
			n_psnr[3] = PSNR.mesurePSNR(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName() + "/img000000004.jpg"), new File(option.getTemporaryResizeImagePath() + "/Test/n_up/" + video.getVideoName() + "/img000000004.jpg"));
			n_psnr[4] = PSNR.mesurePSNR(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName() + "/img000000005.jpg"), new File(option.getTemporaryResizeImagePath() + "/Test/n_up/" + video.getVideoName() + "/img000000005.jpg"));
			z_psnr[0] = PSNR.mesurePSNR(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName() + "/img000000001.jpg"), new File(option.getTemporaryResizeImagePath() + "/Test/z_up/" + video.getVideoName() + "/img000000001.png"));
			z_psnr[1] = PSNR.mesurePSNR(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName() + "/img000000002.jpg"), new File(option.getTemporaryResizeImagePath() + "/Test/z_up/" + video.getVideoName() + "/img000000002.png"));
			z_psnr[2] = PSNR.mesurePSNR(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName() + "/img000000003.jpg"), new File(option.getTemporaryResizeImagePath() + "/Test/z_up/" + video.getVideoName() + "/img000000003.png"));
			z_psnr[3] = PSNR.mesurePSNR(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName() + "/img000000004.jpg"), new File(option.getTemporaryResizeImagePath() + "/Test/z_up/" + video.getVideoName() + "/img000000004.png"));
			z_psnr[4] = PSNR.mesurePSNR(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName() + "/img000000005.jpg"), new File(option.getTemporaryResizeImagePath() + "/Test/z_up/" + video.getVideoName() + "/img000000005.png"));

			testResult = new TestResult(divSpeed / divCnt, zoomSpeed / zoomCnt, (int) (video.getDuration() * video.getFramerate()), n_psnr, z_psnr);
		}
		
		if(option.getRemoveTemporaryOriginalFile()){
			Util.deleteDirectory(new File(option.getTemporaryOriginalImagePath() + "/Test/" + video.getVideoName()));
		}
		Util.deleteDirectory(new File(option.getTemporaryOriginalImagePath() + "/Test/n_down/" + video.getVideoName()));
		Util.deleteDirectory(new File(option.getTemporaryResizeImagePath() + "/Test/z_up/" + video.getVideoName()));
		Util.deleteDirectory(new File(option.getTemporaryResizeImagePath() + "/Test/n_up/" + video.getVideoName()));
		
		if(workEnable){
			ConvertPrograssEvent evt = new ConvertPrograssEvent(this, video, ConvertPrograssEvent.Mode.Complete, new ConvertState(0, 0), 0, 0);
			fireConvertPrograssEvent(evt);
		}
	}
	
	/**
	 * 변환 작업을 취소하는 메서드 입니다.
	 */
	public void cancelWork(){
		if(process != null){
			process.destroy();
			if(w2xPrograss != null){
				w2xPrograss.interrupt();
			}
			workEnable = false;
		}
	}
	
	/**
	 * 변환 진행상황 이벤트 리스너를 추가합니다.
	 * @param listener 변환 진행상황 이벤트
	 */
	public void addConvertPrograssListener(ConvertPrograssListener listener){
		listenerList.add(ConvertPrograssListener.class, listener);
	}
	
	/**
	 * 변환 진행상황 이벤트 리스너를 제거합니다.
	 * @param listener 변환 진행상황 이벤트
	 */
	public void removeConvertPrograssListener(ConvertPrograssListener listener){
		listenerList.remove(ConvertPrograssListener.class, listener);
	}
	
	/**
	 * 변환 실패 횟수를 반환합니다.
	 * @return 변환 실패 횟수
	 */
	public int getFailCount(){
		return failCount;
	}
	
	/**
	 * 테스트 결과를 반환합니다.
	 * @return 테스트 결과
	 */
	public TestResult getTestResult(){
		return testResult;
	}
	
	/**
	 * 이벤트 콜
	 * @param event
	 */
	private void fireConvertPrograssEvent(ConvertPrograssEvent event){
		EventListener[] listeners = listenerList.getListeners(ConvertPrograssListener.class);
		for(int i = 0 ; i < listeners.length ; i++){
			((ConvertPrograssListener)listeners[i]).convertPrograss((ConvertPrograssEvent)event);
		}
	}
	
	/**
	 * 영상 분할 작업
	 * @param v 영상
	 */
	private void divideWork(Video v){
		process = null; // 프로세스 초기화
		int totalFrame = (int) (v.getDuration() * v.getFramerate()); // 토탈 프레임 계산
		long workStartTime = System.currentTimeMillis(); // 작업 시작 시간
		
		try {
			if(mode == Mode.Real){
				process = new ProcessBuilder(FFMpeg.imageOutputCommand(v.getVideoFilePath(), option.getTemporaryOriginalImagePath() + "/" + v.getVideoName())).start();
			}else if(mode == Mode.Test){
				if(totalFrame*1.1 < option.getTestFrame()){
					failCount++;
					return;
				}
				if(option.getTestOption() == Option.TestOption.TIME){
					process = new ProcessBuilder(FFMpeg.timelineTestCommand(v.getVideoFilePath(), option.getTemporaryOriginalImagePath() + "/Test/" + v.getVideoName(), option.getStartTime(), option.getTestFrame())).start();
					totalFrame = option.getTestFrame();
				}else{
					option.setThumbnailRange((int)Math.floor((totalFrame / (double)(option.getTestFrame()*1.1))));
					totalFrame = option.getTestFrame();
					process = new ProcessBuilder(FFMpeg.randomThumbnailTestCommand(v.getVideoFilePath(), option.getTemporaryOriginalImagePath() + "/Test/" + v.getVideoName(), v.getFramerate(), option.getThumbnailRange())).start();
				}
			}
		} catch (IOException e1) {
			failCount++;
			return;
		}
		String str = null;
		BufferedReader stdOut = new BufferedReader( new InputStreamReader(process.getErrorStream()) ); //영상 정보를 스트림으로 읽는다.
		try {
			while( (str = stdOut.readLine()) != null ) {
				// 스트림을 분석하여 처리
				ConvertState s = FFMpegStreamParser.getCurrentState(str);
				if(s != null){
					ConvertPrograssEvent evt = new ConvertPrograssEvent(this, v, ConvertPrograssEvent.Mode.Divide, s, totalFrame, System.currentTimeMillis() - workStartTime);
					fireConvertPrograssEvent(evt);
				}
			}
		} catch (IOException e1) {
			failCount++;
			return;
		}
		try {
			process.waitFor(); //끝날 때 까지 프로세스 대기
		} catch (InterruptedException e) {
			failCount++;
			return;
		}
		try {
			stdOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * PSNR 계산을 위한 일반적인 크기의 영상 변환작업
	 * @param v 영상
	 */
	private void nomalZoomDownWork(Video v){
		process = null; // 프로세스 초기화
		try {
			if(mode == Mode.Test){
				process = new ProcessBuilder(FFMpeg.imageSizeDownCommand(option.getTemporaryOriginalImagePath() + "/Test/", v.getVideoName(), (int)(video.getWidth()/option.getScale()), (int)(video.getHeight()/option.getScale()))).start();
			}
		} catch (IOException e1) {
			failCount++;
			
			return;
		}
		try {
			process.waitFor(); //끝날 때 까지 프로세스 대기
		} catch (InterruptedException e) {
			failCount++;
			return;
		}
	}
	
	/**
	 * PSNR 계산을 위한 일반적인 크기의 영상 변환작업
	 * @param v 영상
	 */
	private void nomalZoomUpWork(Video v){
		process = null; // 프로세스 초기화
		try {
			if(mode == Mode.Test){
				process = new ProcessBuilder(FFMpeg.imageSizeUpCommand(option.getTemporaryOriginalImagePath()  + "/Test/" , option.getTemporaryResizeImagePath()  + "/Test/" , v.getVideoName(), (int)(video.getWidth()), (int)(video.getHeight()))).start();
			}
		} catch (IOException e1) {
			failCount++;
			
			return;
		}
		try {
			process.waitFor(); //끝날 때 까지 프로세스 대기
		} catch (InterruptedException e) {
			failCount++;
			return;
		}
	}
	
	/**
	 * 영상 확대 작업
	 * @param v 영상
	 */
	private void zoomWork(Video v){
		int flen = 0;
		// 실변환 모드일 경우 기존에 작업이 진행중이었다면 이어서 진행할 수 있도록 함.
		if(mode == Mode.Real){
			File[] flr = new File(option.getTemporaryResizeImagePath() + "/" + v.getVideoName()).listFiles();
			flen = flr.length;
			if(flen > 5){
				File[] flo = new File(option.getTemporaryOriginalImagePath() + "/" + v.getVideoName()).listFiles();
				for(int i = 0 ; i < flen - 5 ; i++){
					flo[i].delete();
				}
				for(int i = flen - 1 ; i >= flen - 5 ; i--){
					flr[i].delete();
				}
				flen = new File(option.getTemporaryResizeImagePath() + "/" + v.getVideoName()).listFiles().length;
			}else if(flen > 0){
				for(int i = 0 ; i < flen ; i++){
					flr[i].delete();
				}
				flen = 0;
			}else{
				flen = 0;
			}
		}
		
		process = null; // 프로세스 초기화
		
		try {
			if(mode == Mode.Real){
				process = new ProcessBuilder(Waifu2x.waifu2xCommand(option.getTemporaryOriginalImagePath() + "/" + v.getVideoName(), option.getTemporaryResizeImagePath() + "/" + v.getVideoName(), option.getNoise(), option.getScale(), option.getModel(), option.getProcess())).start();
			}else if(mode == Mode.Test){
				if(tmode == Tmode.Time) {
					process = new ProcessBuilder(Waifu2x.waifu2xCommand(option.getTemporaryOriginalImagePath() + "/Test/" + v.getVideoName(), option.getTemporaryResizeImagePath() + "/Test/" + v.getVideoName(), option.getNoise(), option.getScale(), option.getModel(), option.getProcess())).start();
				}else if(tmode == Tmode.PSNR) {
					process = new ProcessBuilder(Waifu2x.waifu2xCommand(option.getTemporaryOriginalImagePath() + "/Test/n_down/" + v.getVideoName(), option.getTemporaryResizeImagePath() + "/Test/z_up/" + v.getVideoName(), option.getNoise(), option.getScale(), option.getModel(), option.getProcess())).start();
				}
			}
		} catch (IOException e1) {
			failCount++;
			return;
		}
		w2xPrograss = null;
		if(mode == Mode.Real){
			w2xPrograss = new Waifu2xPrograss(option.getTemporaryOriginalImagePath() + "/" + v.getVideoName(), option.getTemporaryResizeImagePath() + "/" + v.getVideoName(), v, flen);
		}else if(mode == Mode.Test){
			if(tmode == Tmode.Time) {
				w2xPrograss = new Waifu2xPrograss(option.getTemporaryOriginalImagePath() + "/Test/" + v.getVideoName(), option.getTemporaryResizeImagePath() + "/Test/" + v.getVideoName(), v, flen);
			}else if(tmode == Tmode.PSNR) {
				w2xPrograss = new Waifu2xPrograss(option.getTemporaryOriginalImagePath() + "/Test/n_down/" + v.getVideoName(), option.getTemporaryResizeImagePath() + "/Test/z_up/" + v.getVideoName(), v, flen);
			}
		}
		// Waifu2x의 진행 상황을 처리할 이벤트 핸들러 구현
		w2xPrograss.addWaifu2xPrograssListener(new Waifu2xPrograssListener() {
			@Override
			public void waifu2xConvertPrograss(Waifu2xPrograssEvent event) {
				double spd = 0.0;
				if(event.currentFrame > 0){
					spd = (double)(event.currentFrame - event.prevWorkFrame) / (double)(event.workingTime);
				}
				ConvertState s = new ConvertState(event.currentFrame, spd);
				ConvertPrograssEvent evt = new ConvertPrograssEvent(this, w2xPrograss.getVideo(), ConvertPrograssEvent.Mode.Zoom, s, event.totalFrame, event.workingTime*1000, event.prevWorkFrame);
				fireConvertPrograssEvent(evt);
			}
		});
		w2xPrograss.start();
		try {
			process.waitFor(); //끝날 때 까지 프로세스 대기
			w2xPrograss.join(); // Waifu2x 처리 스레드가 끝날 때 까지 대기
		} catch (InterruptedException e) {
			failCount++;
			return;
		}
	}
	
	/**
	 * 영상 병합 작업
	 * @param v 영상
	 */
	private void mergeWork(Video v){
		process = null; // 프로세스 초기화
		int totalFrame = (int) (v.getDuration() * v.getFramerate());
		long workStartTime = System.currentTimeMillis();
		
		try {
			process = new ProcessBuilder(FFMpeg.imageMergeCommand(v.getVideoFilePath(), option.getTemporaryResizeImagePath() + "/" + v.getVideoName(), option.getResizeVideoFilePath() + "/" + v.getVideoName() + option.getExtension(), v.getFramerate(), option.getKeyFrame(), option.getCodec(), option.getPreset(), option.getQuality())).start();
		} catch (IOException e1) {
			failCount++;
			return;
		}
		String str = null;
		BufferedReader stdOut = new BufferedReader( new InputStreamReader(process.getErrorStream()) ); //영상 정보를 스트림으로 읽는다.
		try {
			while( (str = stdOut.readLine()) != null ) {
				ConvertState s = FFMpegStreamParser.getCurrentState(str);
				if(s != null){
					ConvertPrograssEvent evt = new ConvertPrograssEvent(this, v, ConvertPrograssEvent.Mode.Merge, s, totalFrame, System.currentTimeMillis() - workStartTime);
					fireConvertPrograssEvent(evt);
				}
			}
		} catch (IOException e1) {
			failCount++;
			return;
		}
		try {
			process.waitFor(); //끝날 때 까지 프로세스 대기
		} catch (InterruptedException e) {
			failCount++;
			return;
		}
	}
	
	@Override
	/**
	 * 변환 스레드 시작
	 */
	public void run(){
		if(mode == Mode.Real){
			convertReal();
		}else if(mode == Mode.Test){
			if(tmode == Tmode.Time){
				convertTest();
			}else if (tmode == Tmode.PSNR){
				psnrTest();
			}
		}
	}
}
