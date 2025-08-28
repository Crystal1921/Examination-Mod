package com.mcg.examinationmod.event;

import com.mcg.examinationmod.network.PlatformRequestPacket;
import net.minecraft.core.BlockPos;
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
public class FuBenBlockClientEvents {
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
        if (level.isClientSide) {
            if (state.is(FUBEN))
                if (stack.is(START_TEST_ITEM.get())) {
                    PacketDistributor.sendToServer(new PlatformRequestPacket("start"));
                } else if (stack.is(PARTY_TEST_ITEM.get())){
                    PacketDistributor.sendToServer(new PlatformRequestPacket("party"));
                } else if (stack.is(TEAM_TEST_ITEM.get())){
                    PacketDistributor.sendToServer(new PlatformRequestPacket("team"));
                } else {
                    GuiOpenWrapper.openFubenBlockGui();
                }
        }
    }
}