package dog.skip.cocoabeans.mixin.client;

import dog.skip.cocoabeans.KeyCodes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
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
public abstract class CocoaTextFieldMixin {
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
    @Redirect(method = "erase(I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;hasControlDown()Z"))
    private boolean wordwiseDeleteWithOption() {
        if (!MinecraftClient.IS_SYSTEM_MAC) {
            return Screen.hasControlDown();
        }

        return Screen.hasAltDown();
    }

    /**
     * Redirects the modifier key check that occurs when traversing (or extending a selection) over words to check
     * for Option instead of Command.
     */
    @Redirect(
        method = "keyPressed(III)Z",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;hasControlDown()Z"),
        slice = @Slice(
            // the game only calls this after it finishes handling arrow keys, backspace/delete, and home/end, so we
            // only need to redirect calls that occur _beforehand_
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;isSelectAll(I)Z")
        )
    )
    private boolean wordwiseTraversalWithOption() {
        if (!MinecraftClient.IS_SYSTEM_MAC) {
            return Screen.hasControlDown();
        }

        return Screen.hasAltDown();
    }

    /**
     * Handle keyboard shortcuts to traverse the entire document/line. We treat document and line identically, setting
     * the cursor to the start or end in both cases.
     */
    @Inject(
        method = "keyPressed(III)Z",
        // PLZNO: this is _really_ gross, but we locate the control/command check (in the original code) and then back
        // out of the TABLESWITCH just outside of the if statement that checks if the widget is focused/narratable.
        // i have no idea why this is -5
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;hasControlDown()Z", shift = At.Shift.BY, by = -5, ordinal = 0),
        cancellable = true
    )
    private void handleDocumentwiseLinewise(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!Screen.hasControlDown() || !MinecraftClient.IS_SYSTEM_MAC) {
            return;
        }

        switch (keyCode) {
            case KeyCodes.LEFT_ARROW, KeyCodes.UP_ARROW -> {
                this.setCursorToStart(Screen.hasShiftDown());
                cir.setReturnValue(true);
            }
            case KeyCodes.RIGHT_ARROW, KeyCodes.DOWN_ARROW -> {
                this.setCursorToEnd(Screen.hasShiftDown());
                cir.setReturnValue(true);
            }
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
