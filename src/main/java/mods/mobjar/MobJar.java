package mods.mobjar;

import mods.mobjar.mobjar.MobJarBlock;
import mods.mobjar.mobjar.MobJarBlockEntity;
import mods.mobjar.mobjar.MobJarBlockItem;
import mods.mobjar.mobjar.StainedMobJarBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MobJar implements ModInitializer {
    public static Block MOB_JAR = new MobJarBlock(Block.Settings.copy(Blocks.GLASS));
    public static Block WHITE_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.WHITE,Block.Settings.copy(Blocks.GLASS));
    public static Block ORANGE_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.ORANGE,Block.Settings.copy(Blocks.GLASS));
    public static Block MAGENTA_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.MAGENTA,Block.Settings.copy(Blocks.GLASS));
    public static Block LIGHT_BLUE_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.LIGHT_BLUE,Block.Settings.copy(Blocks.GLASS));
    public static Block YELLOW_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.YELLOW,Block.Settings.copy(Blocks.GLASS));
    public static Block LIME_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.LIME,Block.Settings.copy(Blocks.GLASS));
    public static Block PINK_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.PINK,Block.Settings.copy(Blocks.GLASS));
    public static Block GRAY_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.GRAY,Block.Settings.copy(Blocks.GLASS));
    public static Block LIGHT_GRAY_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.LIGHT_GRAY,Block.Settings.copy(Blocks.GLASS));
    public static Block CYAN_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.CYAN,Block.Settings.copy(Blocks.GLASS));
    public static Block PURPLE_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.PURPLE,Block.Settings.copy(Blocks.GLASS));
    public static Block BLUE_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.BLUE,Block.Settings.copy(Blocks.GLASS));
    public static Block BROWN_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.BROWN,Block.Settings.copy(Blocks.GLASS));
    public static Block GREEN_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.GREEN,Block.Settings.copy(Blocks.GLASS));
    public static Block RED_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.RED,Block.Settings.copy(Blocks.GLASS));
    public static Block BLACK_STAINED_MOB_JAR = new StainedMobJarBlock(DyeColor.BLACK,Block.Settings.copy(Blocks.GLASS));

    public static final BlockEntityType<MobJarBlockEntity> MOB_JAR_BLOCK_ENTITY=BlockEntityType.Builder.create(MobJarBlockEntity::new,
            MOB_JAR,
            WHITE_STAINED_MOB_JAR,
            ORANGE_STAINED_MOB_JAR,
            MAGENTA_STAINED_MOB_JAR,
            LIGHT_BLUE_STAINED_MOB_JAR,
            YELLOW_STAINED_MOB_JAR,
            LIME_STAINED_MOB_JAR,
            PINK_STAINED_MOB_JAR,
            GRAY_STAINED_MOB_JAR,
            LIGHT_GRAY_STAINED_MOB_JAR,
            CYAN_STAINED_MOB_JAR,
            PURPLE_STAINED_MOB_JAR,
            BLUE_STAINED_MOB_JAR,
            BROWN_STAINED_MOB_JAR,
            GREEN_STAINED_MOB_JAR,
            RED_STAINED_MOB_JAR,
            BLACK_STAINED_MOB_JAR
    ).build(null);

    @Override
    public void onInitialize(){
        register("mob_jar",MOB_JAR);
        register("white_stained_mob_jar",WHITE_STAINED_MOB_JAR);
        register("orange_stained_mob_jar",ORANGE_STAINED_MOB_JAR);
        register("magenta_stained_mob_jar",MAGENTA_STAINED_MOB_JAR);
        register("light_blue_stained_mob_jar",LIGHT_BLUE_STAINED_MOB_JAR);
        register("yellow_stained_mob_jar",YELLOW_STAINED_MOB_JAR);
        register("lime_stained_mob_jar",LIME_STAINED_MOB_JAR);
        register("pink_stained_mob_jar",PINK_STAINED_MOB_JAR);
        register("gray_stained_mob_jar",GRAY_STAINED_MOB_JAR);
        register("light_gray_stained_mob_jar",LIGHT_GRAY_STAINED_MOB_JAR);
        register("cyan_stained_mob_jar",CYAN_STAINED_MOB_JAR);
        register("purple_stained_mob_jar",PURPLE_STAINED_MOB_JAR);
        register("blue_stained_mob_jar",BLUE_STAINED_MOB_JAR);
        register("brown_stained_mob_jar",BROWN_STAINED_MOB_JAR);
        register("green_stained_mob_jar",GREEN_STAINED_MOB_JAR);
        register("red_stained_mob_jar",RED_STAINED_MOB_JAR);
        register("black_stained_mob_jar",BLACK_STAINED_MOB_JAR);

        Registry.register(Registry.BLOCK_ENTITY_TYPE,getId("mob_jar"),MOB_JAR_BLOCK_ENTITY);
    }

    public static Identifier getId(String name) {
        return new Identifier("mobjar", name);
    }

    public static BlockItem register(String name, Block block) {
        return register(name, block, new Item.Settings().group(mobJarCoreGroup));
    }

    public static BlockItem register(String name, Block block, Item.Settings settings) {
        Identifier id = getId(name);
        Registry.register(Registry.BLOCK, id, block);
        BlockItem item = new MobJarBlockItem(block, settings);
        item.appendBlocks(Item.BLOCK_ITEMS, item);
        Registry.register(Registry.ITEM, id, item);
        return item;
    }

    public static ItemGroup mobJarCoreGroup = FabricItemGroupBuilder.build(
            getId("core_group"),
            () -> new ItemStack(MOB_JAR));
}
