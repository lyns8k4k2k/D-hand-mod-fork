package vigillant.lyns.fabric.dhandmod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vigillant.lyns.fabric.dhandmod.util.InventorySafetyHandler;

@Mixin(MinecraftClient.class)
public abstract class InventorySwitchMixin {
    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void onHandleInputEvents(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        KeyBinding inventoryKey = client.options.inventoryKey;

        // Use wasPressed() instead of isPressed() to avoid repeated triggering
        if (inventoryKey.wasPressed() && client.currentScreen == null) {
            InventorySafetyHandler.onInventoryKeyPressed();
        }
    }
}