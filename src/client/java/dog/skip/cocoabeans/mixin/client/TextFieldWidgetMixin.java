package dog.skip.cocoabeans.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dog.skip.cocoabeans.CocoaInputUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// offsets/deltas are:
//   - negative when deleting what's left of the caret (backspace)
//   - positive when deleting what's right of the caret (delete)

@Mixin(TextFieldWidget.class)
public abstract class TextFieldWidgetMixin {
    @Shadow
    public abstract void eraseCharactersTo(int position);

    @Shadow
    private String text;

    @Shadow
    public abstract void setCursorToStart(boolean shiftKeyPressed);

    @Shadow
    public abstract void setCursorToEnd(boolean shiftKeyPressed);

    /**
     * Redirects the modifier key check that occurs while deleting words to check for Option instead of Command.
     */
    @WrapOperation(method = "erase(I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;hasControlDown()Z"))
    private boolean wordwiseDeleteWithOption(Operation<Boolean> original) {
        if (MinecraftClient.IS_SYSTEM_MAC) {
            return Screen.hasAltDown();
        }

        return original.call();
    }

    /**
     * Redirects the modifier key check that occurs when traversing (or extending a selection) over words to check
     * for Option instead of Command.
     */
    @WrapOperation(
        method = "keyPressed(III)Z",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;hasControlDown()Z"),
        slice = @Slice(
            // the game only calls this after it finishes handling arrow keys, backspace/delete, and home/end, so we
            // only need to redirect calls that occur _beforehand_
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;isSelectAll(I)Z")
        )
    )
    private boolean wordwiseTraversalWithOption(Operation<Boolean> original) {
        if (MinecraftClient.IS_SYSTEM_MAC) {
            return Screen.hasAltDown();
        }

        return original.call();
    }

    /**
     * Handle keyboard shortcuts to traverse the entire document/line. We treat document and line identically, setting
     * the cursor to the start or end in both cases.
     */
    @Inject(
        method = "keyPressed(III)Z",
        // inject just after the return that occurs if we fail the "i must be narratable and focused" check, so we can
        // reuse it.
        at = @At(value = "RETURN", shift = At.Shift.BY, by = 2, ordinal = 0),
        cancellable = true
    )
    private void handleDocumentwiseLinewise(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!MinecraftClient.IS_SYSTEM_MAC) {
            return;
        }

        final boolean controlDown = CocoaInputUtil.hasControlDownLikeActually();
        final boolean commandDown = CocoaInputUtil.hasCommandDownLikeActually();

        if ((controlDown && keyCode == GLFW.GLFW_KEY_A) || (commandDown && (keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_UP))) {
            // control-a, command-left, command-up
            this.setCursorToStart(Screen.hasShiftDown());
            cir.setReturnValue(true);
        } else if ((controlDown && keyCode == GLFW.GLFW_KEY_E) || (commandDown && (keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_DOWN))) {
            // control-e, command-right, command-down
            this.setCursorToEnd(Screen.hasShiftDown());
            cir.setReturnValue(true);
        }
    }

    /**
     * Handles Command-Delete and Command-Forward Delete.
     */
    @Inject(method = "erase(I)V", at = @At("HEAD"), cancellable = true)
    private void linewiseErasing(int offset, CallbackInfo ci) {
        if (!MinecraftClient.IS_SYSTEM_MAC) {
            return;
        }

        if (Screen.hasControlDown()) {
            this.eraseCharactersTo(offset == -1 ? 0 : this.text.length());
            ci.cancel();
        }
    }
}
