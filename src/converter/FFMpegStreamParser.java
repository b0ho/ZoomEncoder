package converter;

/**
 * FFMpeg 스트림 분석 유틸 클래스
 * @author admin
 *
 */
public class FFMpegStreamParser {
	
	/**
	 * 스트림을 분석하여 현재 상태를 반환
	 * @param str 읽혀진 스트림
	 * @return 현재 상태
	 */
	public static ConvertState getCurrentState(String str){
		if(str.startsWith("frame=")){ // 진행중인 스트림만 처리
			String[] sTemp = str.split(" ");
			int frame = 0;
			double speed = 0.0;
			for(int i = 0 ; i < sTemp.length ; i++){
				if(sTemp[i].matches("fps=.*")){
					if(sTemp[i-1].startsWith("frame=")){
						frame = Integer.valueOf(sTemp[i-1].substring(6));
					}else{
						frame = Integer.valueOf(sTemp[i-1]);
					}
				}
				if(sTemp[i].matches(".*x")){
					try{
						if(sTemp[i].startsWith("speed=")){
							speed = Double.valueOf(sTemp[i].substring(6, sTemp[i].length() - 1));
						}else{
							speed = Double.valueOf(sTemp[i].substring(0, sTemp[i].length() - 1));
						}
					}catch (StringIndexOutOfBoundsException e){	}
				}
			}
			return new ConvertState(frame, speed);
		}else{
			return null;
		}
	}
}
