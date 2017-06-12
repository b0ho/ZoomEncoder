package display;

import java.util.ArrayList;
import javax.swing.text.*;

/**
 * JTextField를 보조하는 클래스 입니다.
 * @author 강승민
 *
 */
public class JExtendTextField extends PlainDocument {
	private static final long serialVersionUID = -2596672324494951659L;
	/** 차단 또는 허용할 문자를 담고있는 규격화된 문자열 입니다. */
	private String restrict = null;
	
	/** 차단 또는 허용할 문자를 담고있는 규격화된 문자열을 처리하는 배열 입니다. */
	private char[] restrictSet;
	/** 허용할 문자를 담는 배열입니다. */
	private ArrayList<Character> permissionChar = new ArrayList<Character>();
	/** 허용할 문자의 범위를 지정하는 배열의 시작 문자입니다. */
	private ArrayList<Character> permissionCharStart = new ArrayList<Character>();
	/** 허용할 문자의 범위를 지정하는 배열의 끝 문자입니다. */
	private ArrayList<Character> permissionCharEnd = new ArrayList<Character>();
	/** 차단할 문자를 담는 배열입니다. */
	private ArrayList<Character> blockChar = new ArrayList<Character>();
	/** 차단할 문자의 범위를 지정하는 배열의 시작 문자입니다. */
	private ArrayList<Character> blockCharStart = new ArrayList<Character>();
	/** 차단할 문자의 범위를 지정하는 배열의 끝 문자입니다. */
	private ArrayList<Character> blockCharEnd = new ArrayList<Character>();
	
	/** 입력 가능한 문자의 수를 나타냅니다. */
	private int limit = 10;
	
	/**
	 * 입력 가능한 문자열 길이 10, 모든 문자 입력 가능합니다.
	 */
	public JExtendTextField(){
		super();
	}
	/**
	 * 입력 가능한 문자열 길이 n, 모든 문자 입력 가능합니다.
	 * @param limit 제한 길이
	 */
	public JExtendTextField(int limit){
		setLimit(limit);
	}
	/**
	 * 입력 가능한 문자열 길이 10, restrict 규격에 따라 허용/차단 문자열이 정해집니다.
	 * @param restrict 규격화된 문자열
	 */
	public JExtendTextField(String restrict){
		setRestrict(restrict);
	}
	
	/**
	 * 입력 가능한 문자열 길이 n, restrict 규격에 따라 허용/차단 문자열이 정해집니다.
	 * @param restrict 규격화된 문자열
	 * @param limit 제한 길이
	 */
	public JExtendTextField(String restrict, int limit){
		setRestrict(restrict);
		setLimit(limit);
	}
	
	/**
	 * 입력 가능한 길이를 반환합니다.
	 * @return 제한 길이
	 */
	public int getLimit(){
		return limit;
	}
	/**
	 * 입력 가능한 문자열의 길이를 설정합니다.
	 * @param limit 길이를 설정합니다. 1 이상의 값이어야 합니다.
	 */
	public void setLimit(int limit){
		this.limit = Math.abs(limit);
	}
	
	/**
	 * 허용/차단된 문자열 식을 반환합니다.
	 * @return 문자열 식
	 */
	public String getRestrict(){
		return restrict;
	}
	/**
	 * 문자열의 식에 따라 문자를 허용/차단 시킬 수 있습니다.<p>
	 * <div><b><span style="font-size: 12pt;">문자의 입력을 차단,허용을 하는 Restrict에 대한 상세 설명</span></b></div><div><br></div><div>JExtendTextField 안에 존재하는 restrict 라는 문자열 변수는 특정한 규격에 따라 일부 문자만 입력되도록 하거나 혹은 특정 문자를 차단할 수 있도록 합니다.</div><div><br></div><div>생성자에서 문자열을 전달하거나 setRestrict 를 통해 설정할 수 있습니다.</div><div><br></div><div>다음의 예제는 영문 소문자만 입력할 수 있도록 합니다. 하이픈 ('-') 을 입력하면 문자의 범위를 지정할 수 있습니다.</div><div><br></div><div>setRestrict("a-z")</div><div><br></div><div><br></div><div>다음의 예제는 영문 대,소문자 및 숫자와 공백을 입력할 수 있도록 합니다.</div><div><br></div><div>setRestrict("A-Za-z0-9 ")</div><div><br></div><div><br></div><div>다음의 예제는 한글만 입력할 수 있도록 합니다. 한글의 경우 입력 방식의 차이로 ㄱ-ㅎ 은 필히 추가해야하는 문제가 있습니다.</div><div><br></div><div>setRestrict("ㄱ-ㅎ가-힣")</div><div><br></div><div><br></div><div>다음의 예제는 특수문자 (! @ ^ ( ) &lt; &gt; \ - _) 만 입력할 수 있도록 합니다.</div><div><br></div><div>setRestrict("!@\\^()&lt;&gt;\\\\-_")</div><div><br></div><div>특수문자 - 및 ^ 는 문자열 규칙에 이용되어지므로 특정된 두 문자를 차단하고 싶을 경우에는 백슬래시(\\)를 붙여서 입력합니다.</div><div><br></div><div><br></div><div>다음의 예제는 영문 대문자만 입력되도록 하되 문자 S는 차단합니다. 캐럿('^') 이후의 모든 문자들은 차단 문자로 등록 되어집니다.</div><div><br></div><div>setRestrict("A-Z^S")</div><div><br></div><div><br></div><div>다음의 예제는 모든 문자의 입력을 허용하지만 특수문자 ^와 &amp;는 차단합니다.</div><div><br></div><div>setRestrict("^\\^&amp;")</div><div><br></div><div><br></div><div>다음의 예제는 직접 유니코드의 코드를 입력하여 문자열을 허용 차단하는 방식입니다. 소문자 a ~ g 까지 입력 받지만 소문자 e는 차단합니다.</div><div><br></div><div>setRestrict("\u0061-\u0067^\u0065")</div><div><br></div><div><br></div><div>restrict 가 null이거나 공백인 경우에는 모든 입력값을 허용합니다. 어떠한 문자도 입력받고 싶지 않다면 다음의 코드를 사용합니다.</div><div><br></div><div>setRestrict("^\u0000-\uFFFF")</div><div><br></div><div><br></div><div>restrict는 일반적으로 허용보다는 차단의 우선순위가 더 높습니다. 특정 문자가 허용과 차단에 모두 들어있을 경우 해당 문자는 차단됩니다.</div><div>setRestrict("A-Z^S")</div><div>위 코드는 대문자 A부터 Z까지의 입력을 허용하므로 대문자 S 역시 허용 문자에 포함되어지나 차단문자에도 들어있으므로 결과적으로는 입력이 차단 됩니다.</div>
	 * @param rest 규격화된 문자열 입니다.
	 */
	public void setRestrict(String rest){
		restrict = rest;
		
		if(restrict != null){
			restrictSet = restrict.toCharArray();
			int len = restrictSet.length;
			boolean blocked = false;
			for(int i = 0 ; i < len ; i++){
				if(restrictSet[i] == '^'){
					if(i > 0){
						if(restrictSet[i-1] == '\\'){
							if(!blocked){
								permissionChar.add(restrictSet[i]);
							}else{
								blockChar.add(restrictSet[i]);
							}
							continue;
						}
					}
					blocked = true;
					continue;
				}
				if(!blocked){
					if(restrictSet[i] == '-' && i != 0 && i != len-1){
						if(restrictSet[i-1] == '\\'){
							permissionChar.add(restrictSet[i]);
							continue;
						}
						permissionCharStart.add(restrictSet[i-1]);
						permissionCharEnd.add(restrictSet[i+1]);
					}else{
						if(restrictSet[i] == '\\' && i < len-1){
							if(restrictSet[i+1] != '-' && restrictSet[i+1] != '^'){
								permissionChar.add(restrictSet[i]);
							}
						}else{
							permissionChar.add(restrictSet[i]);
						}
					}
				}else{
					if(restrictSet[i] == '-' && i != 0 && i != len-1){
						if(restrictSet[i-1] == '\\'){
							blockChar.add(restrictSet[i]);
							continue;
						}
						blockCharStart.add(restrictSet[i-1]);
						blockCharEnd.add(restrictSet[i+1]);
					}else{
						if(restrictSet[i] == '\\' && i < len-1){
							if(restrictSet[i+1] != '-' && restrictSet[i+1] != '^'){
								blockChar.add(restrictSet[i]);
							}
						}else{
							blockChar.add(restrictSet[i]);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 등록된 JTextField에 문자가 입력될 경우 발생하는 메소드 입니다.
	 */
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException{
		if(str == null) return;
		
		boolean pass = true;
		if(restrict != null){
			pass = false;
			for(int i = 0 ; i < str.length() ; i++){
				char c = str.charAt(i);
				if(permissionChar.size() + permissionCharStart.size() > 0){
					for(int j = 0 ; j < permissionChar.size() ; j++){
						if(c == permissionChar.get(j)){
							pass = true;
							break;
						}
					}
					if(!pass){
						for(int j = 0 ; j < permissionCharStart.size() ; j++){
							if(c >= permissionCharStart.get(j) && c <= permissionCharEnd.get(j)){
								pass = true;
								break;
							}
						}
					}
				}else{
					pass = true;
				}
				
				if(blockChar.size() + blockCharStart.size() > 0){
					if(pass){
						for(int j = 0 ; j < blockChar.size() ; j++){
							if(c == blockChar.get(j)){
								pass = false;
								break;
							}
						}
						if(pass){
							for(int j = 0 ; j < blockCharStart.size() ; j++){
								if(c >= blockCharStart.get(j) && c <= blockCharEnd.get(j)){
									pass = false;
									break;
								}
							}
						}
					}
				}
				if(!pass){
					return;
				}
			}
		}
		
		if(this.getLength() + str.length() <= limit && pass){
			super.insertString(offset, str, attr);
		}
	}
}
