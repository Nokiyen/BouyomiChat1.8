package noki.bouyomichat;

import java.io.File;
import java.io.IOException;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import noki.bouyomichat.bc.BouyomiEvent;
import noki.bouyomichat.command.CommandBouyomi;


/**********
 * @Mod BouyomiChat
 *
 * @author Nokiyen
 * 
 * @description 棒読みちゃんにチャットを読んでもらうModです。
 * 元祖棒読みちゃんMod作者のIwatoRockyさんのコードを参考にしています(しかし、内容はけっこう違いﾏｽ)。
 */
@Mod(modid = "BouyomiChat", version = "2.0.0", name = "Bouyomi Chat")
public class BouyomiChatCore {
	
	//******************************//
	// define member variables.
	//******************************//
	@Instance(value = "BouyomiChat")
	public static BouyomiChatCore instance;
	@Metadata
	public static ModMetadata metadata;	//	extract from mcmod.info file, not java internal coding.
	public static VersionInfo versionInfo;

	
	//******************************//
	// define member methods.
	//******************************//
	//----------
	//Core Event Methods.
	//----------
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		BouyomiChatConf.configFile = event.getSuggestedConfigurationFile();
		BouyomiChatConf.setConf();
		
		versionInfo = new VersionInfo(metadata.modId.toLowerCase(), metadata.version, metadata.updateUrl);
		
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(new BouyomiEvent());
		ClientCommandHandler.instance.registerCommand(new CommandBouyomi());//	register a command to the client.
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		if(BouyomiChatConf.autoExecBouyomi == true) {
			executeBouyomiChan();
		}
	
	}
	
	
	//----------
	//Static Method.
	//----------
	public static void log(String message, Object... data) {
		
		FMLLog.fine("[BouyomiChat:LOG] "+message, data);
		
	}

	public static boolean executeBouyomiChan() {
		
		File file = new File(BouyomiChatConf.pathBouyomi);
		boolean flag = true;
		if(file.exists()) {
			try {
				Runtime rt = Runtime.getRuntime();
				rt.exec(BouyomiChatConf.pathBouyomi);
			} catch (IOException ex) {
				BouyomiChatCore.log("Can't execute Bouyomi-Chan.");
				flag = false;
			}
		}
		else {
			flag = false;
		}
		
		return flag;
		
	}
	
}
