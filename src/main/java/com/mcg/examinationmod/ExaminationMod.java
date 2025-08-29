package com.mcg.examinationmod;

import com.mcg.examinationmod.block.DungeonBlock;
import com.mcg.examinationmod.item.PartyTestItem;
import com.mcg.examinationmod.item.StartTestItem;
import com.mcg.examinationmod.item.TeamTestItem;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(ExaminationMod.MODID)
public class ExaminationMod {
    public static final String MODID = "examinationmod";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<Block> DUNGEON_BLOCK = BLOCKS.register("dungeon_block",
            ()-> new DungeonBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    public static final DeferredItem<BlockItem> DUNGEON_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("dungeon_block", DUNGEON_BLOCK);

    public static final DeferredItem<Item> START_TEST_ITEM = ITEMS.register("start_test_item",
            ()-> new StartTestItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> PARTY_TEST_ITEM = ITEMS.register("party_test_item",
            ()-> new PartyTestItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> TEAM_TEST_ITEM = ITEMS.register("team_test_item",
            ()-> new TeamTestItem(new Item.Properties().stacksTo(1)));

    // Creates a creative tab with the id "examinationmod:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.examinationmod")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> DUNGEON_BLOCK_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(DUNGEON_BLOCK_ITEM);
                output.accept(START_TEST_ITEM);
                output.accept(PARTY_TEST_ITEM);
                output.accept(TEAM_TEST_ITEM);
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ExaminationMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExaminationMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(DUNGEON_BLOCK);
        }
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
