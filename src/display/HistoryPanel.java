package display;

import java.awt.*;
import java.awt.event.*;
import java.util.EventListener;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import history.History;
import option.Option;

/**
 * 히스토리 객체 정보를 표시하는 패널 클래스
 * 
 * @author kim
 *
 */
public class HistoryPanel extends JPanel{
	
	private static final long serialVersionUID = -7952338501080337167L;
	private History h;
	private JLabel informationLabel;
	private JLabel moreLabel;
	private EventListenerList listenerList = new EventListenerList();
	private int dynamicHeight;
	private boolean detailView = false;

	/**
	 * 히스토리 패널 생성자.
	 * @param h History 객체
	 */
	public HistoryPanel(History h) {
		this.h = h;
		initComponents();
	}
	
	/** 보기 옵션 클릭 시 사이즈 동적 변환을 위한 이벤트 리스너 등록 */
	public void addActionListener(ActionListener listener){
		listenerList.add(ActionListener.class, listener);
	}

	/** 보기 옵션 클릭 시 사이즈 동적 변환을 위한 이벤트 리스너 제거 */
	public void removeActionListener(ActionListener listener){
		listenerList.remove(ActionListener.class, listener);
	}
	
	/** 이벤트 콜 */
	private void fireActionEvent(ActionEvent event){
		EventListener[] listeners = listenerList.getListeners(ActionListener.class);
		for(int i = 0 ; i < listeners.length ; i++){
			((ActionListener)listeners[i]).actionPerformed((ActionEvent)event);
		}
	}
	
	/** 컴포넌트 초기화 */
	private void initComponents(){
		informationLabel = new JLabel();
		moreLabel = new JLabel("자세히 보기");
		
		this.setLayout(null);
		this.setBackground(Color.LIGHT_GRAY);
		
		//자세히 보기 <-> 간략히 보기 전환 설정
		moreLabel.setForeground(Color.BLUE);
		moreLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		moreLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(detailView){ //자세히 보기 -> 간략히 보기
					detailView = false;
					moreLabel.setText("자세히 보기");
					informationLabel.setText(simpleInfo());
				}else if(!detailView){ //간략히 보기 -> 자세히 보기
					detailView = true;
					moreLabel.setText("간략히 보기");
					informationLabel.setText(detailInfo());
				}
				sizeSet();
				ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Resize");
				fireActionEvent(evt);
			}
		});
		
		informationLabel.setText(simpleInfo());
		sizeSet();
		this.add(informationLabel);
		this.add(moreLabel);
	}
	
	/**
	 * 간략 정보에 관한 텍스트를 생성하는 메서드
	 * @return 간략 정보
	 */
	private String simpleInfo(){
		StringBuffer str = new StringBuffer();
		str.append("<html>");
		str.append("<b>" + h.getVideoName());
		str.append(h.getOriginalVideoExtension() + "</b><br>");
		str.append("작업 시간 : " + h.getFormattedWorkStartTime() + " ~ ");
		if(h.getWorkEndTime() != 0){
			str.append(h.getFormattedWorkEndTime() + "<br>");
			if(h.getWorkedTime() != 0){
				str.append("작업 소요 시간 : " + h.getFormattedWorkedTime() + "<br>");
			}else{
				str.append("- 사용자에 의한 작업 취소<br>");
			}
		}else{
			str.append("진행 중<br>");
		}
		
		if(h.getConvertMode().equals("Real")){
			str.append("작업 모드 : 실 변환");
		}else if(h.getConvertMode().equals("Test")){
			str.append("작업 모드 : 테스트 변환");
		}else{
			str.append("작업 모드 : 알 수 없음");
		}
		str.append("</html>");
		
		return str.toString();
	}
	
	/**
	 * 자세한 정보에 관한 텍스트를 생성하는 메서드
	 * @return 자세한 정보
	 */
	private String detailInfo(){
		StringBuffer str = new StringBuffer();
		str.append("<html>");
		str.append("<b>" + h.getVideoName());
		str.append(h.getOriginalVideoExtension() + "</b><br>");
		str.append("작업 시간 : " + h.getFormattedWorkStartTime() + " ~ ");
		if(h.getWorkEndTime() != 0){
			str.append(h.getFormattedWorkEndTime() + "<br>");
			if(h.getWorkedTime() != 0){
				str.append("작업 소요 시간 : " + h.getFormattedWorkedTime() + "<br>");
			}else{
				str.append("- 사용자에 의한 작업 취소<br>");
			}
		}else{
			str.append("진행 중<br>");
		}

		if(h.getConvertMode().equals("Real")){
			str.append("작업 모드 : 실 변환");
		}else if(h.getConvertMode().equals("Test")){
			str.append("작업 모드 : 테스트 변환");
		}else{
			str.append("작업 모드 : 알 수 없음");
		}
		str.append("<br>");
		str.append("<br><b>상세 정보</b><br>");
		if(h.getConvertMode().equals("Real")){
			str.append("해상도 : " + h.getVideoWidth() + "x" + h.getVideoHeight() + "<br>");
			str.append("비트레이트 : " + h.getVideoBitrate() + "Kbps<br>");
		}
		str.append("확대율 : " + h.getScaleOption() + "<br>");
		str.append("노이즈 제거율 : " + h.getNoiseOption() + "<br>");
		if(h.getConvertMode().equals("Real")){
			if(h.getModelOption().equals(Option.Model.PHOTO)){
				str.append("변환 모델 : 실사 영상 변환<br>");
			}else if(h.getModelOption().equals(Option.Model.ANIME)){
				str.append("변환 모델 : 애니메이션 변환<br>");
			}else{
				str.append("변환 모델 : 알 수 없음<br>");
			}
			str.append("코덱 : " + h.getCodecOption() + "<br>");
			if(h.getQualityOption() == 0){
				str.append("품질 : 무손실<br>");
			}else if(h.getQualityOption() == 15){
				str.append("품질 : 최고품질<br>");
			}else if(h.getQualityOption() == 19){
				str.append("품질 : 고품질<br>");
			}else if(h.getQualityOption() == 23){
				str.append("품질 : 보통품질<br>");
			}else{
				str.append("품질 : 알 수 없음<br>");
			}
		}
		if(h.getConvertMode().equals("Real")){
			str.append("영상 용량 : " + h.getResizeVideoSize() + "Byte<br>");
			if(h.getResizeVideoPath() != null){
				if(!h.getResizeVideoPath().equals("null"))
					str.append("저장 경로 : " + h.getResizeVideoPath() + "<br>");
			}
		}
		str.append("임시파일 용량 : " + h.getTempFileSize() + "Byte<br>");
		str.append("</html>");
		
		return str.toString();
	}
	
	/** 텍스트 길이에 따른 동적인 사이즈 설정 */
	private void sizeSet(){
		dynamicHeight = (int) informationLabel.getPreferredSize().getHeight() + 24;
		informationLabel.setBounds(0, 0, 425, dynamicHeight + 10);
		moreLabel.setBounds(0, informationLabel.getHeight(), 425, 20);
		this.setSize(425, informationLabel.getHeight() + moreLabel.getHeight());
	}
}
