//package net.prizowo.examplemod.event;
//
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.ComputeFovModifierEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.prizowo.examplemod.Examplemod;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Mod.EventBusSubscriber(modid = Examplemod.MOD_ID, value = Dist.CLIENT)
//public class ClientEvents {
//    // 远程武器的FOV缩放系数
//    private static final float RANGE_WEAPON_ZOOM = 0.35F;
//    // 投掷物的FOV缩放系数
//    private static final float THROWABLE_ZOOM = 0.25F;
//    // 三叉戟的FOV缩放系数
//    private static final float TRIDENT_ZOOM = 0.4F;
//    // 飞行时的FOV放大系数
//    private static final float FLYING_MULTIPLIER = 1.3F;
//    // 使用物品时的最大tick数
//    private static final float MAX_USE_DURATION = 20.0F;
//
//    // 存储物品及其对应的FOV缩放系数
//    private static final Map<Item, Float> ZOOM_FACTORS = new HashMap<>();
//
//    static {
//        // 初始化远程武器
//        registerRangeWeapons();
//        // 初始化投掷物
//        registerThrowables();
//        // 初始化特殊物品
//        registerSpecialItems();
//    }
//
//    /**
//     * 注册远程武器的缩放系数
//     */
//    private static void registerRangeWeapons() {
//        ZOOM_FACTORS.put(Items.BOW, RANGE_WEAPON_ZOOM);
//        ZOOM_FACTORS.put(Items.CROSSBOW, RANGE_WEAPON_ZOOM);
//    }
//
//    /**
//     * 注册投掷物的缩放系数
//     */
//    private static void registerThrowables() {
//        ZOOM_FACTORS.put(Items.SNOWBALL, THROWABLE_ZOOM);
//        ZOOM_FACTORS.put(Items.EGG, THROWABLE_ZOOM);
//        ZOOM_FACTORS.put(Items.ENDER_PEARL, THROWABLE_ZOOM);
//        ZOOM_FACTORS.put(Items.SPLASH_POTION, THROWABLE_ZOOM);
//        ZOOM_FACTORS.put(Items.LINGERING_POTION, THROWABLE_ZOOM);
//        ZOOM_FACTORS.put(Items.FISHING_ROD, THROWABLE_ZOOM);
//    }
//
//    /**
//     * 注册特殊物品的缩放系数
//     */
//    private static void registerSpecialItems() {
//        ZOOM_FACTORS.put(Items.TRIDENT, TRIDENT_ZOOM);
//    }
//
//    /**
//     * 处理FOV修改事件
//     * @param event FOV修改事件
//     */
//    @SubscribeEvent
//    public static void onFOVModifier(ComputeFovModifierEvent event) {
//        Player player = event.getPlayer();
//        float fov = event.getFovModifier();
//
//        // 处理飞行状态的FOV
//        fov = handleFlyingFOV(player, fov);
//
//        // 处理物品使用状态的FOV
//        fov = handleItemUseFOV(player, fov);
//
//        event.setNewFovModifier(fov);
//    }
//
//    /**
//     * 处理飞行状态下的FOV
//     * @param player 玩家
//     * @param currentFOV 当前FOV值
//     * @return 处理后的FOV值
//     */
//    private static float handleFlyingFOV(Player player, float currentFOV) {
//        if (player.getAbilities().flying) {
//            return currentFOV * FLYING_MULTIPLIER;
//        }
//        return currentFOV;
//    }
//
//    /**
//     * 处理物品使用状态下的FOV
//     * @param player 玩家
//     * @param currentFOV 当前FOV值
//     * @return 处理后的FOV值
//     */
//    private static float handleItemUseFOV(Player player, float currentFOV) {
//        if (!player.isUsingItem()) {
//            return currentFOV;
//        }
//
//        ItemStack itemStack = player.getUseItem();
//        Float zoomFactor = ZOOM_FACTORS.get(itemStack.getItem());
//
//        if (zoomFactor == null) {
//            return currentFOV;
//        }
//
//        float useProgress = calculateUseProgress(player);
//        return applyZoomEffect(currentFOV, useProgress, zoomFactor);
//    }
//
//    /**
//     * 计算物品使用进度
//     * @param player 玩家
//     * @return 使用进度 (0.0 - 1.0)
//     */
//    private static float calculateUseProgress(Player player) {
//        return Math.min(1.0F, player.getTicksUsingItem() / MAX_USE_DURATION);
//    }
//
//    /**
//     * 应用缩放效果
//     * @param currentFOV 当前FOV值
//     * @param progress 使用进度
//     * @param zoomFactor 缩放系数
//     * @return 处理后的FOV值
//     */
//    private static float applyZoomEffect(float currentFOV, float progress, float zoomFactor) {
//        return currentFOV * (1.0F - (progress * progress) * zoomFactor);
//    }
//}