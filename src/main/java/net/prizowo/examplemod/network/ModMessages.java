package net.prizowo.examplemod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.prizowo.examplemod.Examplemod;
import net.prizowo.examplemod.network.packet.SetGameModeC2SPacket;
import net.prizowo.examplemod.network.packet.UpdateHopperC2SPacket;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Examplemod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(SetGameModeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetGameModeC2SPacket::new)
                .encoder(SetGameModeC2SPacket::toBytes)
                .consumerMainThread(SetGameModeC2SPacket::handle)
                .add();

        net.messageBuilder(UpdateHopperC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateHopperC2SPacket::new)
                .encoder(UpdateHopperC2SPacket::toBytes)
                .consumerMainThread(UpdateHopperC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
} 