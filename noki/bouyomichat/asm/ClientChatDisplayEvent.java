package noki.bouyomichat.asm;

import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;


/**********
 * @class ClientChatDisplayEvent
 *
 * @description GuiNewChat.printChatMessage()で実際にチャットを表示するときに発生するイベントです。
 * Forgeにissue投げたけど反応がいまいち(確かにあまり使うイベントではない)ので自分で実装しました。
 * Cancelableが付いていますが、実際にcancelしている場面はありません。
 * 本来はAPIとして外のpackageに置くべきですがのちのち検討します。
 */
@Cancelable
public class ClientChatDisplayEvent extends Event {
	
	
	//******************************//
	// define member variables.
	//******************************//
	public IChatComponent message;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ClientChatDisplayEvent(IChatComponent message) {
		
		this.message = message;
		
	}
	
	
	//----------
	//Static Method.
	//----------
	public static boolean postEvent(IChatComponent message) {
		
		return MinecraftForge.EVENT_BUS.post(new ClientChatDisplayEvent(message));
		
	}

}
