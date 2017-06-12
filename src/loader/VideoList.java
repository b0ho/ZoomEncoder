package loader;
import java.util.ArrayList;

/**
 * 영상의 목록을 관리하는 클래스 입니다.</br>
 * 원소의 추가, 제거등은 ArrayList에서 지원하는 메서드를 사용하십시오.
 * @author admin
 *
 */
public class VideoList extends ArrayList<Video>{
	
	private static final long serialVersionUID = -5423069755660708788L;

	/**
	 * 인덱스의 원소를 한칸 위로 올립니다.
	 * @param index 올릴 원소의 인덱스
	 */
	public void moveUp(int index){
		//index가 0인경우는 이 메서드가 작동하지 않음
		if(index < 1 || index >= this.size()) return;
		Video tmp = this.get(index - 1);
		this.set(index - 1, get(index));
		this.set(index, tmp);
	}
	
	/**
	 * 인덱스의 원소를 한칸 밑으로 내립니다.
	 * @param index 내릴 원소의 인덱스
	 */
	public void moveDown(int index){
		//index가 size - 2 보다 큰 경우는 이 메서드가 작동하지 않음
		if(index < 0 || index >= this.size() - 1) return;
		Video tmp = this.get(index + 1);
		this.set(index + 1, get(index));
		this.set(index, tmp);
	}
	
	/**
	 * 인덱스의 원소를 맨 위로 올립니다.
	 * @param index 맨 위로 올릴 원소의 인덱스
	 */
	public void moveTop(int index){
		//index가 0인경우는 이 메서드가 작동하지 않음
		if(index < 1 || index >= this.size()) return;
		for(int i = index ; i > 0 ; i--){
			moveUp(i);
		}
	}

	/**
	 * 인덱스의 원소를 맨 아래로 내립니다.
	 * @param index 맨 아래로 내릴 원소의 인덱스
	 */
	public void moveBottom(int index){
		//index가 size - 2 보다 큰 경우는 이 메서드가 작동하지 않음
		if(index < 0 || index >= this.size() - 1) return;
		for(int i = index ; i < this.size() - 1 ; i++){
			moveDown(i);
		}
	}
}
