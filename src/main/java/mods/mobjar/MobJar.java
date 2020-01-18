package mods.mobjar;

import mods.mobjar.mobjar.MobJarBlock;
import mods.mobjar.mobjar.MobJarBlockEntity;
import mods.mobjar.mobjar.MobJarBlockItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MobJar implements ModInitializer {

    public static Block MOB_JAR = new MobJarBlock(Block.Settings.copy(Blocks.GLASS));
    public static final BlockEntityType<MobJarBlockEntity> MOB_JAR_BLOCK_ENTITY=BlockEntityType.Builder.create(MobJarBlockEntity::new,MobJar.MOB_JAR).build(null);
    public static BlockItem MOB_JAR_ITEM = new MobJarBlockItem(MOB_JAR,new Item.Settings().group(ItemGroup.DECORATIONS));

    @Override
    public void onInitialize(){
        Registry.register(Registry.BLOCK,getId("mob_jar"),MOB_JAR);
        Registry.register(Registry.BLOCK_ENTITY,getId("mob_jar"),MOB_JAR_BLOCK_ENTITY);
        Registry.register(Registry.ITEM,getId("mob_jar"),MOB_JAR_ITEM);
    }

    public static Identifier getId(String name) {
        return new Identifier("mobjar", name);
    }
}
