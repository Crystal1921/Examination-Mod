package com.mcg.examinationmod.gui;

import com.mcg.examinationmod.network.PlatformRequestPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class FubenBlockGui extends Screen {
    private EditBox textInput;
    private Button submitButton;
    private Level level;

    public FubenBlockGui(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init(); // 必须调用父类init方法

        // 计算屏幕中心位置
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // 初始化输入框
        this.textInput = new EditBox(
                this.font,          // 字体渲染器
                centerX - 100,      // x位置
                centerY - 30,       // y位置
                200,                // 宽度
                20,                 // 高度
                Component.literal("输入命令") // 提示文本
        );

        // 设置输入框的最大长度
        this.textInput.setMaxLength(50);

        // 添加输入框到屏幕
        this.addRenderableWidget(this.textInput);

        // 初始化提交按钮
        this.submitButton = Button.builder(
                Component.literal("生成平台"),
                this::onSubmit
        ).bounds(
                centerX - 50,       // x位置
                centerY + 10,       // y位置
                100,                // 宽度
                20                  // 高度
        ).build();

        // 添加按钮到屏幕
        this.addRenderableWidget(this.submitButton);

        // 设置初始焦点到输入框
        this.setInitialFocus(this.textInput);
    }

    private void onSubmit(Button button) {
        // 确保textInput不为null
        if (this.textInput != null) {
            String inputText = this.textInput.getValue();
            PacketDistributor.sendToServer(new PlatformRequestPacket(inputText));
            if (this.minecraft != null) {
                this.minecraft.setScreen(null);
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // 渲染所有组件（包括输入框和按钮）
        super.render(graphics, mouseX, mouseY, partialTicks);

        // 渲染标题
        graphics.drawString(
                this.font,
                this.title,
                this.width / 2 - this.font.width(this.title) / 2,
                20,
                0xFFFFFF
        );

        // 渲染输入框标签
        graphics.drawString(
                this.font,
                Component.literal("输入 'platform' 生成平台:"),
                this.width / 2 - 100,
                this.height / 2 - 45,
                0xFFFFFF
        );
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // 处理回车键提交
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            if (this.textInput != null && this.textInput.isFocused()) {
                this.onSubmit(this.submitButton);
                return true;
            }
        }

        // 处理ESC键关闭
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            if (this.minecraft != null) {
                this.minecraft.setScreen(null);
            }
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
