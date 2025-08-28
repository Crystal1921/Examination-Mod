package com.mcg.examinationmod.event;

import com.ibm.icu.text.CaseMap;
import com.mcg.examinationmod.ExaminationMod;
import com.mcg.examinationmod.gui.FubenBlockGui;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class GuiOpenWrapper {
    public static void openFubenBlockGui(){
        Minecraft.getInstance().setScreen(new FubenBlockGui(Component.translatable("1")));
    }
}
