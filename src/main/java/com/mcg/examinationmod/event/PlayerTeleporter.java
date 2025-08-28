package com.mcg.examinationmod.event;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class PlayerTeleporter {
    private static List<String> teleportedPlayers = new ArrayList<>();
    /**
     * 获取服务器中的玩家数量
     * @param level 服务器世界
     * @return 玩家数量
     */
    public static int getPlayerCount(ServerLevel level) {
        if (level == null) {
            return 0;
        }

        // 获取所有玩家
        List<ServerPlayer> players = level.getPlayers(player -> true);
        return players.size();
    }

    /**
     * 将所有玩家传送到指定位置
     * @param level 服务器世界
     * @param centerPos 目标位置（平台中心）
     * @return 成功传送的玩家数量
     */
    public static int teleportAllPlayersToPlatform(ServerLevel level, BlockPos centerPos) {
        if (level == null || centerPos == null) {
            return 0;
        }
        // 获取所有玩家
        List<ServerPlayer> players = level.getPlayers(player -> true);
        int teleportedCount = 0;

        // 计算传送位置（平台中心上方一格，避免卡在方块内）
        BlockPos teleportPos = centerPos.above(1);
        double teleportX = teleportPos.getX() + 0.5; // 方块中心
        double teleportY = teleportPos.getY() + 5.0;
        double teleportZ = teleportPos.getZ() + 0.5; // 方块中心

        // 传送所有玩家
        for (ServerPlayer player : players) {
            try {
                // 传送玩家
                player.teleportTo(
                        level,
                        teleportX,
                        teleportY,
                        teleportZ,
                        player.getYRot(), // 保持原有朝向
                        player.getXRot()  // 保持原有俯仰角
                );
                teleportedPlayers.add(player.getName().getString());
                teleportedCount++;
            } catch (Exception e) {
                // 记录传送失败
                System.err.println("无法传送玩家 " + player.getName().getString() + ": " + e.getMessage());
            }
        }

        return teleportedCount;
    }

    /**
     * 获取被传送的玩家名单作为字符串
     * @return 逗号分隔的玩家名单字符串
     */
    public static String getTeleportedPlayersAsString() {
        return String.join(", ", teleportedPlayers);
    }

    /**
     * 清空被传送的玩家名单
     */
    public static void clearTeleportedPlayers() {
        teleportedPlayers.clear();
    }
    /**
     * 获取玩家数量并传送所有玩家
     * @param level 服务器世界
     * @param centerPos 目标位置（平台中心）
     * @return 包含玩家总数和成功传送数量的数组 [总玩家数, 成功传送数]
     */
    }
