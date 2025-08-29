package com.mcg.examinationmod.event;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static com.mcg.examinationmod.ExaminationMod.*;
@EventBusSubscriber
// 副本方块客户端事件
public class DungeonBlockClientEvents {
    @SubscribeEvent
    public static void FuBenBlockEvent(PlayerInteractEvent.RightClickBlock event) {
        //TODO : 这里可以直接只监听服务端事件，然后直接处理，不需要再客户端发包给服务端了

        //TODO : 原版会触发两次，主手一次，副手一次，导致物品右击产生bug

        Level level = event.getLevel();
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        state.getBlock();
        if (!level.isClientSide && event.getHand() == InteractionHand.MAIN_HAND) {
            if (state.is(FUBEN))
                if (stack.is(START_TEST_ITEM.get())) {
                    PlatformGenerator.generatePlatformAbovePlayer(player);
                } else if (stack.is(PARTY_TEST_ITEM.get())){
                    if (player.level() instanceof ServerLevel serverLevel) {
                        PlayerTeleporter.clearTeleportedPlayers();
                        BlockPos platformCenter = PlatformGenerator.generatePlatformAbovePlayer(player, false);
                        // 传送所有玩家到平台
                        PlayerTeleporter.teleportAllPlayersToPlatform(serverLevel, platformCenter);
                    }
                } else if (stack.is(TEAM_TEST_ITEM.get())){
                    String playersList = PlayerTeleporter.getTeleportedPlayersAsString();
                    player.sendSystemMessage(Component.literal("被传送的玩家名单:"));
                    player.sendSystemMessage(Component.literal(playersList));
                } else {
                    GuiOpenWrapper.openFubenBlockGui();
                }
        }
    }
}