package net.prizowo.examplemod.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.prizowo.examplemod.container.DropperContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {
    @Unique
    private boolean shouldDrop = true;
    
    @Unique
    private int maxDropCount = 1;
    
    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void onSave(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("ShouldDrop", shouldDrop);
        tag.putInt("MaxDropCount", maxDropCount);
    }
    
    @Inject(method = "load", at = @At("TAIL"))
    private void onLoad(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("ShouldDrop")) {
            shouldDrop = tag.getBoolean("ShouldDrop");
        }
        if (tag.contains("MaxDropCount")) {
            maxDropCount = Math.max(1, Math.min(64, tag.getInt("MaxDropCount")));
        }
    }
    
    @Inject(method = "getAttachedContainer", at = @At("RETURN"), cancellable = true)
    private static void onGetAttachedContainer(Level level, BlockPos pos, BlockState state, CallbackInfoReturnable<Container> cir) {
        if (cir.getReturnValue() == null) {
            Direction direction = state.getValue(net.minecraft.world.level.block.HopperBlock.FACING);
            BlockPos targetPos = pos.relative(direction);
            BlockState targetState = level.getBlockState(targetPos);
            
            HopperBlockEntity hopper = (HopperBlockEntity)level.getBlockEntity(pos);
            if (hopper != null) {
                HopperBlockEntityMixin hopperMixin = (HopperBlockEntityMixin)(Object)hopper;
                
                if (hopperMixin.shouldDrop && 
                    (level.isEmptyBlock(targetPos) ||
                    !targetState.blocksMotion() || 
                    targetState.getPistonPushReaction() == PushReaction.DESTROY)) {
                    
                    cir.setReturnValue(new DropperContainer(level, targetPos, hopperMixin.maxDropCount));
                }
            }
        }
    }

    @Unique
    public boolean getShouldDrop() {
        return shouldDrop;
    }

    @Unique
    public void setShouldDrop(boolean value) {
        this.shouldDrop = value;
        ((HopperBlockEntity)(Object)this).setChanged();
    }

    @Unique
    public int getMaxDropCount() {
        return maxDropCount;
    }

    @Unique
    public void setMaxDropCount(int value) {
        this.maxDropCount = Math.max(1, Math.min(64, value));
        ((HopperBlockEntity)(Object)this).setChanged();
    }
} 