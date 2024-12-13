package net.prizowo.examplemod.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetGameModeC2SPacket {
    private final GameType gameType;

    public SetGameModeC2SPacket(GameType gameType) {
        this.gameType = gameType;
    }

    public SetGameModeC2SPacket(FriendlyByteBuf buf) {
        this.gameType = GameType.byId(buf.readInt());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(gameType.getId());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.hasPermissions(2)) {
                player.setGameMode(gameType);
            }
        });
        return true;
    }
} 