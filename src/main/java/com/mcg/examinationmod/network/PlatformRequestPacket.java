package com.mcg.examinationmod.network;

import com.mcg.examinationmod.ExaminationMod;
import com.mcg.examinationmod.event.PlatformGenerator;
import com.mcg.examinationmod.event.PlayerTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

// 平台请求网络包
public record PlatformRequestPacket(String text) implements CustomPacketPayload {
    public static final Type<PlatformRequestPacket> TYPE = new Type<PlatformRequestPacket>(ResourceLocation.fromNamespaceAndPath(ExaminationMod.MODID, "platform_req"));
    public static final StreamCodec<FriendlyByteBuf, PlatformRequestPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            PlatformRequestPacket::text,
            PlatformRequestPacket::new
    );

    public static void handle(final PlatformRequestPacket data, final IPayloadContext context){
        // 处理网络包的方法，它将接收到的数据在服务器端的逻辑线程上处理
        context.enqueueWork(()-> {
                    var player = context.player();
                    switch (data.text) {
                        case "start" -> PlatformGenerator.generatePlatformAbovePlayer(player);
                        case "party" -> {
                            if (player != null && player.level() instanceof ServerLevel serverLevel) {
                                BlockPos platformCenter = PlatformGenerator.generatePlatformAbovePlayer(player, false);
                                if (platformCenter != null) {
                                    // 获取玩家数量并传送所有玩家
                                    int playerCount = PlayerTeleporter.getPlayerCount(serverLevel);
                                    int teleportedCount = PlayerTeleporter.teleportAllPlayersToPlatform(serverLevel, platformCenter);
                                    PlayerTeleporter.getPlayerCountAndTeleportAll(serverLevel, platformCenter);
                                    context.reply(new TeleportResponsePayload(playerCount, teleportedCount));
                                }
                            }
                        }
                    }
                });
           // case "team"  -> 这里写方法
        }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

