package converter;
import java.util.ArrayList;

/**
 * <b>Waifu2x</b>의 커멘드를 생성하는 클래스입니다.
 * @author admin
 */
public class Waifu2x {
	private static String waifu2xPath = "waifu2x/waifu2x.exe";
	
	/**
	 * <b>[Waifu2xPath] -t [TTA] -p [Process] --model_dir [Model] -s [scale] -n [noise] -m [mode] -o [OutputPath] -i [InputPath]</b><p>
	 * 형식의 질의를 반환합니다. 
	 * @return 질의 문자열 리스트
	 */
	public static ArrayList<String> waifu2xCommand(String inputPath, String outputPath, int noise, double scale, String model, String process){
		ArrayList<String> str = new ArrayList<String>();
		str.add(waifu2xPath);
		str.add("-p");
		str.add(process);
		str.add("--model_dir");
		str.add(model);
		str.add("-s");
		str.add(String.valueOf(scale));
		str.add("-n");
		str.add(String.valueOf(noise));
		str.add("-m");
		str.add("noise_scale");
		str.add("-o");
		str.add(outputPath);
		str.add("-i");
		str.add(inputPath);
		return str;
	}
}
