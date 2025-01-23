package net.prizowo.examplemod.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateHopperC2SPacket {
    private final boolean shouldDrop;
    private final int maxDropCount;

    public UpdateHopperC2SPacket(boolean shouldDrop, int maxDropCount) {
        this.shouldDrop = shouldDrop;
        this.maxDropCount = maxDropCount;
    }

    public UpdateHopperC2SPacket(FriendlyByteBuf buf) {
        this.shouldDrop = buf.readBoolean();
        this.maxDropCount = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(shouldDrop);
        buf.writeInt(maxDropCount);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                Container container = player.containerMenu.slots.get(0).container;
                if (container instanceof HopperBlockEntity hopper) {
                    if (!hopper.getLevel().isClientSide) {
                        synchronized (hopper) {
                            CompoundTag tag = hopper.saveWithoutMetadata();
                            tag.putBoolean("ShouldDrop", shouldDrop);
                            tag.putInt("MaxDropCount", Math.max(1, Math.min(64, maxDropCount)));
                            hopper.load(tag);
                            hopper.setChanged();
                            
                            if (hopper.getLevel() != null) {
                                hopper.getLevel().sendBlockUpdated(hopper.getBlockPos(), 
                                    hopper.getBlockState(), hopper.getBlockState(), 3);
                                hopper.getLevel().blockEntityChanged(hopper.getBlockPos());
                                player.containerMenu.broadcastChanges();
                            }
                        }
                    }
                }
            }
        });
        return true;
    }
} 