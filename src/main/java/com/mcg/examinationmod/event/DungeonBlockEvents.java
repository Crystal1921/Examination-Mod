package com.mcg.examinationmod.event;

import com.mcg.examinationmod.network.PlatformRequestPacket;
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
import net.neoforged.neoforge.network.PacketDistributor;

import static com.mcg.examinationmod.ExaminationMod.*;
@EventBusSubscriber
// 副本方块客户端事件
public class DungeonBlockEvents {
    @SubscribeEvent
    public static void DungeonBlockEvent(PlayerInteractEvent.RightClickBlock event) {
        //TODO : 原版会触发两次，主手一次，副手一次，导致物品右击产生bug
        //因为要避免指定物品右键的时候打开Gui，然后Gui只能在客户端打开，所以用了网络包来处理效果
        Level level = event.getLevel();
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        state.getBlock();
        if (level.isClientSide && event.getHand() == InteractionHand.MAIN_HAND) {
            if (state.is(DUNGEON_BLOCK))
                if (stack.is(START_TEST_ITEM.get())) {
                    //客户端到服务端
                    PacketDistributor.sendToServer(new PlatformRequestPacket("start"));
                } else if (stack.is(PARTY_TEST_ITEM.get())){
                    PacketDistributor.sendToServer(new PlatformRequestPacket("party"));
                } else if (stack.is(TEAM_TEST_ITEM.get())) {
                    PacketDistributor.sendToServer(new PlatformRequestPacket("team"));
                } else {
                    GuiOpenWrapper.DungeonBlockGui();
                }
        }
    }
}