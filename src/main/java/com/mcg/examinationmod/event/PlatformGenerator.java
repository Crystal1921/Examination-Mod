package com.mcg.examinationmod.event;
import com.mcg.examinationmod.ExaminationMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class PlatformGenerator {

    public static BlockPos generatePlatformAbovePlayer(Player player, boolean placeCustomBlock) {
        Level level = player.level();

        BlockPos playerPos = player.blockPosition();
        BlockPos centerPos = playerPos.above(5); // 默认高度5格

        // 生成3x3平台
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos blockPos = centerPos.offset(x, 0, z);
                // 只在空气或可替换方块上放置平台方块
                if (level.isEmptyBlock(blockPos) || level.getBlockState(blockPos).canBeReplaced()) {
                    if (level instanceof ServerLevel serverLevel) {
                        System.out.println("放置方块在位置: " + blockPos);
                        serverLevel.setBlock(blockPos, Blocks.STONE.defaultBlockState(), 3);
                    }
                }
            }
        }
        player.teleportTo(
                centerPos.getX() + 0.5,
                centerPos.getY() + 2.0,
                centerPos.getZ() + 0.5
        );
        if (placeCustomBlock) {
            BlockPos centerAbovePos = centerPos.above(1); // 平台中心上方一格
            if (level.isEmptyBlock(centerAbovePos) || level.getBlockState(centerAbovePos).canBeReplaced()) {
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.setBlock(centerAbovePos, ExaminationMod.FUBEN.get().defaultBlockState(), 3);

                }

            }
        }
        return playerPos;
    }

    public static BlockPos generatePlatformAbovePlayer(Player player) {
        return generatePlatformAbovePlayer(player, true);
    }
}
