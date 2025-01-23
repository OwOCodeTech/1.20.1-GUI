package net.prizowo.examplemod.container;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DropperContainer extends SimpleContainer {
    private final Level level;
    private final BlockPos pos;
    private final int maxDropCount;
    
    public DropperContainer(Level level, BlockPos pos, int maxDropCount) {
        super(1);
        this.level = level;
        this.pos = pos;
        this.maxDropCount = maxDropCount;
    }
    
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return true;
    }
    
    @Override
    public ItemStack addItem(ItemStack stack) {
        ItemStack remaining = stack.copy();
        int dropCount = Math.min(remaining.getCount(), maxDropCount);
        if (dropCount > 0) {
            ItemStack droppingStack = remaining.split(dropCount);
            ItemEntity itemEntity = new ItemEntity(level, 
                pos.getX() + 0.5, 
                pos.getY() + 0.1, 
                pos.getZ() + 0.5, 
                droppingStack);
            itemEntity.setDeltaMovement(0, 0, 0);
            level.addFreshEntity(itemEntity);
            
            level.playSound(null, pos, SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return remaining;
    }
    
    @Override
    public void setItem(int slot, ItemStack stack) {
        if (!stack.isEmpty()) {
            addItem(stack);
        }
    }
} 