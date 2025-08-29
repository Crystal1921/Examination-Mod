package com.mcg.examinationmod.event;

import com.mcg.examinationmod.gui.DungeonBlockGui;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class GuiOpenWrapper {
    public static void DungeonBlockGui(){
        Minecraft.getInstance().setScreen(new DungeonBlockGui(Component.translatable("菜单")));
    }
}
