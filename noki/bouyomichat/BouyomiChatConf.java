package noki.bouyomichat;

import java.io.File;

import net.minecraftforge.common.config.Configuration;


/**********
 * @class BouyomiConf
 *
 * @description このModのコンフィグの値を保存するクラスです。
 */
public class BouyomiChatConf {
	
	//******************************//
	// define member variables.
	//******************************//
	public static File configFile;
	
	public static String host;
	public static final String hostDefault= "localhost";
	public static int port;
	public static final int portDefault = 50001;
	
	public static boolean readName;
	public static final boolean readNameDefault = true;
	public static boolean readPrefix;
	public static final boolean readPrefixDefault = true;
	public static boolean readBouyomi;
	public static final boolean readBouyomiDefault = false;
	public static boolean readSwitch;
	public static final boolean readSwitchDefault = true;
	
	public static short speed;
	public static final short speedDefault = (short)-1;
	public static short tone;
	public static final short toneDefault =(short)-1;
	public static short volume;
	public static final short volumeDefault = (short)-1;
	public static short voice;
	public static final short voiceDefault = (short)0;
	
	public static final String charsetName = "UTF-8";
	
	public static String pathBouyomi;
	public static boolean autoExecBouyomi;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public static void setConf() {
		
		
		Configuration cfg = new Configuration(configFile);
		cfg.defaultEncoding = charsetName;
		cfg.load();
		
		host = cfg.getString("host", "Settings", hostDefault, "");
		port = cfg.getInt("port", "Settings", portDefault, 0, 65535, "");
		
		readName = cfg.getBoolean("readName", "Settings", readNameDefault, "");
		readPrefix = cfg.getBoolean("readPrefix", "Settings", readPrefixDefault, "");
		readBouyomi = cfg.getBoolean("readBouyomi", "Settings", readBouyomiDefault, "");
		readSwitch = cfg.getBoolean("readSwitch", "Settings", readSwitchDefault, "");
		
		speed = (short)cfg.get("Settings", "speed", speedDefault).getInt();
		if(speed != -1 && (speed < 50 || 300 < speed)) {
			speed = speedDefault;
		}
		tone = (short)cfg.get("Settings", "tone", toneDefault).getInt();
		if(tone != -1 && (tone < 50 || 200 < tone)) {
			tone = toneDefault;
		}
		volume = (short)cfg.get("Settings", "volume", volumeDefault).getInt();
		if(volume != -1 && (volume < 0 || 100 < volume)) {
			volume = volumeDefault;
		}
		voice = (short)cfg.get("Settings", "voice", voiceDefault).getInt();
		if(voice != 0 && (voice < 1 || 8 < voice)) {
			voice = voiceDefault;
		}
		
		pathBouyomi = cfg.getString("pathBouyomi", "Settings", "", "").replaceAll("\\\\", "\\\\\\\\");
		autoExecBouyomi = cfg.getBoolean("autoExecBouyomi", "Settings", true, "");
		
		cfg.save();
		
	}

}
