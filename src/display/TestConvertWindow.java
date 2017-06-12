package display;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import converter.*;
import converter.Converter.Tmode;
import converter.event.ConvertPrograssEvent;
import converter.event.ConvertPrograssEvent.Mode;
import converter.event.ConvertPrograssListener;
import history.HistoryList;
import loader.Video;
import option.*;

/**
 * 프로그램 테스트 변환 윈도우.
 * 
 * @author 김선일
 */
public class TestConvertWindow extends JDialog {

	private static final long serialVersionUID = 2129406127874730040L;
	/**	옵션 */
	private Option option;
	/** 히스토리 리스트 */
	private HistoryList historylist;
	/** 테스트 변환할 비디오 객체 */
	private Video video;
	/** 컨버터 */
	private Converter converter;
	/** 테스트 변환 버튼 */
	private JButton testconvertButton;
	/** PSNR 측정 버튼 */
	private JButton testPSNRButton;
	/** 테스트 모드 설정 콤보박스 */
	private JComboBox<String> testpartComboBox;
	/** 테스트 프레임 수 라벨 */
	private JLabel testframeLabel;
	/** 테스트 프레임 위치 라벨 */
	private JLabel testpartLabel;
	/** 프레임 위치 라벨 */
	private JLabel framelocationLabel;
	/** 테스트 프레임 수 텍스트필드 */
	private JTextField testframeTextField;
	/** 프레임 위치 텍스트필드 */
	private JTextField framelocationTextField;

	/**
	 * 프로그램 테스트변환 윈도우 생성자
	 * @param op 옵션
	 * @param v 비디오
	 * @param hl 히스토리 리스트
	 */
	public TestConvertWindow(Option op, Video v, HistoryList hl) {
		option = op;
		video = v;
		historylist = hl;
		converter = new Converter(video, option, historylist);
		initComponents();
	}

	/**
	 * 컴포넌트 초기화 메서드</br>
	 * 컴포넌트 레이아웃 설정 및 버튼 이벤트 등록
	 */
	private void initComponents() {
		// 컴포넌트 초기화
		testframeLabel = new JLabel();
		testpartLabel = new JLabel();
		testconvertButton = new JButton();
		testPSNRButton = new JButton();
		testframeTextField = new JTextField();
		testpartComboBox = new JComboBox<>();
		framelocationTextField = new JTextField();
		framelocationLabel = new JLabel();

		// 우측상단 X버튼을 클릭시에도 사용자가 지정한 이벤트 처리기만 작동하도록 함.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setLayout(null);

		// 레이아웃 및 위치 설정
		setTitle("테스트 변환");
		testframeLabel.setText("테스트 프레임 수");
		add(testframeLabel);
		testframeLabel.setBounds(12, 13, 100, 15);

		testpartLabel.setText("테스트 할 영역");
		add(testpartLabel);
		testpartLabel.setBounds(12, 44, 100, 15);

		testconvertButton.setText("샘플 및 시간 측정");
		testconvertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				testconvertButtonEvent(evt);
			}
		});
		add(testconvertButton);
		testconvertButton.setBounds(40, 110, 150, 75);
		
		testPSNRButton.setText("PSNR 측정");
		testPSNRButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				testPSNRButtonEvent(evt);
			}
		});
		add(testPSNRButton);
		testPSNRButton.setBounds(210, 110, 150, 75);

		framelocationTextField.setText(Integer.toString(option.getStartTime()));
		framelocationTextField.setDocument(new JExtendTextField("0-9.", 6));
		framelocationTextField.setEnabled(false);
		add(framelocationTextField);
		framelocationTextField.setBounds(122, 65, 100, 26);

		framelocationLabel.setText("프레임 위치 (초)");
		framelocationLabel.setEnabled(false);
		add(framelocationLabel);
		framelocationLabel.setBounds(12, 71, 100, 15);

		testframeTextField.setText(Integer.toString(option.getTestFrame()));
		testframeTextField.setDocument(new JExtendTextField("0-9.", 3));
		add(testframeTextField);
		testframeTextField.setBounds(122, 8, 50, 26);
		testpartComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "랜덤 프레임", "프레임 지정" }));
		testpartComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				testpartComboBoxEvent(evt);
			}
		});
		if (option.getTestOption() == Option.TestOption.RANDOM) {
			testpartComboBox.setSelectedIndex(0);
			framelocationTextField.setEnabled(false);
			framelocationLabel.setEnabled(false);
		} else {
			testpartComboBox.setSelectedIndex(1);
			framelocationTextField.setEnabled(true);
			framelocationLabel.setEnabled(true);
		}

		testframeTextField.setText(String.valueOf(option.getTestFrame()));
		framelocationTextField.setText(String.valueOf(option.getStartTime()));

		add(testpartComboBox);
		testpartComboBox.setBounds(122, 41, 150, 21);

		this.addWindowListener(new WindowAdapter() {
			/** 우측 상단 X버튼 클릭시 발생하는 이벤트 */
			public void windowClosing(WindowEvent e) {

				int res = JOptionPane.showConfirmDialog(null, "테스트를 종료하시겠습니까?", "변환작업 종료", JOptionPane.YES_NO_OPTION);
				if (res == 0) {
					converter.cancelWork();
					dispose();
				}
			}
		});
		setPreferredSize(new Dimension(400, 230));
		setLocation(
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - getPreferredSize().getWidth() / 2),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2
						- getPreferredSize().getHeight() / 2));
		pack();
	}

	/**
	 * 테스트 영역 콤보박스 이벤트 선택된 항목에 따라 프레임 위치 컴포넌트들을 활성화/비활성화 함.
	 * @param evt
	 */
	private void testpartComboBoxEvent(ItemEvent evt) {
		if (testpartComboBox.getSelectedIndex() == 0) {
			framelocationTextField.setEnabled(false);
			framelocationLabel.setEnabled(false);
		} else {
			framelocationTextField.setEnabled(true);
			framelocationLabel.setEnabled(true);
		}
	}

	/**
	 * 테스트 변환 버튼을 클릭시 발생하는 이벤트
	 * @param evt 액션이벤트
	 */
	private void testconvertButtonEvent(ActionEvent evt) {
		converter = new Converter(video, option, historylist);
		converter.tmode = Tmode.Time;
		
		if(testframeTextField.getText().equals("")) testframeTextField.setText("15");
		if(framelocationTextField.getText().equals("")) framelocationTextField.setText("0");
		
		option.setTestFrame(Integer.parseInt(testframeTextField.getText()));
		
		switch (testpartComboBox.getSelectedIndex()) {
		case 0:
			option.setTestOption(Option.TestOption.RANDOM);
			break;
		case 1:
			option.setTestOption(Option.TestOption.TIME);
			option.setStartTime(Integer.parseInt(framelocationTextField.getText()));
			break;
		}
		int totalFrame = (int) (video.getDuration() * video.getFramerate()); // 토탈 프레임 계산
		
		if (Integer.parseInt(testframeTextField.getText()) > 120 || Integer.parseInt(testframeTextField.getText()) < 15) {
			JOptionPane.showMessageDialog(this, "15~120사이의 값을 입력해 주세요", "테스트 프레임 수 에러.", JOptionPane.ERROR_MESSAGE);
			return;
		} else if (totalFrame < Integer.parseInt(testframeTextField.getText())) {
			JOptionPane.showMessageDialog(this, "입력한 프레임보다 영상이 짧습니다.", "테스트 프레임 수 에러.", JOptionPane.ERROR_MESSAGE);
			return;
		} else if (testpartComboBox.getSelectedIndex() == 1 && Integer.parseInt(framelocationTextField.getText()) > video.getDuration()) {
			JOptionPane.showMessageDialog(this, "입력한 위치보다 영상이 짧습니다.", "테스트 위치 에러.", JOptionPane.ERROR_MESSAGE);
			return;
		}
		OptionFile.writeOptionFile(option);
		converter.start();
		testconvertButton.setEnabled(false);
		testPSNRButton.setEnabled(false);
		converter.addConvertPrograssListener(new ConvertPrograssListener() {
			public void convertPrograss(ConvertPrograssEvent event) {
				testconvertButton.setText("");
				testconvertButton.setIcon(new ImageIcon("res/loading.gif"));
				setTitle("샘플 추출 및 시간 측정 중..");
				if (event.mode == Mode.Complete) {
					setTitle("샘플 추출 및 시간 측정 완료");
					TestResult result = converter.getTestResult();
					try {
						Desktop.getDesktop().open(new File(option.getTemporaryResizeImagePath() + "/Test/" + video.getVideoName()));
					} catch (IOException e) {
						System.out.println("변환 폴더 열기 실패");
					}
					JOptionPane.showMessageDialog(getParent(),
							"샘플 추출 및 시간 측정이 완료되었습니다." + '\n' + result.getEstimatedTimeFormat() + '\n',
							"샘플 추출 및 시간 측정 완료.", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
			}
		});
	}
	
	/**
	 * PSNR 측정 버튼을 클릭시 발생하는 이벤트
	 * @param evt 액션이벤트
	 */
	private void testPSNRButtonEvent(ActionEvent evt) {
		converter = new Converter(video, option, historylist);
		converter.tmode = Tmode.PSNR;
		
		converter.start();
		converter.addConvertPrograssListener(new ConvertPrograssListener() {
			double n_psnr, z_psnr = 0;
			public void convertPrograss(ConvertPrograssEvent event) {
				testPSNRButton.setText("");
				testPSNRButton.setIcon(new ImageIcon("res/loading.gif"));

				testPSNRButton.setEnabled(false);
				testconvertButton.setEnabled(false);
				setTitle("PSNR 측정 중..");
				if (event.mode == Mode.Complete) {
					setTitle("PSNR 측정 완료");
					TestResult result = converter.getTestResult();
					for (int i = 0; i < result.getN_Psnr().length; i++){
						n_psnr += result.getN_Psnr()[i];
					}
					for (int i = 0; i < result.getZ_Psnr().length; i++){					
						z_psnr += result.getZ_Psnr()[i];
					}
					JOptionPane.showMessageDialog(getParent(), "일반 확대 시 PSNR\n     " + String.format("%.3f", (n_psnr / 5)) + "db" + '\n' + "Zoom Encoder 사용 시 PSNR\n     " + String.format("%.3f", (z_psnr / 5)) + "db" + "\n\n※ PSNR 수치는 참고용 입니다.",
							"PSNR 측정 완료", JOptionPane.INFORMATION_MESSAGE);
					dispose();
					testPSNRButton.setIcon(null);
					testPSNRButton.setText("PSNR 측정");
					testPSNRButton.setEnabled(true);
					testconvertButton.setEnabled(true);
				}
			}
		});
	}
}