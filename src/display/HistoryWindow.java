package display;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import history.*;


/**
 * 프로그램의 히스토리 윈도우.
 * @author kim
 */
public class HistoryWindow extends JDialog {

	private static final long serialVersionUID = 723869079524338929L;
	private HistoryList hisList;
	private JButton closeButton;
	private JButton deleteallButton;
	private JButton deletecomButton;
	private ArrayList<HistoryPanel> historyPanelList;
	private JScrollPane historyListScroll;
	private JPanel scrollViewport;

	/** 프로그램 히스토리 윈도우 생성자. */
	public HistoryWindow(HistoryList hl) {
		hisList = hl;
		setModal(true);
		initComponents();
	}
	
	/** 컴포넌트 초기화 */
	private void initComponents() {
		closeButton = new JButton();
		deleteallButton = new JButton();
		deletecomButton = new JButton();
		historyListScroll = new JScrollPane();
		scrollViewport = new JPanel();
		historyPanelList = new ArrayList<HistoryPanel>();
		
		//레이아웃 설정
		getContentPane().setLayout(null);
		this.setTitle("작업 히스토리");
		setPreferredSize(new Dimension(450, 550));
		this.setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - this.getPreferredSize().getWidth() / 2), 
				(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - this.getPreferredSize().getHeight() / 2));
		setResizable(false);

		closeButton.setText("닫기");
		getContentPane().add(closeButton);
		closeButton.setBounds(378, 485, 60, 30);
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		deletecomButton.setText("완료 내역 제거");
		getContentPane().add(deletecomButton);
		deletecomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hisList.deleteCompleteHistory();
				HistoryFile.writeHistoryFile(hisList);
				dynamicRedrawPanel();
				repaint();
			}
		});
		deletecomButton.setBounds(12, 485, 120, 30);

		deleteallButton.setText("모든 내역 제거");
		getContentPane().add(deleteallButton);
		deleteallButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hisList.clear();
				HistoryFile.writeHistoryFile(hisList);
				dynamicRedrawPanel();
				repaint();

			}
		});
		deleteallButton.setBounds(142, 485, 120, 30);
		
		//스크롤 패널 설정
		scrollViewport.setLayout(null);
		dynamicRedrawPanel();
		historyListScroll.setBounds(12, 10, 425, 470);
		historyListScroll.setViewportView(scrollViewport);
		historyListScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		historyListScroll.getVerticalScrollBar().setUnitIncrement(15);
		getContentPane().add(historyListScroll);
		
		pack();
	}
	
	/**	패널들의 세로 크기에 따라 스크롤 길이 동적 설정 */
	private void dynamicResizePanel(){
		int hei = 0;
		for (int i = 0; i < historyPanelList.size(); i++) {
			HistoryPanel h = historyPanelList.get(i);
			h.setLocation(0, hei);
			hei += h.getHeight() + 5;
		}
		scrollViewport.setPreferredSize(new Dimension(425, hei));
	}
	
	/** 패널 개수에 따라 표시되는 목록 설정 */
	private void dynamicRedrawPanel(){
		int hei = 0;
		historyPanelList.clear();
		scrollViewport.removeAll();
		for (int i = 0; i < hisList.size(); i++) {
			HistoryPanel h = new HistoryPanel(hisList.get(i));
			h.setLocation(0, hei);
			h.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					dynamicResizePanel();
				}
			});
			hei += h.getHeight() + 5;
			scrollViewport.add(h);
			historyPanelList.add(h);
		}
		scrollViewport.setPreferredSize(new Dimension(425, hei));
	}
}
