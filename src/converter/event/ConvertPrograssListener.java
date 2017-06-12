package converter.event;

import java.util.EventListener;

/**
 * 변환 진행 이벤트 리스너입니다.
 * @author 강승민
 *
 */
public interface ConvertPrograssListener extends EventListener{
	
	/**
	 * 핸들러 메소드 입니다.
	 * @param event 변환 진행 이벤트
	 */
	public void convertPrograss(ConvertPrograssEvent event);
}
