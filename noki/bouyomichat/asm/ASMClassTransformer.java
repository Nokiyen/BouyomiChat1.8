package noki.bouyomichat.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AnalyzerAdapter;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;


/**********
 * @class ASMClassTransformer
 *
 * @description 実際にASMによるバイトコード改変を行うクラスです。
 */
public class ASMClassTransformer implements IClassTransformer, Opcodes {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String TARGET_CLASS_NAME = "net.minecraft.client.gui.GuiNewChat";
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		
		ASMLoadingPlugin.LOGGER.fine("enter ASMClassTransformer.");
		
		if (!transformedName.equals(TARGET_CLASS_NAME)) {
			return basicClass;
		}
		
		try {
			ASMLoadingPlugin.LOGGER.fine("enter transforming.");
			
			//**この辺りから、ASMの書き方ができます。**//
			
			//ClassReader, ClassWriter, ClassVisitorで3すくみになるように引数を与えることで処理を早める。
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, 0);
			CustomClassVisitor customVisitor = new CustomClassVisitor(name, classWriter);
			classReader.accept(customVisitor, 0);
			return classWriter.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("asm, class transforming failed.", e);
		}
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	class CustomClassVisitor extends ClassVisitor {
		
		//*****define member variables.*//
		private String owner;
		
		private static final String TARGET_METHOD_NAME_OBF = "func_146227_a";
		private static final String TARGET_METHOD_DESC = "(Lnet/minecraft/util/IChatComponent;)V";
		@SuppressWarnings("unused")
		private static final String TARGET_METHOD_NAME = "printChatMessage";
		
		
		//*****define member methods.***//
		public CustomClassVisitor(String owner, ClassVisitor cv) {
			super(Opcodes.ASM4, cv);
			this.owner = owner;
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			boolean flag1 = TARGET_METHOD_NAME_OBF.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(this.owner, name, desc));
			boolean flag2 = TARGET_METHOD_DESC.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc));
			if(flag1 && flag2) {
				ASMLoadingPlugin.LOGGER.fine("enter method.");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new CustomMethodVisitor(this.api, this.owner, access, name, desc, mv);
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		
	}
	
	//MethodVisitorの代わりにAnalyzerAdapterを使うことで、
	//visitMax()やvisitFrame()の処理をしなくてよくなる。
	//その代わり、各種superメソッドを呼び出す必要がある。
	//COMPUTE_MAXでもそんなに速度落ちないという噂も。
	class CustomMethodVisitor extends AnalyzerAdapter {
		
		//*****define member variables.*//
		protected CustomMethodVisitor(int api, String owner, int access, String name, String desc, MethodVisitor mv) {
			super(api, owner, access, name, desc, mv);
		}

		//*****define member methods.***//
		@Override
		public void visitCode() {
			/*
			 * if(ClientChatDisplayEvent.post(chatComponent) == true) {
			 * 		return;
			 * }
			 */
			ASMLoadingPlugin.LOGGER.fine("enter visitCode().");
			super.visitCode();
			super.visitVarInsn(ALOAD, 1);
			super.visitMethodInsn(INVOKESTATIC, "noki/bouyomichat/asm/ClientChatDisplayEvent",
					"postEvent", "(Lnet/minecraft/util/IChatComponent;)Z", false);
			Label label = new Label();
			super.visitJumpInsn(IFEQ, label);
			super.visitInsn(RETURN);
			super.visitLabel(label);
//			super.visitFrame(F_NEW, 0, null, 0, null); //必要なし
		}
		
	}

}
