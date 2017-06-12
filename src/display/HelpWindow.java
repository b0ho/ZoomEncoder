package display;

import java.awt.*;

import javax.swing.*;

public class HelpWindow extends JDialog {

	private static final long serialVersionUID = 5477539056779393257L;
	/** 도움말 라벨을 부착할 스크롤팬 */
	private JScrollPane helpScrollPane;
	/** 도움말 라벨 */
	private JLabel helpLabel;
	/** 도움말 라벨에 설정할 문자열. html기준으로 작성됨. */
	private StringBuffer helpText;

	/** 프로그램 도움말 윈도우 생성자 */
	public HelpWindow() {
		initComponents();

	}

	/** 컴포넌트 초기화 메서드 */
	void initComponents() {
		helpText = new StringBuffer();
		setLayout(new CardLayout());
		setTitle("도움말");
		setPreferredSize(new Dimension(500, 500));
		this.setLocation(
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2
						- this.getPreferredSize().getWidth() / 2),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2
						- this.getPreferredSize().getHeight() / 2));

		helpText = new StringBuffer(
				"<html>"
				+ "<br><h2><b>Zoom Encoder</b></h2>"
				+ "<br>버전 : 1.0"
				+ "<br><br>"
				+ "<br>----------------------------------------------------------------------------------"
				+ "<br><h2><b>사용 방법</b></h2>"
				+ "<br>'불러오기' 버튼을 클릭하여 영상을 로드할 수 있습니다."
				+ "<br>'옵션'을 통해 변환 옵션을 선택할 수 있습니다."
				+ "<br>'변환하기'를 통해 불러온 영상의 화질을 개선시킬 수 있습니다."
				+ "<br>'테스트변환'을 통해 일부 프레임의 샘플 변환 결과를 확인하거나 작업 예상시간,"
				+ "<br>PSNR을 측정할 수 있습니다."
				+ "<br>'작업히스토리'를 통해 이전의 작업 목록을 확인할 수 있습니다."
				+ "<br>----------------------------------------------------------------------------------"
				+ "<br><h2><b>구동 사양</b></h2>"
				+ "<br><b>운영체제</b> : Windows 7, 8, 8.1, 10 (64bit)"
				+ "<br><b>CPU</b> : 3.0Ghz 이상의 듀얼코어 CPU"
				+ "<br><b>RAM</b> : 4GB 이상 / (권장 8GB 이상)"
				+ "<br><b>그래픽카드</b> : nVidia의 CUDA 7.5, Compute Capability 3.0 이상 지원 카드"
				+ "<br>GeForce 6 시리즈 이상 필요 / 7 시리즈 이상 권장"
				+ "<br><b>HDD</b> : 10GB 이상 / (영상 크기에 따라 더 필요할 수 있습니다.)"
				+ "<br>----------------------------------------------------------------------------------"
				+ "<br><h2><b>옵션 설명</b></h2>"
				+ "<br><b>코덱</b> : 변환 영상의 코덱을 뜻합니다."
				+ "<br><b>프로세스</b> : 영상 변환 방법을 지정합니다."
				+ "<br>nVidia 그래픽카드 사용자만 GPU, CUDNN 옵션 지정이 가능합니다."
				+ "<br><b>확대율</b> : 영상의 확대율을 지정합니다. 가로 세로가 확대율만큼 늘어납니다."
				+ "<br><b>키 프레임 간격</b> : 영상의 키 프레임 삽입 간격을 지정합니다."
				+ "<br><b>노이즈 제거율</b> : 영상의 노이즈를 제거하는 정도를 지정합니다."
				+ "<br>노이즈가 거의 없다면 이 옵션을 낮게 지정하십시오."
				+ "<br>노이즈가 많다면 최대 제거를 권장합니다."
				+ "<br><b>영상 종류</b> : 실사/애니메이션을 선택하여 지정하십시오."
				+ "<br><b>임시파일 처리</b> : 작업 완료 후 임시파일을 제거할지 여부입니다."
				+ "<br>작업을 이어서 하고자 한다면 이 옵션을 보관으로 두십시오."
				+ "<br><b>영상 품질</b> : 변환 영상의 압축 정도를 지정합니다. 무손실은 압축하지 않습니다."
				+ "<br>고화질 또는 최고화질을 권장합니다."
				+ "<br><b>테스트 프레임 수</b> : 샘플을 추출할 프레임의 수를 지정합니다."
				+ "<br>이 값이 크면 많은 샘플을 얻을 수 있습니다."
				+ "<br><b>테스트 할 영역</b> : 특정 구간만 샘플을 추출할지, 영상 전체에서 추출할지 지정합니다."
				+ "<br><b>프레임 위치</b> : 특정 구간 추출인 경우 구간의 시작지점을 지정합니다."
				+ "<br>----------------------------------------------------------------------------------"
				+ "<br><h2><b>특징</b></h2>"
				+ "<br>인코딩 작업은 사용자 요청으로 중단된 경우 이어서 다시할 수 있습니다."
				+ "<br>CPU 및 GPU 사용량이 높으므로 게임 등 많은 자원이 필요한 프로그램과 같이"
				+ "<br>구동하지 마십시오."
				+ "<br>용량이 부족한 경우 작업이 완료되지 않습니다."
				+ "<br>작업에 앞서 충분한 용량 확보 후 작업을 진행해 주십시오."
				+ "<br>----------------------------------------------------------------------------------"
				+ "<br><h2><b>라이센스</b></h2>"
				+ "<br>Waifu2x Copyright (C) 2015 nagadomi"
				+ "<br>Waifu2x-Caffe Copyright (C) 2015 lltcggie"
				+ "<br>FFMpeg"
				+ "<br>FFProbe"
				+ "<br>Caffe"
				+ "<br>이 프로그램은 LGPL, MIT 및 BSD 2-Clause 라이센스를 따릅니다."
				+ "<br>LICENSE 파일을 참고하십시오."
				+ "<br>----------------------------------------------------------------------------------"
				+ "<br><h2><b>저작권</b></h2>"
				+ "<br>Copyright (C) 2017 JNU"
				+ "<br></html>");
		helpLabel = new JLabel(helpText.toString());
		helpScrollPane = new JScrollPane(helpLabel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		helpScrollPane.getVerticalScrollBar().setUnitIncrement(15);
		add(helpScrollPane);

		setModal(true);
		pack();
	}

}
