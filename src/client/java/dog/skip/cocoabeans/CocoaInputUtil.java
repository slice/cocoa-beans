package dog.skip.cocoabeans;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class CocoaInputUtil {
    /**
     * Returns whether the control key (and only the control key) is down. Misleadingly, {@link Screen#hasControlDown()}
     * returns <code>true</code> when Command is down on macOS. This method is used to avoid that behavior.
     */
    public static boolean hasControlDownLikeActually() {
        final long handle = MinecraftClient.getInstance().getWindow().getHandle();
        return InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_CONTROL) || InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT_CONTROL);
    }

    /**
     * Returns whether the command key (and only the command key) is down.
     */
    public static boolean hasCommandDownLikeActually() {
        final long handle = MinecraftClient.getInstance().getWindow().getHandle();
        return InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SUPER) || InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT_SUPER);
    }
}
