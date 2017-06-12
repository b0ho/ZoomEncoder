package display;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import option.*;
import option.Option.Mode;

/**
 * 옵션을 설정하는 윈도우
 * 
 * @author kim
 */
public class OptionWindow extends JFrame {

	private static final long serialVersionUID = 7271286984175424129L;
	private Option option;
	private JButton resizedPathButton;
	private JButton resizedtempPathButton;
	private JButton setDefaultButton;
	private JButton cancelButton;
	private JButton acceptButton;
	private JButton origintempPathButton;
	private JComboBox<String> codecComboBox;
	private JComboBox<String> processComboBox;
	private JComboBox<String> noiseComboBox;
	private JComboBox<String> modelComboBox;
	private JComboBox<String> tempRemoveComboBox;
	private JComboBox<String> qualityComboBox;
	private JComboBox<String> extentionComboBox;
	private JLabel codecLabel;
	private JLabel processLabel;
	private JLabel keyframeLabel;
	private JLabel scaleLabel;
	private JLabel noiseLabel;
	private JLabel modelLabel;
	private JLabel tempRemoveLabel;
	private JLabel qualityLabel;
	private JLabel resizedPathLabel;
	private JLabel resizedtempPathLabel;
	private JLabel extensionLabel;
	private JLabel origintempPathLabel;
	private JTextField resizedPathTextfield;
	private JTextField resizedtempPathTextfield;
	private JTextField scaleTextfield;
	private JTextField keyframeTextfield;
	private JTextField origintempPathTextfield;

	public OptionWindow(Option op) {
		option = op;
		initComponents();
	}

	private void initComponents() {
		codecLabel = new JLabel();
		scaleLabel = new JLabel();
		noiseLabel = new JLabel();
		modelLabel = new JLabel();
		tempRemoveLabel = new JLabel();
		qualityLabel = new JLabel();
		resizedPathLabel = new JLabel();
		resizedtempPathLabel = new JLabel();
		extensionLabel = new JLabel();
		processLabel = new JLabel();
		keyframeLabel = new JLabel();
		codecComboBox = new JComboBox<String>();
		noiseComboBox = new JComboBox<String>();
		processComboBox = new JComboBox<String>();
		modelComboBox = new JComboBox<String>();
		tempRemoveComboBox = new JComboBox<String>();
		qualityComboBox = new JComboBox<String>();
		extentionComboBox = new JComboBox<String>();
		resizedPathTextfield = new JTextField();
		resizedtempPathTextfield = new JTextField();
		scaleTextfield = new JTextField();
		keyframeTextfield = new JTextField();
		resizedPathButton = new JButton();
		resizedtempPathButton = new JButton();
		setDefaultButton = new JButton();
		cancelButton = new JButton();
		acceptButton = new JButton();
		origintempPathTextfield = new JTextField();
		origintempPathButton = new JButton();
		origintempPathLabel = new JLabel();

		setTitle("옵션 설정");
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setPreferredSize(new Dimension(500, 450));
		this.setLocation(
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2
						- this.getPreferredSize().getWidth() / 2),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2
						- this.getPreferredSize().getHeight() / 2));

		getContentPane().setLayout(null);

		codecLabel.setText("코덱");
		getContentPane().add(codecLabel);
		codecLabel.setBounds(12, 11, 28, 17);

		processLabel.setText("프로세스");
		getContentPane().add(processLabel);
		processLabel.setBounds(12, 42, 80, 17);

		scaleLabel.setText("확대율");
		getContentPane().add(scaleLabel);
		scaleLabel.setBounds(12, 73, 42, 17);

		keyframeLabel.setText("키 프레임 간격(초)");
		getContentPane().add(keyframeLabel);
		keyframeLabel.setBounds(12, 104, 150, 17);

		noiseLabel.setText("노이즈 제거율");
		getContentPane().add(noiseLabel);
		noiseLabel.setBounds(12, 135, 84, 17);

		modelLabel.setText("영상 종류");
		getContentPane().add(modelLabel);
		modelLabel.setBounds(12, 166, 61, 17);

		tempRemoveLabel.setText("임시파일 처리");
		getContentPane().add(tempRemoveLabel);
		tempRemoveLabel.setBounds(12, 198, 89, 17);

		qualityLabel.setText("영상 품질");
		getContentPane().add(qualityLabel);
		qualityLabel.setBounds(12, 231, 61, 17);

		extensionLabel.setText("변환할 형식");
		getContentPane().add(extensionLabel);
		extensionLabel.setBounds(12, 262, 80, 17);

		resizedPathLabel.setText("변환 된 영상 경로");
		getContentPane().add(resizedPathLabel);
		resizedPathLabel.setBounds(12, 293, 152, 17);

		origintempPathLabel.setText("변환 전 임시파일 경로");
		getContentPane().add(origintempPathLabel);
		origintempPathLabel.setBounds(12, 324, 152, 17);

		resizedtempPathLabel.setText("변환 후 임시파일 경로");
		getContentPane().add(resizedtempPathLabel);
		resizedtempPathLabel.setBounds(12, 355, 152, 17);

		codecComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "x264", "xvid" }));
		getContentPane().add(codecComboBox);
		codecComboBox.setBounds(152, 8, 150, 25);

		processComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "GPU", "CPU", "CUDNN" }));
		getContentPane().add(processComboBox);
		processComboBox.setBounds(152, 39, 150, 25);

		getContentPane().add(scaleTextfield);
		scaleTextfield.setDocument(new JExtendTextField("0-9.", 4));
		scaleTextfield.setBounds(152, 71, 150, 25);

		keyframeTextfield.setDocument(new JExtendTextField("0-9", 2));
		getContentPane().add(keyframeTextfield);
		keyframeTextfield.setBounds(152, 101, 150, 25);

		noiseComboBox
				.setModel(new DefaultComboBoxModel<String>(new String[] { "노이즈 제거 없음", "최소 제거", "보통 제거", "최대 제거" }));
		getContentPane().add(noiseComboBox);
		noiseComboBox.setBounds(152, 132, 150, 25);

		modelComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "실사", "애니메이션" }));
		getContentPane().add(modelComboBox);
		modelComboBox.setBounds(152, 163, 150, 25);

		tempRemoveComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "보관", "삭제" }));
		getContentPane().add(tempRemoveComboBox);
		tempRemoveComboBox.setBounds(152, 195, 150, 25);

		qualityComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "무손실", "최고화질", "고화질", "보통화질" }));
		getContentPane().add(qualityComboBox);
		qualityComboBox.setBounds(152, 227, 150, 25);

		extentionComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "AVI", "MP4", "MKV" }));
		getContentPane().add(extentionComboBox);
		extentionComboBox.setBounds(152, 258, 150, 25);

		getContentPane().add(resizedPathTextfield);
		resizedPathTextfield.setBounds(152, 291, 248, 25);
		resizedPathTextfield.setEditable(false);

		getContentPane().add(origintempPathTextfield);
		origintempPathTextfield.setBounds(152, 322, 248, 25);
		origintempPathTextfield.setEditable(false);

		getContentPane().add(resizedtempPathTextfield);
		resizedtempPathTextfield.setBounds(152, 353, 248, 25);
		resizedtempPathTextfield.setEditable(false);

		resizedPathButton.setText("찾아보기");
		getContentPane().add(resizedPathButton);
		resizedPathButton.setBounds(407, 291, 81, 23);
		resizedPathButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser pathFileChooser = new JFileChooser();
				pathFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = pathFileChooser.showOpenDialog(null);
				if(pathFileChooser.getSelectedFile() != null && result == JFileChooser.APPROVE_OPTION){
					resizedPathTextfield.setText(pathFileChooser.getSelectedFile().toString());
					resizedPathTextfield.setToolTipText(resizedPathTextfield.getText());
				}
			}
		});

		origintempPathButton.setText("찾아보기");
		getContentPane().add(origintempPathButton);
		origintempPathButton.setBounds(407, 322, 81, 23);
		origintempPathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser pathFileChooser = new JFileChooser();
				pathFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = pathFileChooser.showOpenDialog(null);
				if(pathFileChooser.getSelectedFile() != null && result == JFileChooser.APPROVE_OPTION){
					origintempPathTextfield.setText(pathFileChooser.getSelectedFile().toString());
					origintempPathTextfield.setToolTipText(origintempPathTextfield.getText());
				}
			}
		});

		resizedtempPathButton.setText("찾아보기");
		getContentPane().add(resizedtempPathButton);
		resizedtempPathButton.setBounds(407, 353, 81, 23);
		resizedtempPathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser pathFileChooser = new JFileChooser();
				pathFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = pathFileChooser.showOpenDialog(null);
				if(pathFileChooser.getSelectedFile() != null && result == JFileChooser.APPROVE_OPTION){
					resizedtempPathTextfield.setText(pathFileChooser.getSelectedFile().toString());
					resizedtempPathTextfield.setToolTipText(resizedtempPathTextfield.getText());
				}
			}
		});

		setDefaultButton.setText("기본값으로 복원");
		setDefaultButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDefaultButtonEvent(e);
			}
		});
		
		getContentPane().add(setDefaultButton);
		setDefaultButton.setBounds(367, 410, 121, 23);

		cancelButton.setText("취소");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		getContentPane().add(cancelButton);
		cancelButton.setBounds(303, 410, 57, 23);

		acceptButton.setText("적용");
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				acceptButtonEvent(evt);
			}
		});
		getContentPane().add(acceptButton);
		acceptButton.setBounds(239, 410, 57, 23);

		pack();

		setValue();

	}

	private void setDefaultButtonEvent(ActionEvent evt) {
		option = OptionDefault.getDefaultOption(option);
		setValue();
	}

	private void acceptButtonEvent(ActionEvent evt) {
		switch (codecComboBox.getSelectedIndex()) {
		case 0:
			option.setCodec(Option.Codec.X264);
			break;
		case 1:
			option.setCodec(Option.Codec.XVID);
			break;
		}

		switch (processComboBox.getSelectedIndex()) {
		case 0:
			option.setProcess(Option.Process.GPU);
			break;
		case 1:
			option.setProcess(Option.Process.CPU);
			break;
		case 2:
			option.setProcess(Option.Process.CUDNN);
			break;
		}

		option.setScale(Double.parseDouble(scaleTextfield.getText()));

		option.setKeyFrame(Integer.parseInt(keyframeTextfield.getText()));

		option.setNoise(noiseComboBox.getSelectedIndex());

		switch (modelComboBox.getSelectedIndex()) {
		case 0:
			option.setModel(Option.Model.PHOTO);
			break;
		case 1:
			option.setModel(Option.Model.ANIME);
			break;
		}

		switch (qualityComboBox.getSelectedIndex()) {
		case 0:
			option.setQuality(Option.Quality.LOSSLESS);
			break;
		case 1:
			option.setQuality(Option.Quality.HIGHEST);
			break;
		case 2:
			option.setQuality(Option.Quality.HIGH);
			break;
		case 3:
			option.setQuality(Option.Quality.MEDIUM);
			break;
		}

		switch (tempRemoveComboBox.getSelectedIndex()) {
		case 0:
			option.setRemoveTemporaryOriginalFile(false);
			option.setRemoveTemporaryResizeFile(false);
			break;
		case 1:
			option.setRemoveTemporaryOriginalFile(true);
			option.setRemoveTemporaryResizeFile(true);
			break;
		}

		option.setTemporaryOriginalImage(new File(origintempPathTextfield.getText()));
		option.setTemporaryResizeImage(new File(resizedtempPathTextfield.getText()));
		option.setResizeVideoFile(new File(resizedPathTextfield.getText()));

		switch (extentionComboBox.getSelectedIndex()) {
		case 0:
			option.setExtension(Option.Extension.AVI);
			break;
		case 1:
			option.setExtension(Option.Extension.MP4);
			break;
		case 2:
			option.setExtension(Option.Extension.MKV);
			break;
		}

		if (option.getNoise() == 0 && option.getScale() != 0.2)
			option.setMode(Mode.SCALE);
		else if (option.getNoise() != 0 && option.getScale() == 0.2)
			option.setMode(Mode.NOISE);
		else
			option.setMode(Mode.NOISE_SCALE);

		OptionFile.writeOptionFile(option);

		dispose();
	}

	public void setValue() {
		switch (option.getCodec()) {
		case "libx264":
			codecComboBox.setSelectedIndex(0);
			break;
		case "libxvid":
			codecComboBox.setSelectedIndex(1);
			break;
		}

		switch (option.getProcess()) {
		case "gpu":
			processComboBox.setSelectedIndex(0);
			break;
		case "cpu":
			processComboBox.setSelectedIndex(1);
			break;
		case "cudnn":
			processComboBox.setSelectedIndex(2);
			break;
		}

		scaleTextfield.setText(String.valueOf(option.getScale()));
		keyframeTextfield.setText(String.valueOf(option.getKeyFrame()));
		noiseComboBox.setSelectedIndex(option.getNoise());

		switch (option.getModel()) {
		case "waifu2x/models/upconv_7_photo":
			modelComboBox.setSelectedIndex(0);
			break;
		case "waifu2x/models/upconv_7_anime_style_art_rgb":
			modelComboBox.setSelectedIndex(1);
			break;

		}

		if (option.getRemoveTemporaryResizeFile())
			tempRemoveComboBox.setSelectedIndex(1);
		else
			tempRemoveComboBox.setSelectedIndex(0);

		switch (option.getQuality()) {
		case 0:
			qualityComboBox.setSelectedIndex(0);
			break;
		case 15:
			qualityComboBox.setSelectedIndex(1);
			break;
		case 19:
			qualityComboBox.setSelectedIndex(2);
			break;
		case 23:
			qualityComboBox.setSelectedIndex(3);
			break;
		}

		switch (option.getExtension()) {
		case ".avi":
			extentionComboBox.setSelectedIndex(0);
			break;
		case ".mp4":
			extentionComboBox.setSelectedIndex(1);
			break;
		case ".mkv":
			extentionComboBox.setSelectedIndex(2);
			break;
		}
		resizedPathTextfield.setText(option.getResizeVideoFilePath());
		resizedtempPathTextfield.setText(option.getTemporaryResizeImagePath());
		origintempPathTextfield.setText(option.getTemporaryOriginalImagePath());
		resizedtempPathTextfield.setToolTipText(resizedtempPathTextfield.getText());
		resizedPathTextfield.setToolTipText(resizedPathTextfield.getText());
		origintempPathTextfield.setToolTipText(origintempPathTextfield.getText());
	}
}