package net.prizowo.examplemod.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.prizowo.examplemod.Examplemod;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

public class ModeSwitcherMenu extends AbstractContainerMenu {
    public static final DeferredRegister<MenuType<?>> MENUS = 
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Examplemod.MOD_ID);
    
    public static final RegistryObject<MenuType<ModeSwitcherMenu>> MODE_SWITCHER_MENU = 
            MENUS.register("mode_switcher_menu",
                    () -> IForgeMenuType.create(ModeSwitcherMenu::new));

    private final BlockPos blockPos;

    public ModeSwitcherMenu(int windowId, Inventory inv, FriendlyByteBuf data) {
        this(windowId, inv, data != null ? data.readBlockPos() : BlockPos.ZERO);
    }

    public ModeSwitcherMenu(int windowId, Inventory inv, BlockPos pos) {
        super(MODE_SWITCHER_MENU.get(), windowId);
        this.blockPos = pos;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(
            blockPos.getX() + 0.5D,
            blockPos.getY() + 0.5D,
            blockPos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    public static class Provider implements MenuProvider {
        private final BlockPos pos;

        public Provider(BlockPos pos) {
            this.pos = pos;
        }

        @Override
        public @NotNull Component getDisplayName() {
            return Component.translatable("block.examplemod.mode_switcher");
        }

        @Override
        public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
            return new ModeSwitcherMenu(id, inventory, pos);
        }
    }
} 