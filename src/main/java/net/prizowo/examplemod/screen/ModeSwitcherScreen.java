package net.prizowo.examplemod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.GameType;
import net.prizowo.examplemod.menu.ModeSwitcherMenu;
import net.prizowo.examplemod.networking.ModMessages;
import net.prizowo.examplemod.networking.packet.SetGameModeC2SPacket;

public class ModeSwitcherScreen extends AbstractContainerScreen<ModeSwitcherMenu> {
    private static final ResourceLocation TEXTURE = 
            new ResourceLocation("minecraft", "textures/gui/demo_background.png");
    private static final int TEXTURE_WIDTH = 248;
    private static final int TEXTURE_HEIGHT = 166;

    public ModeSwitcherScreen(ModeSwitcherMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;  // 设置GUI宽度
        this.imageHeight = 100; // 设置GUI高度
        this.inventoryLabelY = this.imageHeight - 94; // 隐藏物品栏标签
    }

    @Override
    protected void init() {
        super.init();
        
        // 添加创造模式按钮
        this.addRenderableWidget(Button.builder(Component.translatable("gameMode.creative"), (button) -> {
                    ModMessages.sendToServer(new SetGameModeC2SPacket(GameType.CREATIVE));
                })
                .pos(this.leftPos + (this.imageWidth / 2) - 65, this.topPos + 40)
                .size(60, 20)
                .build());

        // 添加生存模式按钮
        this.addRenderableWidget(Button.builder(Component.translatable("gameMode.survival"), (button) -> {
                    ModMessages.sendToServer(new SetGameModeC2SPacket(GameType.SURVIVAL));
                })
                .pos(this.leftPos + (this.imageWidth / 2) + 5, this.topPos + 40)
                .size(60, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        
        // 绘制标题
        graphics.drawCenteredString(this.font, this.title, 
                this.leftPos + this.imageWidth / 2, 
                this.topPos + 6, 
                0x404040);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
} 