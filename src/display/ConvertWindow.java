package display;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import converter.*;
import converter.event.*;
import converter.event.ConvertPrograssEvent.Mode;
import history.HistoryList;
import loader.VideoList;
import option.*;

/**
 * 프로그램의 변환 윈도우입니다.
 * @author 김선일
 */
public class ConvertWindow extends JDialog {

	private static final long serialVersionUID = -2252618228534370055L;
	/** 옵션 */
	private Option option;
	/** 히스토리 목록 */
	private HistoryList historyList;
	/** 컨버터  */
	private Converter converter;
	/** 비디오 리스트 */
	private VideoList videoList;
	/** 변환할 영상 개수 */
	private int total;
	/** 변환된 영상 개수 */
	private int completeCount;
	/** 영상 미리보기 비활성화시 출력되는 이미지 */
	private ImageIcon defaultImageicon;
	/** 영상 미리보기 활성화시 출력되는 이미지 */
	private ImageIcon convertImageicon;
	/** 영상 미리보기 활성화 체크박스 */
	private JCheckBox previewCheckBox;
	/** 변환 완료시 컴퓨터 종료 활성화 체크박스 */
	private JCheckBox poweroffCheckBox;
	/** 영상 미리보기 이미지 부착 라벨 */
	private JLabel previewLabel;
	/** 변환 취소 버튼 */
	private JButton convertCancleButton;
	/** 현재 진행율 라벨 */
	private JLabel currentprogressLabel;
	/** 현재 진행 작업 표시 라벨 */
	private JLabel detailprogressLabel;
	/** 소요시간 표시 라벨 */
	private JLabel progressspeedLabel;
	/** 작업 속도 표시 라벨 */
	private JLabel taketimeLabel;
	/** 작업 진행바 */
	private JProgressBar currentProgressBar;
	/** 버튼 사이 구별 수평선 */
	private JSeparator horizonLine;

	/**
	 * 변환 윈도우 생성자.
	 * @param con 컨버터
	 * @param totalVideo 변할할 영상 개수
	 */
	public ConvertWindow(Option op, VideoList videoList, HistoryList historyList) {
		option = op;
		this.videoList = videoList;
		this.historyList = historyList;
		converter = new Converter(this.videoList, option, this.historyList);
		total = videoList.size();
		completeCount = 1;
		setModal(true);
		initComponents();
	}

	/**
	 * 컴포넌트 초기화 메서드</br>
	 * 컴포넌트 레이아웃 설정 및 버튼 이벤트 등록
	 */
	private void initComponents() {
		// 컴포넌트 초기화
		convertCancleButton = new JButton();
		previewLabel = new JLabel();
		previewCheckBox = new JCheckBox();
		currentprogressLabel = new JLabel();
		currentProgressBar = new JProgressBar();
		detailprogressLabel = new JLabel();
		horizonLine = new JSeparator();
		poweroffCheckBox = new JCheckBox();
		progressspeedLabel = new JLabel();
		taketimeLabel = new JLabel();
		convertCancleButton = new JButton();

		getContentPane().setLayout(null);
		setResizable(false);

		this.addWindowListener(new WindowAdapter() {
			/** 우측 상단 X버튼 클릭시 발생하는 이벤트 */
			public void windowClosing(WindowEvent e) {
				cancleConvert();
			}
		});

		previewCheckBox.setText("영상 미리보기");
		getContentPane().add(previewCheckBox);
		previewCheckBox.setBounds(100, 315, 101, 23);
		previewCheckBox.setSelected(option.getUsePreview());
		previewCheckBox.addChangeListener(new ChangeListener() {
			/** 미리보기 활성화 체크박스 상태 리스너 */
			public void stateChanged(ChangeEvent e) {
				option.setUsePreview(previewCheckBox.isSelected());
				OptionFile.writeOptionFile(option);
				if (previewCheckBox.isSelected()) {
					previewLabel.setIcon(convertImageicon);
				}else{
					previewLabel.setIcon(defaultImageicon);
				}

			}
		});

		previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		previewLabel.setVerticalTextPosition(SwingConstants.CENTER);

		defaultImageicon = new ImageIcon("res/previewDefault.png");
		previewLabel.setBackground(new Color(153, 153, 153));
		if (previewCheckBox.isSelected()){
			previewLabel.setIcon(convertImageicon);
		}else{
			previewLabel.setIcon(defaultImageicon);
		}

		previewLabel.setOpaque(true);
		getContentPane().add(previewLabel);
		previewLabel.setBounds(100, 10, 400, 300);

		currentprogressLabel.setText("현재 진행율");
		getContentPane().add(currentprogressLabel);
		currentprogressLabel.setBounds(15, 380, 100, 15);

		currentProgressBar.setValue(0);
		currentProgressBar.setStringPainted(true);
		getContentPane().add(currentProgressBar);
		currentProgressBar.setBounds(10, 400, 570, 30);

		getContentPane().add(detailprogressLabel);
		detailprogressLabel.setBounds(15, 435, 150, 15);
		getContentPane().add(horizonLine);
		horizonLine.setBounds(10, 350, 570, 10);

		poweroffCheckBox.setText("변환 작업 완료시 컴퓨터 종료");
		getContentPane().add(poweroffCheckBox);
		poweroffCheckBox.setBounds(10, 480, 185, 23);
		poweroffCheckBox.setSelected(option.getUsePoweroff());
		poweroffCheckBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				option.setUsePoweroff(poweroffCheckBox.isSelected());
			}
		});

		convertCancleButton.setText("변환 취소");
		getContentPane().add(convertCancleButton);
		convertCancleButton.setBounds(500, 480, 80, 23);
		convertCancleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancleConvert();
			}
		});

		getContentPane().add(progressspeedLabel);
		progressspeedLabel.setHorizontalAlignment(JLabel.RIGHT);
		progressspeedLabel.setBounds(10, 380, 565, 15);

		getContentPane().add(taketimeLabel);
		taketimeLabel.setHorizontalAlignment(JLabel.RIGHT);
		taketimeLabel.setBounds(10, 435, 565, 15);

		converter.addConvertPrograssListener(new ConvertPrograssListener() {
			public void convertPrograss(ConvertPrograssEvent event) {
				if (total > 1){
					currentprogressLabel.setText("현재 진행율 " + completeCount + "/" + total);
				}

				if (event.mode == Mode.Complete){
					completeCount++;
				}
				if(Double.isInfinite(event.getEstimatedTime())){
					taketimeLabel.setText("남은 작업 예상 시간 : 계산 중");
				}else{
					taketimeLabel.setText("남은 작업 예상 시간 : " + getEstimatedTime(event.getEstimatedTime()));
				}
				detailprogressLabel.setText(getCurrentWork(event.mode));
				currentProgressBar.setValue((int) event.getPrograssPercentage());
				setTitle("변환 중..  " + String.format("%.2f", event.getPrograssPercentage()) + "%");
				if (Double.isNaN(event.currentState.speed) || Double.isInfinite(event.currentState.speed)) {
					progressspeedLabel.setText("작업 속도 : 0.000x");
				} else {
					progressspeedLabel.setText("작업 속도 : " + String.format("%.3f", (event.currentState.speed)) + "x");
				}
				
				if (event.mode != Mode.Complete && event.mode != Mode.Divide){
					setPreviewLabel(event);
				}else{
					previewLabel.setIcon(defaultImageicon);
				}

				if (event.mode == Mode.Complete && completeCount > total) {
					setTitle("변환 작업 완료");
					new ConvertCompleteWindow(videoList.size(), converter.getFailCount(), option, historyList).setVisible(true);
					dispose();
				}
			}
		});

		setPreferredSize(new Dimension(600, 550));
		setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - this.getPreferredSize().getWidth() / 2),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - this.getPreferredSize().getHeight() / 2));
		setTitle("변환 중...");
		pack();

		converter.start();
	}

	/**
	 * "작업 취소" 버튼을 클릭시 진행하는 이벤트
	 */
	private void cancleConvert() {
		int res = JOptionPane.showConfirmDialog(null, "현재 변환작업이 진행중입니다." + '\n' + "변환작업을 취소하시겠습니까?", "변환작업 취소", JOptionPane.YES_NO_OPTION);
		if (res == 0) {
			converter.cancelWork();
			dispose();
		}
	}

	/**
	 * 작업중인 프레임 이름을 반환하는 메소드
	 * @param n 작업중인 프레임 번호
	 * @return 작업중인 프레임 이름
	 */
	private String getWorkingImgName(int n) {

		int a = (int) Math.log10(n);
		StringBuffer str = new StringBuffer("img");
		for (int i = 0; i < 8 - a; i++) {
			str.append("0");
		}
		str.append(n);
		str.append(".png");

		return str.toString();
	}

	/**
	 * 남은 작업 예상시간을 반환하는 메소드
	 * @param d 남은시간
	 * @return 남은 시간 포맷
	 */

	private String getEstimatedTime(double d) {
		int left = (int) d;
		int day, h, m, s;
		day = left / 86400;
		h = (left % 86400) / 3600;
		m = (left % 86400 % 3600) / 60;
		s = left % 86400 % 3600 % 60;
		if(day == 0 && h == 0){
			return m + "분 " + s + "초";
		}else if(day == 0){
			return h + "시간 " + m + "분 " + s + "초";
		}else{
			return day + "일 " + h + "시간 " + m + "분 " + s + "초";
		}
	}

	/**
	 * 현재 작업을 반환하는 메소드
	 * @param m ConvertPrograssEvent Mode
	 * @return 현재 진행중 작업
	 */
	private String getCurrentWork(ConvertPrograssEvent.Mode m) {
		switch (m) {
		case Divide:
			return "영상 분할 작업중.. (1/3)";
		case Zoom:
			return "영상 개선 작업중.. (2/3)";
		case Merge:
			return "영상 인코딩 작업중.. (3/3)";
		case Complete:
			return "작업 완료";
		default:
			return "에러";
		}
	}

	/**
	 * convertImageLabel에 부착할 이미지 갱신하는 메소드
	 * 
	 * @param event ConvertPrograssEvent 객체
	 */

	private void setPreviewLabel(ConvertPrograssEvent event) {
		if (!previewCheckBox.isSelected()){
			previewLabel.setIcon(defaultImageicon);
			return;
		}

		convertImageicon = new ImageIcon(option.getTemporaryResizeImagePath() + "/" + event.video.getVideoName() + "/" + getWorkingImgName(event.currentState.currentFrame));
		Image img = convertImageicon.getImage();
		Image resizeImg = null;
		if (convertImageicon.getIconHeight() > convertImageicon.getIconWidth()) {
			try {
				resizeImg = img.getScaledInstance(convertImageicon.getIconWidth() / (convertImageicon.getIconHeight() / 300), 300, Image.SCALE_SMOOTH);
			} catch (ArithmeticException e) {
				resizeImg = img.getScaledInstance(400, 225, Image.SCALE_SMOOTH);
			}
		} else {
			try {
				resizeImg = img.getScaledInstance(400, convertImageicon.getIconHeight() / (convertImageicon.getIconWidth() / 400), Image.SCALE_SMOOTH);
			} catch (ArithmeticException e) {
				resizeImg = img.getScaledInstance(400, 225, Image.SCALE_SMOOTH);
			}
		}
		convertImageicon = new ImageIcon(resizeImg);
		previewLabel.setIcon(convertImageicon);
	}

}