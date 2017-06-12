package display;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import history.*;
import option.*;
import util.ComputerPowerOff;

/**
 * 프로그램 변환 완료 윈도우
 * @author 김선일
 */
public class ConvertCompleteWindow extends JDialog {
	
	private static final long serialVersionUID = 3021062009473906477L;
	/** 작업 완료 */
	private int timeleft;
	/** 작업 완료시 컴퓨터 종료를 위한 타이머 */
	private Timer timer;
	/** 옵션 */
	private Option option;
	/** 히스토리 리스트 */
	private HistoryList historyList;
	/** 영상폴더보기 버튼 */
	private JButton videofolderButton;
	/** 닫기 버튼 */
	private JButton closeButton;
	/** 작업내역보기 버튼 */
	private JButton historyButton;
	/** 컴퓨터 종료 체크박스 */
	private JCheckBox poweroffCheckBox;
	/** 결과 표시 라벨 */
	private JLabel resultLabel;
	/** 변환 성공 영상 수 */
	private int success;
	/** 변환 실패 영상 수 */
	private int fail;

	/**
	 * 프로그램 변환 완료 윈도우
	 * @param success 변환 성공 영상 수
	 * @param fail 변환 실패 영상 수
	 */
	public ConvertCompleteWindow(int success, int fail, Option op, HistoryList hl) {
		super();
		setModal(true);
		option = op;
		historyList = hl;
		this.success = success;
		this.fail = fail;
		initComponents();
		
	}

	private void initComponents() {
		timeleft = 300;
		ActionListener taskPerformer = new ActionListener() {
			/** 컴퓨터 종료 옵션 활성화시 이벤트 */
			public void actionPerformed(ActionEvent evt) {
				if (poweroffCheckBox.isSelected()) {
					timeleft--;
					poweroffCheckBox.setText(timeleft + "초 후에 컴퓨터를 종료합니다");
					if (timeleft == -1) {
						ComputerPowerOff.powerOff(0);
						timer.stop();
					}
				}
			}
		};

		timer = new Timer(1000, taskPerformer);
		resultLabel = new JLabel();
		videofolderButton = new JButton();
		closeButton = new JButton();
		historyButton = new JButton();
		poweroffCheckBox = new JCheckBox();

		setTitle("작업 완료");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(null);
		
		this.addWindowListener(new WindowAdapter() {
			/** 우측 상단 X버튼 클릭시 발생하는 이벤트 */
			public void windowClosing(WindowEvent e) {
				if (poweroffCheckBox.isSelected()) {
						timer.stop();
						int res = JOptionPane.showConfirmDialog(null,
								"컴퓨터 종료 옵션이 활성화되어 있습니다" + '\n' + "컴퓨터 종료 옵션을 비활성화하고 이 창을 닫으시겠습니까?", "컴퓨터 종료 옵션 활성화 됨",
								JOptionPane.OK_CANCEL_OPTION);
						if (res == 0){
							dispose();
						}else{
							timer.start();
						}
				}else{
					dispose();
				}
			}
		});

		resultLabel.setText("<html>전체 영상 개수 : " + Integer.toString(success + fail) + "<br>변환 성공한 영상 개수 : "
				+ Integer.toString(success) + "<br>변환 실패한 개수 : " + Integer.toString(fail) + "</html>");
		resultLabel.setOpaque(true);
		getContentPane().add(resultLabel);
		resultLabel.setBounds(100, 30, 200, 50);
		videofolderButton.setText("영상 폴더 열기");
		getContentPane().add(videofolderButton);
		videofolderButton.setBounds(25, 147, 113, 23);
		videofolderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(new File(option.getResizeVideoFilePath()));
				} catch (IOException e1) {
					System.out.println("영상 폴더 열기 실패");
				}
			}
		});

		closeButton.setText("닫기");
		getContentPane().add(closeButton);
		closeButton.setBounds(265, 147, 57, 23);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (poweroffCheckBox.isSelected()) {
					timer.stop();
					int res = JOptionPane.showConfirmDialog(null,
							"컴퓨터 종료 옵션이 활성화되어 있습니다" + '\n' + "컴퓨터 종료 옵션을 비활성화하고 이 창을 닫으시겠습니까?", "컴퓨터 종료 옵션 활성화 됨",
							JOptionPane.OK_CANCEL_OPTION);
					if (res == 2) {
						timer.start();
						return;
					}
				}
				dispose();
			}
		});
		
		
		historyButton.setText("작업 내역 확인");
		getContentPane().add(historyButton);
		historyButton.setBounds(145, 147, 113, 23);
		historyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new HistoryWindow(historyList).setVisible(true);
			}
		});

		if (option.getUsePoweroff()) {
			poweroffCheckBox.setText(timeleft + "초 후에 컴퓨터를 종료합니다");
			getContentPane().add(poweroffCheckBox);
			poweroffCheckBox.setBounds(52, 112, 300, 21);
			poweroffCheckBox.setSelected(option.getUsePoweroff());
		}
		setPreferredSize(new Dimension(360, 225));
		timer.start();
		setLocation(
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2
						- this.getPreferredSize().getWidth() / 2),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2
						- this.getPreferredSize().getHeight() / 2));
		pack();
	}
}