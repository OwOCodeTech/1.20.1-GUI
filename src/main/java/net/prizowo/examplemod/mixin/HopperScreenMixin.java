package net.prizowo.examplemod.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.HopperScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.prizowo.examplemod.network.ModMessages;
import net.prizowo.examplemod.network.packet.UpdateHopperC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;

@Mixin(HopperScreen.class)
public abstract class HopperScreenMixin extends AbstractContainerScreen<HopperMenu> {
    private EditBox maxCountInput;
    private Button checkBox;
    private boolean currentShouldDrop = true;

    public HopperScreenMixin(HopperMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        
        int startX = this.leftPos + 176;
        int startY = this.topPos + 5;

        Container container = this.menu.slots.get(0).container;
        if (container instanceof HopperBlockEntity hopper) {
            CompoundTag tag = hopper.saveWithoutMetadata();
            currentShouldDrop = tag.contains("ShouldDrop") ? tag.getBoolean("ShouldDrop") : true;
            int currentMaxCount = tag.contains("MaxDropCount") ? tag.getInt("MaxDropCount") : 1;
            this.maxCountInput = new EditBox(this.font,
                startX + 5, startY + 60,
                40, 20, Component.translatable("gui.examplemod.hopper.max_count"));
            this.maxCountInput.setValue(String.valueOf(currentMaxCount));
        } else {
            this.maxCountInput = new EditBox(this.font,
                startX + 5, startY + 60,
                40, 20, Component.translatable("gui.examplemod.hopper.max_count"));
            this.maxCountInput.setValue("1");
            currentShouldDrop = true;
        }
                
        Button confirmButton = Button.builder(
            Component.translatable("gui.examplemod.hopper.confirm"), 
            (button) -> {
                try {
                    int newMaxCount = Integer.parseInt(this.maxCountInput.getValue());
                    if (newMaxCount > 0 && newMaxCount <= 64) {
                        ModMessages.sendToServer(new UpdateHopperC2SPacket(currentShouldDrop, newMaxCount));
                    }
                } catch (NumberFormatException ignored) {}
            }).bounds(startX + 50, startY + 60, 40, 20).build();
                
        this.checkBox = Button.builder(
            Component.translatable(currentShouldDrop ? "gui.examplemod.hopper.disable_drop" : "gui.examplemod.hopper.enable_drop"),
            (button) -> {
                ModMessages.sendToServer(new UpdateHopperC2SPacket(!currentShouldDrop, 
                    Integer.parseInt(this.maxCountInput.getValue())));
                currentShouldDrop = !currentShouldDrop;
                button.setMessage(Component.translatable(currentShouldDrop ? 
                    "gui.examplemod.hopper.disable_drop" : "gui.examplemod.hopper.enable_drop"));
            }).bounds(startX + 5, startY + 20, 85, 20).build();

        this.addRenderableWidget(this.checkBox);
        this.addRenderableWidget(this.maxCountInput);
        this.addRenderableWidget(confirmButton);
        
        this.maxCountInput.setVisible(true);
        this.maxCountInput.setEditable(true);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        
        int startX = this.leftPos + 176;
        int startY = this.topPos + 5;

        graphics.drawString(this.font,
            Component.translatable("gui.examplemod.hopper.should_drop"), 
            startX + 5, startY + 5, 0x404040, false);
        graphics.drawString(this.font, 
            Component.translatable("gui.examplemod.hopper.max_count"), 
            startX + 5, startY + 45, 0x404040, false);
    }
} 