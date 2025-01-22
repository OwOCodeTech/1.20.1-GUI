package net.prizowo.examplemod.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.prizowo.examplemod.Examplemod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Examplemod.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    private static final Map<Item, Float> ZOOM_FACTORS = new HashMap<>();
    
    static {
        // 初始化缩放系数
        float RANGE_WEAPON = 0.35F;  // 远程武器
        float THROWABLE = 0.25F;     // 投掷物
        float TRIDENT = 0.4F;        // 三叉戟
        
        ZOOM_FACTORS.put(Items.BOW, RANGE_WEAPON);
        ZOOM_FACTORS.put(Items.CROSSBOW, RANGE_WEAPON);
        ZOOM_FACTORS.put(Items.TRIDENT, TRIDENT);
        ZOOM_FACTORS.put(Items.SNOWBALL, THROWABLE);
        ZOOM_FACTORS.put(Items.EGG, THROWABLE);
        ZOOM_FACTORS.put(Items.ENDER_PEARL, THROWABLE);
        ZOOM_FACTORS.put(Items.SPLASH_POTION, THROWABLE);
        ZOOM_FACTORS.put(Items.LINGERING_POTION, THROWABLE);
        ZOOM_FACTORS.put(Items.FISHING_ROD, THROWABLE);
    }

    @SubscribeEvent
    public static void onFOVModifier(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        float fov = event.getFovModifier();
        
        if (player.getAbilities().flying) {
            fov *= 1.3F;
        }
        
        if (player.isUsingItem()) {
            ItemStack item = player.getUseItem();
            Float zoomFactor = ZOOM_FACTORS.get(item.getItem());
            
            if (zoomFactor != null) {
                float progress = Math.min(1.0F, (float) player.getTicksUsingItem() / 20.0F);
                fov *= 1.0F - (progress * progress) * zoomFactor;
            }
        }
        
        event.setNewFovModifier(fov);
    }
}