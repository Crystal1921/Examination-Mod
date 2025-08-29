package com.mcg.examinationmod.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static com.mcg.examinationmod.ExaminationMod.DUNGEON_BLOCK;

@EventBusSubscriber
public class DungeonBlockClientEvents {
    @SubscribeEvent
    public static void DungeonBlockClientEvent(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        state.getBlock();
        if (level.isClientSide) {
            if (state.is(DUNGEON_BLOCK))
                GuiOpenWrapper.DungeonBlockGui();
        }
    }
}
