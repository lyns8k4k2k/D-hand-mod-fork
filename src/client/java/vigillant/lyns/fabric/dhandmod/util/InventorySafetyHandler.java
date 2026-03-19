package vigillant.lyns.fabric.dhandmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class InventorySafetyHandler {
    private static boolean inventoryActionInProgress = false;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Random random = new Random();

    public static void onInventoryKeyPressed() {
        if (inventoryActionInProgress) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        int targetSlot = -1;

        // Find totem in hotbar
        for (int i = 0; i < 9; i++) {
            if (client.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                targetSlot = i;
                break;
            }
        }

        // Switch slot (uses access widener)
        if (targetSlot != -1 && client.player.getInventory().selectedSlot != targetSlot) {
            client.player.getInventory().selectedSlot = targetSlot;
        }

        long delayMs = 15 + random.nextInt(25);
        inventoryActionInProgress = true;

        scheduler.schedule(() -> {
            client.execute(() -> {
                try {
                    if (client.player != null && client.currentScreen == null) {
                        client.setScreen(new InventoryScreen(client.player));
                    }
                } finally {
                    inventoryActionInProgress = false;
                }
            });
        }, delayMs, TimeUnit.MILLISECONDS);
    }

    public static void shutdown() {
        scheduler.shutdown();
    }
}