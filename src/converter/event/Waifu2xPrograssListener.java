package converter.event;

import java.util.EventListener;

/**
 * Waifu2x 변환 진행 이벤트 리스너입니다.
 * @author 강승민
 *
 */
public interface Waifu2xPrograssListener extends EventListener{
	/**
	 * 핸들러 메소드 입니다.
	 * @param event Waifu2x 변환 진행 이벤트
	 */
	public void waifu2xConvertPrograss(Waifu2xPrograssEvent event);
}
