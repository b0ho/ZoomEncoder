package history;
import java.io.*;
import java.util.ArrayList;

import converter.Converter;

/**
 * 히스토리를 파일로 쓰거나 파일에서 히스토리를 읽는 클래스입니다.
 * @author admin
 *
 */
public class HistoryFile {
	
	/**
	 * 히스토리 파일을 읽습니다.
	 * @return 파일에서 읽어들인 히스토리의 리스트 입니다.
	 */
	public static HistoryList readHistoryFile(){
		HistoryList hl = new HistoryList();
		BufferedReader br = null;
		ArrayList<String> strList = new ArrayList<String>();
		String s = null;
		
		try{
			br = new BufferedReader(new FileReader("history.log")); //history.log 파일을 읽어들입니다.
			while((s = br.readLine()) != null){
				strList.add(s);
			}
			
			for(int i = 0 ; i < strList.size() / History.historyCount ; i++){
				try{
					History h = new History();
					
					h.setVideoName(strList.get(i*History.historyCount + 0));
					h.setOriginalVideoExtension(strList.get(i*History.historyCount + 1));
					h.setResizeVideoExtension(strList.get(i*History.historyCount + 2));
					h.setWorkStartTime(Long.valueOf(strList.get(i*History.historyCount + 3)));
					h.setWorkEndTime(Long.valueOf(strList.get(i*History.historyCount + 4)));
					h.setWorkedTime(Long.valueOf(strList.get(i*History.historyCount + 5)));
					h.setConvertMode(convertMode(strList.get(i*History.historyCount + 6)));
					h.setResizeVideoSize(Long.valueOf(strList.get(i*History.historyCount + 7)));
					h.setTempFileSize(Long.valueOf(strList.get(i*History.historyCount + 8)));
					h.setCodecOption(strList.get(i*History.historyCount + 9));
					h.setScaleOption(Double.valueOf(strList.get(i*History.historyCount + 10)));
					h.setNoiseOption(Integer.valueOf(strList.get(i*History.historyCount + 11)));
					h.setModelOption(strList.get(i*History.historyCount + 12));
					h.setQualityOption(Integer.valueOf(strList.get(i*History.historyCount + 13)));
					h.setResizeVideoPath(strList.get(i*History.historyCount + 14));
					h.setVideoWidth(Integer.valueOf(strList.get(i*History.historyCount + 15)));
					h.setVideoHeight(Integer.valueOf(strList.get(i*History.historyCount + 16)));
					h.setVideoBitrate(Integer.valueOf(strList.get(i*History.historyCount + 17)));
					
					hl.add(h);
				}catch(NumberFormatException e){
					continue;
				}
			}
			
			br.close();
		}catch(IOException e){
			// 만약 파일이 없거나 읽어들이는 경과값이 정상적이지 않을경우 빈 히스토리 목록을 반환합니다.
			return hl;
		}
		return hl;
	}
	
	/**
	 * 히스토리 리스트를 파일로 씁니다.
	 * @param historyList 파일로 쓸 히스토리 리스트
	 */
	public static void writeHistoryFile(HistoryList historyList){
		BufferedWriter bw = null;
		
		try{
			bw = new BufferedWriter(new FileWriter("history.log"));
			
			for(History h : historyList){
				bw.write(h.getVideoName() + "");
				bw.newLine();
				bw.write(h.getOriginalVideoExtension() + "");
				bw.newLine();
				bw.write(h.getResizeVideoExtension() + "");
				bw.newLine();
				bw.write(h.getWorkStartTime() + "");
				bw.newLine();
				bw.write(h.getWorkEndTime() + "");
				bw.newLine();
				bw.write(h.getWorkedTime() + "");
				bw.newLine();
				bw.write(h.getConvertMode() + "");
				bw.newLine();
				bw.write(h.getResizeVideoSize() + "");
				bw.newLine();
				bw.write(h.getTempFileSize() + "");
				bw.newLine();
				bw.write(h.getCodecOption() + "");
				bw.newLine();
				bw.write(h.getScaleOption() + "");
				bw.newLine();
				bw.write(h.getNoiseOption() + "");
				bw.newLine();
				bw.write(h.getModelOption() + "");
				bw.newLine();
				bw.write(h.getQualityOption() + "");
				bw.newLine();
				bw.write(h.getResizeVideoPath() + "");
				bw.newLine();
				bw.write(h.getVideoWidth() + "");
				bw.newLine();
				bw.write(h.getVideoHeight() + "");
				bw.newLine();
				bw.write(h.getVideoBitrate() + "");
				bw.newLine();
			}
			
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 문자열로 기록된 모드를 컨버터 모드로 바꿉니다.
	 * @param arg 문자열로 기록된 모드
	 * @return 컨버터 모드
	 */
	private static Converter.Mode convertMode(String arg){
		if(arg.equals("Real")){
			return Converter.Mode.Real;
		}else if(arg.equals("Test")){
			return Converter.Mode.Test;
		}else{
			return Converter.Mode.Real;
		}
	}
}
