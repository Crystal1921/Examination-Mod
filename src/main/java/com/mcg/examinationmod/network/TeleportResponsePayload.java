package com.mcg.examinationmod.network;
import com.mcg.examinationmod.ExaminationMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record TeleportResponsePayload(int playerCount, int teleportedCount) implements CustomPacketPayload {
    public static final Type<TeleportResponsePayload> TYPE = new Type<TeleportResponsePayload>(ResourceLocation.fromNamespaceAndPath(ExaminationMod.MODID, "tp_platform_req"));

    // 创建流编解码器
    public static final StreamCodec<FriendlyByteBuf, TeleportResponsePayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeInt(payload.playerCount());
                buf.writeInt(payload.teleportedCount());
            },
            buf -> new TeleportResponsePayload(buf.readInt(), buf.readInt())
    );

    // 处理网络包的方法
    public static void handle(final TeleportResponsePayload payload, final IPayloadContext context) {
        // 在客户端线程上执行
        context.enqueueWork(() -> {
            // 获取玩家
            var player = context.player();
            if (player != null) {
                if (payload.playerCount() >= 0) {
                    // 显示结果消息
                    player.displayClientMessage(
                            Component.literal("服务器中共有 " + payload.playerCount() +
                                    " 名玩家，成功传送了 " + payload.teleportedCount() +
                                    " 名玩家到平台!"),
                            false
                    );

                    // 如果有玩家传送失败
                    if (payload.teleportedCount() < payload.playerCount()) {
                        player.displayClientMessage(
                                Component.literal("警告: " + (payload.playerCount() - payload.teleportedCount()) +
                                        " 名玩家传送失败!"),
                                false
                        );
                    }
                } else {
                    // 平台生成失败
                    player.displayClientMessage(
                            Component.literal("平台生成失败!"),
                            false
                    );
                }
            }
        }).exceptionally(e -> {
            // 处理异常
            return null;
        });
    } @Override
      public Type<? extends CustomPacketPayload> type() {
          return TYPE;
    }
}