package option;
import java.io.File;

/**
 * 옵션의 기본값을 얻는 클래스 입니다.
 * @author admin
 *
 */
public class OptionDefault {
	
		/**
	    * 새롭게 옵션을 생성하고 기본값으로 지정한 뒤 반환합니다.
	    * @return 기본값으로 지정된 옵션입니다.
	    */
	   public static Option getDefaultOption(){
	      Option op = new Option();
	      
	      //경로의 기본 값은 시스템이 실행되는 위치와 그 하위폴더 temp 이다.
	      op.setResizeVideoFile(new File(System.getProperty("user.dir") + "/output/"));
	      op.setTemporaryOriginalImage(new File(System.getProperty("user.dir") + "/temp/original/"));
	      op.setTemporaryResizeImage(new File(System.getProperty("user.dir") + "/temp/resize/"));
	      
	      return op;
	   }
	   
	   public static Option getDefaultOption(Option op){
		   op.setResizeVideoFile(new File(System.getProperty("user.dir") + "/output/"));
		   op.setTemporaryOriginalImage(new File(System.getProperty("user.dir") + "/temp/original/"));
		   op.setTemporaryResizeImage(new File(System.getProperty("user.dir") + "/temp/resize/"));
		   op.setCodec(Option.Codec.X264);
		   op.setPreset(Option.Preset.SLOW);
		   op.setExtension(Option.Extension.MP4);
		   op.setKeyFrame(1);
		   op.setStartTime(0);
		   op.setWorkTime(0);
		   op.setProcess(Option.Process.CUDNN);
		   op.setMode(Option.Mode.NOISE_SCALE);
		   op.setScale(2.0);
		   op.setNoise(3);
		   op.setModel(Option.Model.PHOTO);
		   op.setQuality(Option.Quality.HIGH);
		   op.setUsePreview(true);
		   op.setUsePoweroff(false);
		   op.setRemoveOriginalVideoFile(false);
		   op.setRemoveTemporaryOriginalFile(false);
		   op.setRemoveTemporaryResizeFile(false);
		   
		   return op;
	   }
}
