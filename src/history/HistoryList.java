package history;
import java.util.ArrayList;

/**
 * 히스토리들을 관리하는 리스트 입니다.
 * @author admin
 *
 */
public class HistoryList extends ArrayList<History> {

	private static final long serialVersionUID = -3622425366981578587L;
	
	/**
	 * 작업이 완료된 히스토리를 리스트에서 제거합니다.<p>
	 * 특정 히스토리만 제거하거나 전체를 제거하려면 ArrayList의 메소드를 사용하십시오.
	 */
	public void deleteCompleteHistory(){
		for(int i = 0 ; i < this.size() ; i++){
			History h = this.get(i);
			if(h.getWorkEndTime() != 0){
				this.remove(i);
				i--;
			}
		}
		HistoryFile.writeHistoryFile(this);
	}
}
